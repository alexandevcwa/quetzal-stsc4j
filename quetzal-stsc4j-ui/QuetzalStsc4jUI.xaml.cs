using Microsoft.Win32;
using System;
using System.IO;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Input;
using System.Windows.Media;

namespace quetzal_stsc4j_ui;

/// <summary>
/// Interaction logic for QuetzalStsc4jUI.xaml
/// </summary>
public partial class QuetzalStsc4jUI : Window
{
    // Variable para recordar qué archivo estamos editando
    private string _currentFilePath = null;

    public QuetzalStsc4jUI()
    {
        InitializeComponent();

        // Atajo de teclado para Guardar (Ctrl + S)
        this.KeyDown += (s, e) => {
            if (e.Key == Key.S && Keyboard.Modifiers == ModifierKeys.Control)
            {
                SaveFile();
            }
        };
    }

    // ---------------------------------------------------------
    // 1. ABRIR DIRECTORIO Y POBLAR ÁRBOL
    // ---------------------------------------------------------
    private void BtnOpenFolder_Click(object sender, RoutedEventArgs e)
    {
        var dialog = new OpenFileDialog
        {
            ValidateNames = false,
            CheckFileExists = false,
            CheckPathExists = true,
            FileName = "Selecciona esta carpeta",
            Title = "Selecciona la carpeta raíz del proyecto"
        };

        if (dialog.ShowDialog() == true)
        {
            string folderPath = Path.GetDirectoryName(dialog.FileName);
            LoadDirectoryTree(folderPath);
        }
    }

    private void LoadDirectoryTree(string path)
    {
        FileExplorer.Items.Clear();
        var rootDirectoryInfo = new DirectoryInfo(path);

        // Crear el nodo raíz
        var rootItem = CreateTreeItem(rootDirectoryInfo);
        FileExplorer.Items.Add(rootItem);
    }

    // ---------------------------------------------------------
    // 2. CREACIÓN DEL ÁRBOL CON MENÚ CONTEXTUAL (MODIFICADO)
    // ---------------------------------------------------------
    private TreeViewItem CreateTreeItem(object item)
    {
        TreeViewItem treeItem = new TreeViewItem();

        // IMPORTANTE: Evento para seleccionar el ítem con click DERECHO antes de mostrar el menú
        treeItem.PreviewMouseRightButtonDown += (s, e) => {
            TreeViewItem source = s as TreeViewItem;
            if (source != null)
            {
                source.Focus();
                e.Handled = true; // Detenemos la propagación para que no seleccione al padre
            }
        };

        // Definimos el ContextMenu
        ContextMenu contextMenu = new ContextMenu();

        if (item is DirectoryInfo directory)
        {
            treeItem.Header = directory.Name;
            treeItem.Tag = directory.FullName;
            treeItem.FontWeight = FontWeights.Bold;

            // --- OPCIONES SOLO PARA DIRECTORIOS ---
            MenuItem addFileMenu = new MenuItem { Header = "Nuevo Archivo..." };
            addFileMenu.Click += (s, e) => CreateNewFile(treeItem);
            contextMenu.Items.Add(addFileMenu);

            contextMenu.Items.Add(new Separator()); // Línea separadora

            // Cargar hijos recursivamente
            foreach (var dir in directory.GetDirectories())
            {
                treeItem.Items.Add(CreateTreeItem(dir));
            }

            foreach (var file in directory.GetFiles())
            {
                treeItem.Items.Add(CreateTreeItem(file));
            }
        }
        else if (item is FileInfo file)
        {
            treeItem.Header = file.Name;
            treeItem.Tag = file.FullName;
            treeItem.FontWeight = FontWeights.Normal;
        }

        // --- OPCIONES COMUNES (Renombrar y Eliminar) ---
        MenuItem renameMenu = new MenuItem { Header = "Renombrar" };
        renameMenu.Click += (s, e) => RenameItem(treeItem);
        contextMenu.Items.Add(renameMenu);

        MenuItem deleteMenu = new MenuItem { Header = "Eliminar" };
        deleteMenu.Click += (s, e) => DeleteItem(treeItem);
        contextMenu.Items.Add(deleteMenu);

        // Asignar el menú al item
        treeItem.ContextMenu = contextMenu;

        return treeItem;
    }

