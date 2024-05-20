using Lab_8;
using System;
using System.IO;
using System.Text;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Forms;
using System.Windows.Input;
using System.Windows.Interop;

namespace Lab_8
{
    public partial class MainWindow : Window
    {
        private DirectoryInfo? _currentDirectory = null;

        public MainWindow()
        {
            InitializeComponent();
            TreeView.ContextMenu = new ContextMenu();
        }

        private void OnMenuExitClick(object sender, RoutedEventArgs e) => Close();

        private void OnMenuOpenClick(object sender, RoutedEventArgs e)
        {
            var dialog = new FolderBrowserDialog { Description = "Select directory to open", UseDescriptionForTitle = true };
            var win32Parent = new NativeWindow();
            win32Parent.AssignHandle(new WindowInteropHelper(this).Handle);
            var result = dialog.ShowDialog(win32Parent);
            if (result != System.Windows.Forms.DialogResult.OK) return;

            if (!Directory.Exists(dialog.SelectedPath))
            {
                System.Windows.MessageBox.Show(this, "Invalid path!", "Error!", MessageBoxButton.OK, MessageBoxImage.Error);
                return;
            }

            _currentDirectory = new DirectoryInfo(dialog.SelectedPath);
            DisplayFiles();
        }

        private void DisplayFiles()
        {
            TreeView.Items.Clear();

            if (_currentDirectory == null) return;

            foreach (var directory in _currentDirectory.EnumerateDirectories())
            {
                TreeView.Items.Add(GetTreeViewItem(directory));
            }

            foreach (var file in _currentDirectory.EnumerateFiles())
            {
                TreeView.Items.Add(GetTreeViewItem(file));
            }

            TreeView.ContextMenu?.Items.Clear();
            var createMenuItem = new MenuItem { Header = "Create", Tag = _currentDirectory.FullName };
            createMenuItem.Click += TreeViewDirectoryItem_OnCreate;
            TreeView.ContextMenu?.Items.Add(createMenuItem);
        }

        private TreeViewItem GetTreeViewItem(DirectoryInfo directoryInfo)
        {
            var root = CreateTreeViewItem(directoryInfo.Name, directoryInfo.FullName);

            foreach (var directory in directoryInfo.EnumerateDirectories())
            {
                root.Items.Add(GetTreeViewItem(directory));
            }

            foreach (var file in directoryInfo.EnumerateFiles())
            {
                root.Items.Add(GetTreeViewItem(file));
            }

            return root;
        }

        private TreeViewItem GetTreeViewItem(FileInfo fileInfo)
        {
            var item = CreateTreeViewItem(fileInfo.Name, fileInfo.FullName);
            item.Selected += TreeViewFileItem_OnSelected;
            item.MouseDoubleClick += TreeViewFileItem_OnDoubleClick;

            var openMenuItem = CreateMenuItem("Open", fileInfo.FullName, OnFileOpen);
            var deleteMenuItem = CreateMenuItem("Delete", fileInfo.FullName, OnFileDelete);

            item.ContextMenu.Items.Add(openMenuItem);
            item.ContextMenu.Items.Add(deleteMenuItem);

            return item;
        }

        private void TreeViewFileItem_OnDoubleClick(object sender, MouseButtonEventArgs e)
        {
            if (!(e.Source is TreeViewItem item) || !(item.Tag is string path) || !File.Exists(path))
            {
                System.Windows.MessageBox.Show(this, "Invalid path", "Error", MessageBoxButton.OK, MessageBoxImage.Error);
                return;
            }

            fileViewer.Text = File.ReadAllText(path, Encoding.UTF8);
        }

        private void TreeViewFileItem_OnSelected(object sender, RoutedEventArgs e)
        {
            if (!(e.Source is TreeViewItem menuItem) || !(menuItem.Tag is string path) || !File.Exists(path))
            {
                System.Windows.MessageBox.Show(this, "Invalid path", "Error", MessageBoxButton.OK, MessageBoxImage.Error);
                return;
            }

            var attributes = File.GetAttributes(path);
            var dosAttributes = new StringBuilder();
            dosAttributes.Append((attributes & FileAttributes.ReadOnly) != 0 ? 'R' : '-');
            dosAttributes.Append((attributes & FileAttributes.Archive) != 0 ? 'A' : '-');
            dosAttributes.Append((attributes & FileAttributes.Hidden) != 0 ? 'H' : '-');
            dosAttributes.Append((attributes & FileAttributes.System) != 0 ? 'S' : '-');

            AttributeTextBlock.Text = dosAttributes.ToString();
        }

        private void OnFileOpen(object sender, RoutedEventArgs e) => OpenFile(sender);

        private void OpenFile(object sender)
        {
            if (!(sender is MenuItem menuItem) || !(menuItem.Tag is string path) || !File.Exists(path))
            {
                System.Windows.MessageBox.Show(this, "Invalid path", "Error", MessageBoxButton.OK, MessageBoxImage.Error);
                return;
            }

            fileViewer.Text = File.ReadAllText(path, Encoding.UTF8);
        }

        private void TreeViewDirectoryItem_OnCreate(object sender, RoutedEventArgs e)
        {
            if (!(sender is MenuItem menuItem) || !(menuItem.Tag is string path) || !Directory.Exists(path))
            {
                System.Windows.MessageBox.Show("Invalid path", "Error", MessageBoxButton.OK, MessageBoxImage.Error);
                return;
            }

            var window = new CreateFileWindow(new DirectoryInfo(path));
            window.ShowDialog();
            DisplayFiles();
        }

        private void TreeViewDirectoryItem_OnDelete(object sender, RoutedEventArgs e)
        {
            if (!(sender is MenuItem menuItem) || !(menuItem.Tag is string path) || !Directory.Exists(path))
            {
                System.Windows.MessageBox.Show("Invalid path", "Error", MessageBoxButton.OK, MessageBoxImage.Error);
                return;
            }

            foreach (var file in Directory.EnumerateFiles(path, "*", SearchOption.AllDirectories))
            {
                if (File.GetAttributes(file).HasFlag(FileAttributes.ReadOnly))
                {
                    File.SetAttributes(file, File.GetAttributes(file) & ~FileAttributes.ReadOnly);
                }

                File.Delete(file);
            }

            Directory.Delete(path);
            DisplayFiles();
        }

        private void OnFileDelete(object sender, RoutedEventArgs e) => DeleteFile(sender);

        private void DeleteFile(object sender)
        {
            if (!(sender is MenuItem menuItem) || !(menuItem.Tag is string path) || !File.Exists(path))
            {
                System.Windows.MessageBox.Show("Invalid path", "Error", MessageBoxButton.OK, MessageBoxImage.Error);
                return;
            }

            if (File.GetAttributes(path).HasFlag(FileAttributes.ReadOnly))
            {
                File.SetAttributes(path, File.GetAttributes(path) & ~FileAttributes.ReadOnly);
            }

            File.Delete(path);
            DisplayFiles();
        }

        private TreeViewItem CreateTreeViewItem(string header, string fullPath)
        {
            return new TreeViewItem
            {
                Header = header,
                Tag = fullPath,
                ContextMenu = new ContextMenu()
            };
        }

        private MenuItem CreateMenuItem(string header, string fullPath, RoutedEventHandler eventHandler)
        {
            var menuItem = new MenuItem { Header = header, Tag = fullPath };
            menuItem.Click += eventHandler;
            return menuItem;
        }
    }
}
