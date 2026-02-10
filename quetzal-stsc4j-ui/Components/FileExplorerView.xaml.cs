using System;
using System.IO;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Input;

namespace quetzal_stsc4j_ui.Components;

public partial class FileExplorerView : UserControl
{
    private string _rootPath;
    public event Action<string> FileSelected;

    public FileExplorerView()
    {
        InitializeComponent();
    }

    public void LoadDirectory(string path)
    {
        _rootPath = path;
        FileExplorer.Items.Clear();
        
        if (!Directory.Exists(path))
        {
            return;
        }

        var rootDirectoryInfo = new DirectoryInfo(path);
        var rootItem = CreateTreeItem(rootDirectoryInfo);
        
        // Expandir la raíz automáticamente
        rootItem.IsExpanded = true;
        
        FileExplorer.Items.Add(rootItem);
    }

    private TreeViewItem CreateTreeItem(object item)
    {
        TreeViewItem treeItem = new TreeViewItem();

        // Configurar menú contextual
        treeItem.ContextMenu = new ContextMenu();
        treeItem.ContextMenuOpening += (s, e) => OnTreeItemContextMenuOpening(s as TreeViewItem, e);

        treeItem.PreviewMouseRightButtonDown += (s, e) =>
        {
            TreeViewItem source = s as TreeViewItem;
            if (source != null)
            {
                source.Focus();
                source.IsSelected = true;
                e.Handled = false; // Permitir que se abra el menú contextual
            }
        };

        if (item is DirectoryInfo directory)
        {
            // Crear header con icono de carpeta y tema claro
            var headerPanel = CreateHeaderWithIcon("📁", directory.Name, isBold: true);
            treeItem.Header = headerPanel;
            treeItem.Tag = directory.FullName;
            treeItem.FontWeight = System.Windows.FontWeights.Bold;

            try
            {
                // Cargar todos los subdirectorios
                var subdirectories = directory.GetDirectories();
                foreach (var dir in subdirectories)
                {
                    try
                    {
                        var subItem = CreateTreeItem(dir);
                        treeItem.Items.Add(subItem);
                    }
                    catch
                    {
                        // Ignorar directorios sin acceso
                    }
                }

                // Cargar todos los archivos
                var files = directory.GetFiles();
                foreach (var file in files)
                {
                    try
                    {
                        var fileItem = CreateTreeItem(file);
                        treeItem.Items.Add(fileItem);
                    }
                    catch
                    {
                        // Ignorar archivos sin acceso
                    }
                }
            }
            catch (UnauthorizedAccessException)
            {
                // Sin acceso a directorio
            }
        }
        else if (item is FileInfo file)
        {
            // Crear header con icono de archivo según extensión
            string fileIcon = GetFileIcon(file.Extension);
            var headerPanel = CreateHeaderWithIcon(fileIcon, file.Name, isBold: false);
            treeItem.Header = headerPanel;
            treeItem.Tag = file.FullName;
        }

        return treeItem;
    }

    /// <summary>
    /// Crea un panel con icono y texto para el header del TreeViewItem con colores del tema claro.
    /// </summary>
    private StackPanel CreateHeaderWithIcon(string icon, string text, bool isBold = false)
    {
        var panel = new StackPanel
        {
            Orientation = Orientation.Horizontal,
            Margin = new Thickness(0),
            Background = System.Windows.Media.Brushes.Transparent
        };

        // Icono - Usa el color del tema claro
        var iconBlock = new TextBlock
        {
            Text = icon,
            FontSize = 14,
            Margin = new Thickness(0, 0, 8, 0),
            VerticalAlignment = System.Windows.VerticalAlignment.Center,
            Foreground = new System.Windows.Media.SolidColorBrush(System.Windows.Media.Color.FromRgb(51, 51, 51))  // Gris oscuro (#333333)
        };
        panel.Children.Add(iconBlock);

        // Texto con colores del tema claro
        var textBlock = new TextBlock
        {
            Text = text,
            VerticalAlignment = System.Windows.VerticalAlignment.Center,
            FontWeight = isBold ? System.Windows.FontWeights.Bold : System.Windows.FontWeights.Normal,
            Foreground = new System.Windows.Media.SolidColorBrush(System.Windows.Media.Color.FromRgb(51, 51, 51))  // Gris oscuro (#333333)
        };
        panel.Children.Add(textBlock);

        return panel;
    }

