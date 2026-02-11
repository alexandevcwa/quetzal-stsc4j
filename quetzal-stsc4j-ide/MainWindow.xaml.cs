using System.Collections.ObjectModel;
using System.Diagnostics;
using System.IO;
using System.Text;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Input;
using Editor1.Models;

namespace Editor1
{
    public partial class MainWindow : Window
    {
        private string? _projectPath;
        private ObservableCollection<FileNode> _fileTree = [];
        private ObservableCollection<EditorTab> _openTabs = [];
        private FileNode? _selectedNode;

        public MainWindow()
        {
            InitializeComponent();
            FileTree.ItemsSource = _fileTree;
        }

        // ───────── Open Project ─────────
        private void BtnOpenProject_Click(object sender, RoutedEventArgs e)
        {
            var dialog = new System.Windows.Forms.FolderBrowserDialog
            {
                Description = "Seleccione el directorio del proyecto",
                UseDescriptionForTitle = true
            };

            if (dialog.ShowDialog() == System.Windows.Forms.DialogResult.OK)
            {
                _projectPath = dialog.SelectedPath;
                StatusProjectPath.Text = _projectPath;
                LoadFileTree(_projectPath);
                AppendOutput($"Proyecto abierto: {_projectPath}");
            }
        }

        private void LoadFileTree(string rootPath)
        {
            _fileTree.Clear();
            var rootNode = CreateNode(rootPath);
            foreach (var child in rootNode.Children)
                _fileTree.Add(child);

            NoProjectLabel.Visibility = _fileTree.Count > 0 ? Visibility.Collapsed : Visibility.Visible;
        }

        private FileNode CreateNode(string path)
        {
            var node = new FileNode
            {
                Name = Path.GetFileName(path),
                FullPath = path,
                IsDirectory = Directory.Exists(path)
            };

            if (node.IsDirectory)
            {
                try
                {
                    foreach (var dir in Directory.GetDirectories(path).OrderBy(d => d))
                        node.Children.Add(CreateNode(dir));

                    foreach (var file in Directory.GetFiles(path).OrderBy(f => f))
                        node.Children.Add(CreateNode(file));
                }
                catch (UnauthorizedAccessException) { }
            }

            return node;
        }

        // ───────── File Explorer Events ─────────
        private void FileTree_SelectedItemChanged(object sender, RoutedPropertyChangedEventArgs<object> e)
        {
            _selectedNode = e.NewValue as FileNode;
        }

        private void FileTree_MouseDoubleClick(object sender, MouseButtonEventArgs e)
        {
            if (_selectedNode is null || _selectedNode.IsDirectory)
                return;

            OpenFileInTab(_selectedNode.FullPath);
        }

        // ───────── New File ─────────
        private void BtnNewFile_Click(object sender, RoutedEventArgs e)
        {
            if (_projectPath is null)
            {
                MessageBox.Show("Abra un proyecto primero.", "Sin proyecto", MessageBoxButton.OK, MessageBoxImage.Warning);
                return;
            }

            var parentDir = GetSelectedDirectory();

            var dialog = new InputDialog("Nuevo Archivo", "Nombre del archivo (ej: main.qz):");
            if (dialog.ShowDialog() == true && !string.IsNullOrWhiteSpace(dialog.ResponseText))
            {
                var name = dialog.ResponseText.Trim();
                var ext = Path.GetExtension(name).ToLowerInvariant();
                if (ext is not ".qz" and not ".json")
                {
                    MessageBox.Show("Solo se permiten archivos .qz o .json", "Extensión inválida",
                        MessageBoxButton.OK, MessageBoxImage.Warning);
                    return;
                }

                var fullPath = Path.Combine(parentDir, name);
                if (!File.Exists(fullPath))
                {
                    File.WriteAllText(fullPath, "", Encoding.UTF8);
                    AppendOutput($"Archivo creado: {fullPath}");
                }

                LoadFileTree(_projectPath!);
                OpenFileInTab(fullPath);
            }
        }

