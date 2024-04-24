using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Runtime.Serialization;
using System.Runtime.Serialization.Formatters.Binary;
using System.Text;
using System.Threading.Tasks;
using System.Xml;



namespace laby7
{
    public static class Extensions
    {
        public static DateTime GetOldestFile(this DirectoryInfo directoryInfo)
        {
            DateTime oldestDateTime = DateTime.Now;
            DirectoryInfo[] subDirectories = directoryInfo.GetDirectories();
            foreach (var subDirectory in subDirectories)
            {
                DateTime subDirectoryOldestDateTime = subDirectory.GetOldestFile();
                if (subDirectoryOldestDateTime < oldestDateTime)
                {
                    oldestDateTime = subDirectoryOldestDateTime;
                }
            }
            FileInfo[] files = directoryInfo.GetFiles();
            foreach (var file in files)
            {
                DateTime fileDateTime = file.CreationTime;
                if (fileDateTime < oldestDateTime)
                {
                    oldestDateTime = fileDateTime;
                }
            }

            return oldestDateTime;
        }

        public static string GetDOSAttributes(this FileSystemInfo fileSystemInfo)
        {
            FileAttributes attributes = fileSystemInfo.Attributes;
            return $"{(attributes.HasFlag(FileAttributes.ReadOnly) ? 'r' : '-')}" +
                   $"{(attributes.HasFlag(FileAttributes.Archive) ? 'a' : '-')}" +
                   $"{(attributes.HasFlag(FileAttributes.Hidden) ? 'h' : '-')}" +
                   $"{(attributes.HasFlag(FileAttributes.System) ? 's' : '-')}";
        }
    }

    [Serializable]
    public class StringComparator : IComparer<string>
    {

        public int Compare(string x, string y)
        {
            //porownanie dlugosci
            int lengthComparison = x.Length.CompareTo(y.Length);
            if (lengthComparison != 0)
            {
                return lengthComparison;
            }
            return string.Compare(x, y, StringComparison.Ordinal); //porownanie leksykograficzne z microsoft
        }
    }

    internal class Program
    {


        static void SerializeData<T>(T data, string dir)
        {
            DataContractSerializer serializer = new DataContractSerializer(typeof(T));
            using (FileStream fileStream = new FileStream(dir, FileMode.Create))
            {
                using (XmlWriter xmlWriter = XmlWriter.Create(fileStream))
                {
                    serializer.WriteObject(xmlWriter, data);
                }
            }
        }

        static T DeserializeData<T>(string dir)
        {
            DataContractSerializer serializer = new DataContractSerializer(typeof(T));
            using (FileStream fileStream = new FileStream(dir, FileMode.Open))
            {
                using (XmlReader xmlReader = XmlReader.Create(fileStream))
                {
                    return (T)serializer.ReadObject(xmlReader);
                }
            }
        }

        static void ListAllFilesShifted(string directory, int wciecie)
        {
            DirectoryInfo dirInfo = new DirectoryInfo(directory);
            FileSystemInfo[] fileSystemInfos = dirInfo.GetFileSystemInfos();
            foreach (var info in fileSystemInfos)
            {
                if (info is FileInfo fileInfo)

                    Console.WriteLine($"{new string(' ', wciecie * 2)}{fileInfo.Name} (Size: {fileInfo.Length} bytes) {fileInfo.GetDOSAttributes()}");

                else if (info is DirectoryInfo directoryInfo)
                {
                    int directoryCount = directoryInfo.GetFileSystemInfos().Length;
                    Console.WriteLine($"{new string(' ', wciecie * 2)}{directoryInfo.Name} (Contains: {directoryCount} items) {directoryInfo.GetDOSAttributes()}");
                    ListAllFilesShifted(directoryInfo.FullName, wciecie + 1);
                }
            }
        }

        static void LoadDirectoryData(string directory)
        {
            DirectoryInfo dirInfo = new DirectoryInfo(directory);
            var items = new SortedDictionary<string, long>(new StringComparator());


            foreach (var info in dirInfo.GetDirectories())
            {
                items[info.Name] = info.GetFileSystemInfos().Length;
            }
            foreach (var info in dirInfo.GetFiles())
            {
                items[info.Name] = info.Length;
            }

            //tutaj część na serializacje i deserializacje

            //Serializacja

            SerializeData(items, directory + "\\\\test.txt");

            var items2 = DeserializeData<SortedDictionary<string, long>>(directory + "\\\\test.txt");


            Console.WriteLine("Wynikowa: ");

            foreach (var i in items2)
            {
                Console.WriteLine($"{i.Key} ----- {i.Value}");
            }

            //foreach (var item in items)
            //{
            //    Console.WriteLine($"{item.Key} ({item.Value})");
            //}
        }

        static void Main(string[] args)
        {
            if (args.Length > 0)
            {
                string directoryPath = args[0];
                DirectoryInfo directoryInfo = new DirectoryInfo(directoryPath);
                Console.WriteLine("Zawartość katalogu: " + directoryPath);

                try
                {
                    ListAllFilesShifted(directoryPath, 0);
                    Console.WriteLine("Najstarszy plik w katalogu: " + Extensions.GetOldestFile(directoryInfo));
                    LoadDirectoryData(directoryPath);
                    //Console.WriteLine("Atrybuty pliku: " + Extensions.GetDOSAttributes(directoryInfo));
                    // Console.WriteLine("Najstarszy plik w katalogu: " + Extensions.OldestFile);
                }
                catch (Exception e)
                {
                    Console.WriteLine("Nie mozna dostac sie do folderud");
                }

            }
            else
            {
                Console.WriteLine("Nie mozna dostac sie do folderu");
            }
        }

    }
}