    /// <summary>
    /// Obtiene el icono apropiado según la extensión del archivo.
    /// </summary>
    private string GetFileIcon(string extension)
    {
        return extension?.ToLower() switch
        {
            // Lenguajes de programación
            ".java" => "☕",
            ".cs" => "🔷",
            ".cpp" => "⚡",
            ".c" => "⚡",
            ".h" => "📄",
            ".py" => "🐍",
            ".js" => "📜",
            ".ts" => "📜",
            ".php" => "🐘",
            ".rb" => "💎",
            ".go" => "🐹",
            ".rs" => "🦀",

            // Configuración y markup
            ".xml" => "📋",
            ".json" => "📋",
            ".yaml" => "📋",
            ".yml" => "📋",
            ".config" => "⚙️",
            ".properties" => "⚙️",
            ".ini" => "⚙️",
            ".toml" => "⚙️",

            // Documentos
            ".txt" => "📄",
            ".md" => "📝",
            ".doc" => "📘",
            ".docx" => "📘",
            ".pdf" => "📕",
            ".xlsx" => "📊",
            ".csv" => "📊",

            // Archivos comprimidos y ejecutables
            ".zip" => "📦",
            ".rar" => "📦",
            ".7z" => "📦",
            ".jar" => "📦",
            ".war" => "📦",
            ".class" => "⚙️",
            ".exe" => "⚙️",
            ".dll" => "⚙️",
            ".so" => "⚙️",

            // Imágenes
            ".jpg" => "🖼️",
            ".jpeg" => "🖼️",
            ".png" => "🖼️",
            ".gif" => "🖼️",
            ".bmp" => "🖼️",
            ".svg" => "🖼️",

            // Audio y video
            ".mp3" => "🎵",
            ".mp4" => "🎬",
            ".avi" => "🎬",
            ".mov" => "🎬",
            ".wav" => "🎵",
            ".flv" => "🎬",

            // Otros
            ".sql" => "🗄️",
            ".db" => "🗄️",
            ".log" => "📋",
            ".sh" => "🖥️",
            ".bat" => "🖥️",
            ".gradle" => "🔨",
            ".maven" => "🔨",
            ".pom" => "🔨",
            ".git" => "🌳",
            ".gitignore" => "🌳",

            // Por defecto
            _ => "📄"
        };
    }

    private void OnTreeItemContextMenuOpening(TreeViewItem treeItem, ContextMenuEventArgs e)
    {
        if (treeItem?.Tag is not string itemPath)
        {
            e.Handled = true;
            return;
        }

        var contextMenu = treeItem.ContextMenu;
        contextMenu.Items.Clear();

        bool isDirectory = Directory.Exists(itemPath);

        if (isDirectory)
        {
            // Opción: Crear nuevo archivo
            var createFileItem = new MenuItem { Header = "Nuevo Archivo..." };
            createFileItem.Click += (s, e) => CreateNewFile(treeItem, itemPath);
            contextMenu.Items.Add(createFileItem);

            // Opción: Crear nueva carpeta
            var createFolderItem = new MenuItem { Header = "Nueva Carpeta..." };
            createFolderItem.Click += (s, e) => CreateNewFolder(treeItem, itemPath);
            contextMenu.Items.Add(createFolderItem);

            contextMenu.Items.Add(new Separator());
        }

        // Opción: Renombrar (para archivos y carpetas)
        var renameItem = new MenuItem { Header = "Renombrar" };
        renameItem.Click += (s, e) => RenameItem(treeItem, itemPath);
        contextMenu.Items.Add(renameItem);

        // Opción: Eliminar
        var deleteItem = new MenuItem { Header = "Eliminar", Foreground = System.Windows.Media.Brushes.Red };
        deleteItem.Click += (s, e) => DeleteItem(treeItem, itemPath);
        contextMenu.Items.Add(deleteItem);
    }