        // ───────── New Folder ─────────
        private void BtnNewFolder_Click(object sender, RoutedEventArgs e)
        {
            if (_projectPath is null)
            {
                MessageBox.Show("Abra un proyecto primero.", "Sin proyecto", MessageBoxButton.OK, MessageBoxImage.Warning);
                return;
            }

            var parentDir = GetSelectedDirectory();

            var dialog = new InputDialog("Nueva Carpeta", "Nombre de la carpeta:");
            if (dialog.ShowDialog() == true && !string.IsNullOrWhiteSpace(dialog.ResponseText))
            {
                var dirPath = Path.Combine(parentDir, dialog.ResponseText.Trim());
                if (!Directory.Exists(dirPath))
                {
                    Directory.CreateDirectory(dirPath);
                    AppendOutput($"Carpeta creada: {dirPath}");
                }
                LoadFileTree(_projectPath!);
            }
        }

        // ───────── Rename ─────────
        private void BtnRename_Click(object sender, RoutedEventArgs e)
        {
            if (_selectedNode is null) return;

            _selectedNode.EditName = _selectedNode.Name;
            _selectedNode.IsEditing = true;
        }

        private void RenameBox_KeyDown(object sender, KeyEventArgs e)
        {
            if (e.Key == Key.Enter)
                CommitRename(sender);
            else if (e.Key == Key.Escape)
                CancelRename(sender);
        }

        private void RenameBox_LostFocus(object sender, RoutedEventArgs e)
        {
            CommitRename(sender);
        }

        private void CommitRename(object sender)
        {
            if (sender is not TextBox tb || tb.DataContext is not FileNode node || !node.IsEditing)
                return;

            node.IsEditing = false;
            var newName = node.EditName.Trim();
            if (string.IsNullOrEmpty(newName) || newName == node.Name)
                return;

            try
            {
                var parentDir = Path.GetDirectoryName(node.FullPath)!;
                var newPath = Path.Combine(parentDir, newName);

                if (node.IsDirectory)
                    Directory.Move(node.FullPath, newPath);
                else
                    File.Move(node.FullPath, newPath);

                // Update any open tab
                var tab = _openTabs.FirstOrDefault(t => t.FilePath == node.FullPath);
                if (tab is not null)
                {
                    tab.FilePath = newPath;
                    tab.FileName = newName;
                }

                AppendOutput($"Renombrado: {node.Name} → {newName}");
                LoadFileTree(_projectPath!);
            }
            catch (Exception ex)
            {
                MessageBox.Show($"Error al renombrar: {ex.Message}", "Error", MessageBoxButton.OK, MessageBoxImage.Error);
            }
        }

        private void CancelRename(object sender)
        {
            if (sender is TextBox tb && tb.DataContext is FileNode node)
                node.IsEditing = false;
        }

        // ───────── Delete ─────────
        private void BtnDelete_Click(object sender, RoutedEventArgs e)
        {
            if (_selectedNode is null) return;

            var type = _selectedNode.IsDirectory ? "carpeta" : "archivo";
            var result = MessageBox.Show(
                $"¿Está seguro de eliminar el {type} '{_selectedNode.Name}'?",
                "Confirmar eliminación", MessageBoxButton.YesNo, MessageBoxImage.Warning);

            if (result != MessageBoxResult.Yes) return;

            try
            {
                if (_selectedNode.IsDirectory)
                    Directory.Delete(_selectedNode.FullPath, true);
                else
                    File.Delete(_selectedNode.FullPath);

                // Close tab if open
                var tab = _openTabs.FirstOrDefault(t => t.FilePath == _selectedNode.FullPath);
                if (tab is not null)
                    CloseTabInternal(tab);

                AppendOutput($"Eliminado: {_selectedNode.FullPath}");
                LoadFileTree(_projectPath!);
            }
            catch (Exception ex)
            {
                MessageBox.Show($"Error al eliminar: {ex.Message}", "Error", MessageBoxButton.OK, MessageBoxImage.Error);
            }
        }