    // ---------------------------------------------------------
    // 3. LÓGICA DE GESTIÓN DE ARCHIVOS (NUEVO, RENOMBRAR, ELIMINAR)
    // ---------------------------------------------------------

    private void CreateNewFile(TreeViewItem parentFolderItem)
    {
        string folderPath = parentFolderItem.Tag.ToString();
        // Usamos la clase InputBox definida abajo
        string fileName = InputBox.Show("Nombre del archivo (ej: script.java):", "Crear Archivo");

        if (string.IsNullOrWhiteSpace(fileName)) return;

        string fullPath = Path.Combine(folderPath, fileName);

        try
        {
            if (File.Exists(fullPath))
            {
                MessageBox.Show("El archivo ya existe.");
                return;
            }

            // Crear archivo vacío y cerrarlo inmediatamente para liberar el recurso
            File.Create(fullPath).Close();

            // Actualizar UI: Agregar nuevo ítem al árbol visualmente
            var fileInfo = new FileInfo(fullPath);
            parentFolderItem.Items.Add(CreateTreeItem(fileInfo));
            parentFolderItem.IsExpanded = true;
        }
        catch (Exception ex)
        {
            MessageBox.Show("Error al crear archivo: " + ex.Message);
        }
    }

    private void RenameItem(TreeViewItem itemToRename)
    {
        string oldPath = itemToRename.Tag.ToString();
        string oldName = itemToRename.Header.ToString();
        bool isDirectory = Directory.Exists(oldPath);

        string newName = InputBox.Show("Nuevo nombre:", "Renombrar", oldName);
        if (string.IsNullOrWhiteSpace(newName) || newName == oldName) return;

        string parentDir = Path.GetDirectoryName(oldPath);
        string newPath = Path.Combine(parentDir, newName);

        try
        {
            if (isDirectory)
                Directory.Move(oldPath, newPath);
            else
                File.Move(oldPath, newPath);

            // Actualizar UI
            itemToRename.Header = newName;
            itemToRename.Tag = newPath;

            // Si el archivo renombrado es el que está abierto, actualizamos la referencia
            if (_currentFilePath == oldPath)
            {
                _currentFilePath = newPath;
                this.Title = $"Modern Code Editor - {newName}";
            }
        }
        catch (Exception ex)
        {
            MessageBox.Show("Error al renombrar: " + ex.Message);
        }
    }

    private void DeleteItem(TreeViewItem itemToDelete)
    {
        string path = itemToDelete.Tag.ToString();
        bool isDirectory = Directory.Exists(path);

        var result = MessageBox.Show($"¿Estás seguro de eliminar '{itemToDelete.Header}'?",
                                     "Confirmar Eliminación",
                                     MessageBoxButton.YesNo,
                                     MessageBoxImage.Warning);

        if (result == MessageBoxResult.Yes)
        {
            try
            {
                if (isDirectory)
                    Directory.Delete(path, true); // true para recursivo
                else
                    File.Delete(path);

                // Actualizar UI: Eliminar del padre visual
                ItemsControl parent = GetSelectedTreeViewItemParent(itemToDelete);
                if (parent != null)
                {
                    parent.Items.Remove(itemToDelete);
                }

                // Si borramos el archivo abierto, limpiar editor
                if (_currentFilePath == path)
                {
                    EditorTexto.Text = "";
                    _currentFilePath = null;
                    this.Title = "Modern Code Editor";
                }
            }
            catch (Exception ex)
            {
                MessageBox.Show("Error al eliminar: " + ex.Message);
            }
        }
    }

    // Helper para encontrar el padre visual (TreeViewItem o TreeView)
    public ItemsControl GetSelectedTreeViewItemParent(TreeViewItem item)
    {
        DependencyObject parent = VisualTreeHelper.GetParent(item);
        while (!(parent is TreeViewItem || parent is TreeView))
        {
            if (parent == null) return null;
            parent = VisualTreeHelper.GetParent(parent);
        }
        return parent as ItemsControl;
    }