    private void CreateNewFile(TreeViewItem parentItem, string parentPath)
    {
        string fileName = InputDialog.Show("Nombre del archivo:", "Crear Archivo", "archivo.java");
        
        if (string.IsNullOrWhiteSpace(fileName))
            return;

        try
        {
            string fullPath = Path.Combine(parentPath, fileName);

            if (File.Exists(fullPath) || Directory.Exists(fullPath))
            {
                MessageBox.Show("El archivo o carpeta ya existe.", "Error", MessageBoxButton.OK, MessageBoxImage.Warning);
                return;
            }

            // Crear archivo vacío
            File.Create(fullPath).Dispose();
            
            // Actualizar árbol
            var newItem = CreateTreeItem(new FileInfo(fullPath));
            parentItem.Items.Add(newItem);

            MessageBox.Show($"Archivo '{fileName}' creado correctamente.", "Éxito", MessageBoxButton.OK, MessageBoxImage.Information);
        }
        catch (Exception ex)
        {
            MessageBox.Show($"Error al crear archivo: {ex.Message}", "Error", MessageBoxButton.OK, MessageBoxImage.Error);
        }
    }

    private void CreateNewFolder(TreeViewItem parentItem, string parentPath)
    {
        string folderName = InputDialog.Show("Nombre de la carpeta:", "Crear Carpeta", "NuevaCarpeta");
        
        if (string.IsNullOrWhiteSpace(folderName))
            return;

        try
        {
            string fullPath = Path.Combine(parentPath, folderName);

            if (File.Exists(fullPath) || Directory.Exists(fullPath))
            {
                MessageBox.Show("La carpeta ya existe.", "Error", MessageBoxButton.OK, MessageBoxImage.Warning);
                return;
            }

            // Crear directorio
            Directory.CreateDirectory(fullPath);
            
            // Actualizar árbol
            var newItem = CreateTreeItem(new DirectoryInfo(fullPath));
            parentItem.Items.Add(newItem);
            newItem.IsExpanded = true;

            MessageBox.Show($"Carpeta '{folderName}' creada correctamente.", "Éxito", MessageBoxButton.OK, MessageBoxImage.Information);
        }
        catch (Exception ex)
        {
            MessageBox.Show($"Error al crear carpeta: {ex.Message}", "Error", MessageBoxButton.OK, MessageBoxImage.Error);
        }
    }

    private void RenameItem(TreeViewItem treeItem, string itemPath)
    {
        string currentName = Path.GetFileName(itemPath);
        string newName = InputDialog.Show("Nuevo nombre:", "Renombrar", currentName);
        
        if (string.IsNullOrWhiteSpace(newName) || newName == currentName)
            return;

        try
        {
            string parentPath = Path.GetDirectoryName(itemPath);
            string newPath = Path.Combine(parentPath, newName);

            if (File.Exists(newPath) || Directory.Exists(newPath))
            {
                MessageBox.Show("Ya existe un archivo o carpeta con ese nombre.", "Error", MessageBoxButton.OK, MessageBoxImage.Warning);
                return;
            }

            bool isDirectory = Directory.Exists(itemPath);

            if (isDirectory)
            {
                Directory.Move(itemPath, newPath);
            }
            else
            {
                File.Move(itemPath, newPath);
            }

            // Actualizar el árbol con icono
            string icon = isDirectory ? "📁" : GetFileIcon(Path.GetExtension(newName));
            var headerPanel = CreateHeaderWithIcon(icon, newName, isBold: isDirectory);
            treeItem.Header = headerPanel;
            treeItem.Tag = newPath;

            MessageBox.Show($"'{currentName}' renombrado a '{newName}' correctamente.", "Éxito", MessageBoxButton.OK, MessageBoxImage.Information);
        }
        catch (Exception ex)
        {
            MessageBox.Show($"Error al renombrar: {ex.Message}", "Error", MessageBoxButton.OK, MessageBoxImage.Error);
        }
    }

    private void DeleteItem(TreeViewItem treeItem, string itemPath)
    {
        string itemName = Path.GetFileName(itemPath);
        bool isDirectory = Directory.Exists(itemPath);

        var result = MessageBox.Show(
            $"¿Estás seguro de que deseas eliminar '{itemName}'?{(isDirectory ? " Esta acción eliminará todo su contenido." : "")}",
            "Confirmar Eliminación",
            MessageBoxButton.YesNo,
            MessageBoxImage.Warning);

        if (result != MessageBoxResult.Yes)
            return;

        try
        {
            if (isDirectory)
            {
                Directory.Delete(itemPath, true); // true = recursivo
            }
            else
            {
                File.Delete(itemPath);
            }

            // Remover del árbol
            if (treeItem.Parent is ItemsControl parent)
            {
                parent.Items.Remove(treeItem);
            }

            MessageBox.Show($"'{itemName}' eliminado correctamente.", "Éxito", MessageBoxButton.OK, MessageBoxImage.Information);
        }
        catch (Exception ex)
        {
            MessageBox.Show($"Error al eliminar: {ex.Message}", "Error", MessageBoxButton.OK, MessageBoxImage.Error);
        }
    }