        // ───────── Tab Management ─────────
        private void OpenFileInTab(string filePath)
        {
            // Check if already open
            var existing = _openTabs.FirstOrDefault(t => t.FilePath == filePath);
            if (existing is not null)
            {
                EditorTabs.SelectedItem = FindTabItem(existing);
                return;
            }

            try
            {
                var content = File.ReadAllText(filePath, Encoding.UTF8);
                var tab = new EditorTab
                {
                    FileName = Path.GetFileName(filePath),
                    FilePath = filePath,
                    Content = content
                };
                _openTabs.Add(tab);

                var editor = new TextBox
                {
                    Text = content,
                    AcceptsReturn = true,
                    AcceptsTab = true,
                    TextWrapping = TextWrapping.NoWrap,
                    VerticalScrollBarVisibility = ScrollBarVisibility.Auto,
                    HorizontalScrollBarVisibility = ScrollBarVisibility.Auto,
                    Background = new System.Windows.Media.SolidColorBrush(
                        (System.Windows.Media.Color)System.Windows.Media.ColorConverter.ConvertFromString("#1E1E2E")),
                    Foreground = new System.Windows.Media.SolidColorBrush(
                        (System.Windows.Media.Color)System.Windows.Media.ColorConverter.ConvertFromString("#CCCCDD")),
                    FontFamily = new System.Windows.Media.FontFamily("Cascadia Mono, Consolas, Courier New"),
                    FontSize = 14,
                    BorderThickness = new Thickness(0),
                    Padding = new Thickness(12, 8, 12, 8),
                    Tag = tab
                };

                editor.TextChanged += Editor_TextChanged;

                var tabItem = new TabItem
                {
                    DataContext = tab,
                    Content = editor
                };

                EditorTabs.Items.Add(tabItem);
                EditorTabs.SelectedItem = tabItem;

                WelcomePanel.Visibility = Visibility.Collapsed;
                StatusText.Text = $"Abierto: {tab.FileName}";
            }
            catch (Exception ex)
            {
                MessageBox.Show($"Error al abrir archivo: {ex.Message}", "Error", MessageBoxButton.OK, MessageBoxImage.Error);
            }
        }

        private void Editor_TextChanged(object sender, TextChangedEventArgs e)
        {
            if (sender is TextBox editor && editor.Tag is EditorTab tab)
            {
                tab.Content = editor.Text;
                tab.IsModified = true;
            }
        }

        private void CloseTab_Click(object sender, RoutedEventArgs e)
        {
            if (sender is Button btn && btn.Tag is EditorTab tab)
            {
                if (tab.IsModified)
                {
                    var result = MessageBox.Show(
                        $"¿Guardar cambios en '{tab.FileName}'?",
                        "Guardar", MessageBoxButton.YesNoCancel, MessageBoxImage.Question);

                    if (result == MessageBoxResult.Cancel) return;
                    if (result == MessageBoxResult.Yes)
                        SaveTab(tab);
                }

                CloseTabInternal(tab);
            }
        }

        private void CloseTabInternal(EditorTab tab)
        {
            var tabItem = FindTabItem(tab);
            if (tabItem is not null)
                EditorTabs.Items.Remove(tabItem);

            _openTabs.Remove(tab);

            if (EditorTabs.Items.Count == 0)
                WelcomePanel.Visibility = Visibility.Visible;
        }

        private TabItem? FindTabItem(EditorTab tab)
        {
            foreach (TabItem item in EditorTabs.Items)
            {
                if (item.DataContext == tab)
                    return item;
            }
            return null;
        }

        private void EditorTabs_SelectionChanged(object sender, SelectionChangedEventArgs e)
        {
            if (EditorTabs.SelectedItem is TabItem ti && ti.DataContext is EditorTab tab)
                StatusText.Text = tab.FilePath;
        }

        // ───────── Save ─────────
        private void BtnSave_Click(object sender, RoutedEventArgs e)
        {
            if (EditorTabs.SelectedItem is TabItem ti && ti.DataContext is EditorTab tab)
            {
                SaveTab(tab);
                AppendOutput($"Guardado: {tab.FilePath}");
            }
        }

