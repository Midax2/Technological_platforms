using System;
using System.IO;
using System.Net;
using System.Net.Sockets;
using System.Text.Json;
public class Data
{
    public int NumberA { get; set; }
    public int NumberB { get; set; }
    public int? Result { get; set; }
    public string Content { get; set; } = string.Empty;
}

class Program
{
    static async Task Main(string[] args)
    {
        TcpClient client = null;

        try
        {
            client = new TcpClient("127.0.0.1", 1234);
            Console.WriteLine("Connected to server.");
        }
        catch (SocketException ex)
        {
            Console.WriteLine($"Failed to connect to server: {ex.Message}");
            return;
        }

        var streamWriter = new StreamWriter(client.GetStream());
        var streamReader = new StreamReader(client.GetStream());

        while (true)
        {
            Console.Write("Enter NumberA: ");
            var numberA = int.Parse(Console.ReadLine());

            Console.Write("Enter NumberB: ");
            var numberB = int.Parse(Console.ReadLine());

            Console.Write("Enter Content: ");
            var content = Console.ReadLine();

            var data = new Data
            {
                NumberA = numberA,
                NumberB = numberB,
                Content = content
            };

            var serializedData = JsonSerializer.Serialize(data);

            await streamWriter.WriteLineAsync(serializedData);
            await streamWriter.FlushAsync();

            var response = await streamReader.ReadLineAsync();
            if (!string.IsNullOrWhiteSpace(response))
            {
                var responseData = JsonSerializer.Deserialize<Data>(response);
                Console.WriteLine($"Result: {responseData.Result}");
            }
        }
    }
}