    private void FileExplorer_SelectedItemChanged(object sender, RoutedPropertyChangedEventArgs<object> e)
    {
        if (e.NewValue is TreeViewItem item && item.Tag is string path)
        {
            if (File.Exists(path))
            {
                FileSelected?.Invoke(path);
            }
        }
    }
}

/// <summary>
/// Diálogo de entrada para nombres de archivos y carpetas.
/// Implementa las mejores prácticas de UX.
/// </summary>
public static class InputDialog
{
    public static string Show(string prompt, string title, string defaultValue = "")
    {
        var window = new Window
        {
            Width = 450,
            Height = 200,
            Title = title,
            WindowStartupLocation = WindowStartupLocation.CenterScreen,
            ResizeMode = ResizeMode.NoResize,
            Background = new System.Windows.Media.SolidColorBrush(System.Windows.Media.Color.FromRgb(30, 30, 30)),
            Foreground = System.Windows.Media.Brushes.White,
            WindowStyle = WindowStyle.SingleBorderWindow
        };

        var mainPanel = new StackPanel { Margin = new Thickness(20) };

        // Etiqueta del prompt
        var label = new TextBlock
        {
            Text = prompt,
            Margin = new Thickness(0, 0, 0, 10),
            Foreground = System.Windows.Media.Brushes.LightGray,
            FontSize = 12
        };
        mainPanel.Children.Add(label);

        // Campo de entrada
        var textBox = new TextBox
        {
            Text = defaultValue,
            Padding = new Thickness(8),
            Background = new System.Windows.Media.SolidColorBrush(System.Windows.Media.Color.FromRgb(45, 45, 48)),
            Foreground = System.Windows.Media.Brushes.White,
            BorderThickness = new Thickness(1),
            BorderBrush = new System.Windows.Media.SolidColorBrush(System.Windows.Media.Color.FromRgb(60, 60, 60)),
            CaretBrush = System.Windows.Media.Brushes.White,
            FontFamily = new System.Windows.Media.FontFamily("Consolas"),
            Height = 32
        };
        mainPanel.Children.Add(textBox);

        // Panel de botones
        var buttonPanel = new StackPanel
        {
            Orientation = Orientation.Horizontal,
            Margin = new Thickness(0, 20, 0, 0),
            HorizontalAlignment = HorizontalAlignment.Right
        };

        // Botón Aceptar
        var btnOk = new Button
        {
            Content = "Aceptar",
            IsDefault = true,
            Width = 80,
            Height = 32,
            Margin = new Thickness(0, 0, 10, 0),
            Background = new System.Windows.Media.SolidColorBrush(System.Windows.Media.Color.FromRgb(0, 122, 204)),
            Foreground = System.Windows.Media.Brushes.White,
            BorderThickness = new Thickness(0),
            Cursor = System.Windows.Input.Cursors.Hand
        };
        btnOk.Click += (s, e) =>
        {
            window.DialogResult = true;
            window.Close();
        };
        buttonPanel.Children.Add(btnOk);

        // Botón Cancelar
        var btnCancel = new Button
        {
            Content = "Cancelar",
            IsCancel = true,
            Width = 80,
            Height = 32,
            Background = new System.Windows.Media.SolidColorBrush(System.Windows.Media.Color.FromRgb(60, 60, 60)),
            Foreground = System.Windows.Media.Brushes.White,
            BorderThickness = new Thickness(0),
            Cursor = System.Windows.Input.Cursors.Hand
        };
        btnCancel.Click += (s, e) =>
        {
            window.DialogResult = false;
            window.Close();
        };
        buttonPanel.Children.Add(btnCancel);

        mainPanel.Children.Add(buttonPanel);
        window.Content = mainPanel;

        // Seleccionar todo el texto por defecto
        textBox.Focus();
        textBox.SelectAll();

        return window.ShowDialog() == true ? textBox.Text : "";
    }
}