        private void SaveTab(EditorTab tab)
        {
            try
            {
                File.WriteAllText(tab.FilePath, tab.Content, Encoding.UTF8);
                tab.IsModified = false;
                StatusText.Text = $"Guardado: {tab.FileName}";
            }
            catch (Exception ex)
            {
                MessageBox.Show($"Error al guardar: {ex.Message}", "Error", MessageBoxButton.OK, MessageBoxImage.Error);
            }
        }

        // ───────── Toolbar Actions (stubs) ─────────
        private void BtnRun_Click(object sender, RoutedEventArgs e)
        {
            AppendOutput("▶ Acción 'Correr' — pendiente de implementar.");
        }

        private void BtnCompile_Click(object sender, RoutedEventArgs e)
        {
            if (_projectPath is null)
            {
                AppendOutput("⚠ No hay proyecto abierto para compilar.");
                return;
            }

            _ = RunCompileAsync();
        }

        private async Task RunCompileAsync()
        {
            AppendOutput("⚙ Iniciando compilación...");
            StatusText.Text = "Compilando...";

            try
            {
                // TODO: Set the correct path to the .jar compiler
                var jarPath = Path.Combine(_projectPath!, "compiler.jar");

                if (!File.Exists(jarPath))
                {
                    AppendOutput($"⚠ No se encontró el compilador en: {jarPath}");
                    AppendOutput("  Coloque el archivo compiler.jar en el directorio del proyecto.");
                    StatusText.Text = "Error de compilación";
                    return;
                }

                var psi = new ProcessStartInfo
                {
                    FileName = "java",
                    Arguments = $"-jar \"{jarPath}\" \"{_projectPath}\"",
                    WorkingDirectory = _projectPath,
                    RedirectStandardOutput = true,
                    RedirectStandardError = true,
                    UseShellExecute = false,
                    CreateNoWindow = true
                };

                using var process = new Process { StartInfo = psi };
                process.Start();

                var output = await process.StandardOutput.ReadToEndAsync();
                var error = await process.StandardError.ReadToEndAsync();

                await process.WaitForExitAsync();

                if (!string.IsNullOrWhiteSpace(output))
                    AppendOutput(output);
                if (!string.IsNullOrWhiteSpace(error))
                    AppendOutput($"⚠ ERRORES:\n{error}");

                AppendOutput($"⚙ Compilación finalizada. Código de salida: {process.ExitCode}");
                StatusText.Text = process.ExitCode == 0 ? "Compilación exitosa" : "Compilación con errores";
            }
            catch (Exception ex)
            {
                AppendOutput($"❌ Error al compilar: {ex.Message}");
                StatusText.Text = "Error de compilación";
            }
        }

        private void BtnTransform_Click(object sender, RoutedEventArgs e)
        {
            AppendOutput("🔄 Acción 'Transformar' — pendiente de implementar.");
        }

        private void BtnGenerateJava_Click(object sender, RoutedEventArgs e)
        {
            AppendOutput("☕ Acción 'Generar Java' — pendiente de implementar.");
        }

        private void BtnPackage_Click(object sender, RoutedEventArgs e)
        {
            AppendOutput("📦 Acción 'Empaquetar' — pendiente de implementar.");
        }

        // ───────── Output Log ─────────
        private void BtnClearOutput_Click(object sender, RoutedEventArgs e)
        {
            OutputLog.Clear();
        }

        private void AppendOutput(string message)
        {
            Dispatcher.Invoke(() =>
            {
                OutputLog.AppendText($"[{DateTime.Now:HH:mm:ss}] {message}\n");
                OutputLog.ScrollToEnd();
            });
        }

        // ───────── Helpers ─────────
        private string GetSelectedDirectory()
        {
            if (_selectedNode is not null)
            {
                if (_selectedNode.IsDirectory)
                    return _selectedNode.FullPath;
                return Path.GetDirectoryName(_selectedNode.FullPath) ?? _projectPath!;
            }
            return _projectPath!;
        }
    }
}