    // ---------------------------------------------------------
    // 4. ABRIR Y GUARDAR (EXISTENTE)
    // ---------------------------------------------------------
    private void FileExplorer_SelectedItemChanged(object sender, RoutedPropertyChangedEventArgs<object> e)
    {
        if (FileExplorer.SelectedItem is TreeViewItem selectedItem)
        {
            string fullPath = selectedItem.Tag as string;

            if (File.Exists(fullPath))
            {
                try
                {
                    EditorTexto.Text = File.ReadAllText(fullPath);
                    _currentFilePath = fullPath;
                    this.Title = $"Modern Code Editor - {selectedItem.Header}";
                }
                catch (Exception ex)
                {
                    MessageBox.Show($"Error al leer archivo: {ex.Message}");
                }
            }
        }
    }

    private void BtnSave_Click(object sender, RoutedEventArgs e)
    {
        SaveFile();
    }

    private void SaveFile()
    {
        if (string.IsNullOrEmpty(_currentFilePath))
        {
            MessageBox.Show("No hay ningún archivo abierto para guardar.");
            return;
        }

        try
        {
            File.WriteAllText(_currentFilePath, EditorTexto.Text);
            MessageBox.Show("Archivo guardado correctamente.", "Éxito", MessageBoxButton.OK, MessageBoxImage.Information);
        }
        catch (Exception ex)
        {
            MessageBox.Show($"Error al guardar: {ex.Message}", "Error", MessageBoxButton.OK, MessageBoxImage.Error);
        }
    }

    private void BtnOpenFile_Click(object sender, RoutedEventArgs e)
    {
        OpenFileDialog openFileDialog = new OpenFileDialog();
        if (openFileDialog.ShowDialog() == true)
        {
            _currentFilePath = openFileDialog.FileName;
            EditorTexto.Text = File.ReadAllText(_currentFilePath);
        }
    }
}

// ---------------------------------------------------------
// CLASE HELPER: INPUT BOX (Ventana emergente para pedir nombres)
// ---------------------------------------------------------
public static class InputBox
{
    public static string Show(string prompt, string title, string defaultText = "")
    {
        Window window = new Window()
        {
            Width = 400,
            Height = 190,
            Title = title,
            WindowStartupLocation = WindowStartupLocation.CenterScreen,
            ResizeMode = ResizeMode.NoResize,
            // Estilo oscuro para que combine
            Background = new SolidColorBrush(Color.FromRgb(30, 30, 30)),
            Foreground = Brushes.White
        };

        StackPanel stack = new StackPanel { Margin = new Thickness(20) };

        stack.Children.Add(new TextBlock
        {
            Text = prompt,
            Margin = new Thickness(0, 0, 0, 10),
            Foreground = Brushes.LightGray
        });

        TextBox textBox = new TextBox
        {
            Text = defaultText,
            Padding = new Thickness(5),
            Background = new SolidColorBrush(Color.FromRgb(45, 45, 48)),
            Foreground = Brushes.White,
            BorderThickness = new Thickness(0),
            CaretBrush = Brushes.White
        };
        stack.Children.Add(textBox);

        Button btnOk = new Button
        {
            Content = "Aceptar",
            IsDefault = true,
            Width = 80,
            Height = 30,
            Margin = new Thickness(0, 20, 0, 0),
            HorizontalAlignment = HorizontalAlignment.Right,
            Background = new SolidColorBrush(Color.FromRgb(0, 122, 204)), // Azul VS
            Foreground = Brushes.White,
            BorderThickness = new Thickness(0)
        };

        btnOk.Click += (s, e) => { window.DialogResult = true; window.Close(); };

        stack.Children.Add(btnOk);
        window.Content = stack;

        textBox.Focus();
        textBox.SelectAll();

        if (window.ShowDialog() == true)
            return textBox.Text;

        return "";
    }
}