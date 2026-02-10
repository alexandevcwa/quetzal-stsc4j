using Microsoft.Win32;
using System;
using System.Diagnostics;
using System.IO;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Input;
using System.Windows.Media;
using quetzal_stsc4j_ui.Components;

namespace quetzal_stsc4j_ui;

public partial class QuetzalStsc4jUI : Window
{
    private string _currentFilePath = null;
    private string _currentDirectoryPath = null;

    public QuetzalStsc4jUI()
    {
        InitializeComponent();
        InitializeEventHandlers();
        InitializeToolbar();
        InitializeFileExplorer();
    }

    private void InitializeEventHandlers()
    {
        // Keyboard shortcut for Ctrl+S
        this.KeyDown += (s, e) =>
        {
            if (e.Key == Key.S && Keyboard.Modifiers == ModifierKeys.Control)
            {
                SaveFile();
            }
        };
    }

    private void InitializeToolbar()
    {
        Toolbar.CompileRequested += OnCompileRequested;
        Toolbar.RunRequested += OnRunRequested;
        Toolbar.SaveRequested += SaveFile;
        Toolbar.PackageRequested += OnPackageRequested;
        Toolbar.ConvertRequested += OnConvertRequested;
    }

    private void InitializeFileExplorer()
    {
        // Conectar el evento FileSelected del explorador de archivos
        MainExplorer.FileSelected += (filePath) =>
        {
            _currentFilePath = filePath;
            LoadFileIntoEditor(filePath);
        };
    }

    // ========== TOOLBAR COMMAND HANDLERS ==========
    private void OnCompileRequested(string filePath)
    {
        if (string.IsNullOrEmpty(filePath))
        {
            MessageBox.Show("No hay archivo seleccionado para compilar.", "Advertencia", MessageBoxButton.OK, MessageBoxImage.Warning);
            return;
        }

        OutputPanel.ClearCompileOutput();
        OutputPanel.AppendCompileOutput($"Compilando: {Path.GetFileName(filePath)}");
        OutputPanel.AppendCompileOutput("Iniciando compilación con quetzal-stsc4j...\n");

        try
        {
            var terminal = OutputPanel.GetTerminal();
            terminal.ExecuteCompilerCommand(filePath);
        }
        catch (Exception ex)
        {
            OutputPanel.AppendCompileOutput($"Error: {ex.Message}");
        }
    }

    private void OnRunRequested(string filePath)
    {
        if (string.IsNullOrEmpty(filePath))
        {
            MessageBox.Show("No hay archivo seleccionado para ejecutar.", "Advertencia", MessageBoxButton.OK, MessageBoxImage.Warning);
            return;
        }

        string className = Path.GetFileNameWithoutExtension(filePath);
        var terminal = OutputPanel.GetTerminal();
        terminal.ExecuteRunCommand(className);
    }

    private void OnPackageRequested(string projectPath)
    {
        if (string.IsNullOrEmpty(projectPath))
        {
            projectPath = _currentDirectoryPath ?? Directory.GetCurrentDirectory();
        }

        OutputPanel.ClearCompileOutput();
        OutputPanel.AppendCompileOutput($"Empaquetando: {projectPath}");
        OutputPanel.AppendCompileOutput("Iniciando empaquetado...\n");

        try
        {
            var terminal = OutputPanel.GetTerminal();
            terminal.ExecutePackageCommand(projectPath);
        }
        catch (Exception ex)
        {
            OutputPanel.AppendCompileOutput($"Error: {ex.Message}");
        }
    }

    private void OnConvertRequested(string filePath)
    {
        if (string.IsNullOrEmpty(filePath))
        {
            MessageBox.Show("No hay archivo seleccionado para convertir.", "Advertencia", MessageBoxButton.OK, MessageBoxImage.Warning);
            return;
        }

        OutputPanel.ClearCompileOutput();
        OutputPanel.AppendCompileOutput($"Convirtiendo: {Path.GetFileName(filePath)}");
        OutputPanel.AppendCompileOutput("Iniciando conversión...\n");

        try
        {
            var terminal = OutputPanel.GetTerminal();
            terminal.ExecuteConvertCommand(filePath);
        }
        catch (Exception ex)
        {
            OutputPanel.AppendCompileOutput($"Error: {ex.Message}");
        }
    }

    // ========== FILE OPERATIONS ==========
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
            _currentDirectoryPath = folderPath;
            MainExplorer.LoadDirectory(folderPath);
        }
    }

    private void BtnOpenFile_Click(object sender, RoutedEventArgs e)
    {
        OpenFileDialog openFileDialog = new OpenFileDialog
        {
            Filter = "Java files (*.java)|*.java|All files (*.*)|*.*",
            Title = "Abrir archivo"
        };

        if (openFileDialog.ShowDialog() == true)
        {
            _currentFilePath = openFileDialog.FileName;
            LoadFileIntoEditor(_currentFilePath);
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
            MessageBox.Show("No hay ningún archivo abierto para guardar.", "Advertencia", MessageBoxButton.OK, MessageBoxImage.Warning);
            return;
        }

        try
        {
            File.WriteAllText(_currentFilePath, MainEditor.Text);
            MainEditor.IsModified = false;
            MessageBox.Show("Archivo guardado correctamente.", "Éxito", MessageBoxButton.OK, MessageBoxImage.Information);
        }
        catch (Exception ex)
        {
            MessageBox.Show($"Error al guardar: {ex.Message}", "Error", MessageBoxButton.OK, MessageBoxImage.Error);
        }
    }

    private void LoadFileIntoEditor(string filePath)
    {
        try
        {
            MainEditor.Text = File.ReadAllText(filePath);
            MainEditor.CurrentFilePath = filePath;
            MainEditor.IsModified = false;
            this.Title = $"Quetzal Source to Source Compiler for Java - {Path.GetFileName(filePath)}";
        }
        catch (Exception ex)
        {
            MessageBox.Show($"Error al leer archivo: {ex.Message}", "Error", MessageBoxButton.OK, MessageBoxImage.Error);
        }
    }

    private void MenuItem_Exit_Click(object sender, RoutedEventArgs e)
    {
        this.Close();
    }

    // ========== PUBLIC HELPERS FOR TOOLBAR ==========
    public string GetCurrentFilePath()
    {
        return _currentFilePath;
    }

    public string GetCurrentDirectoryPath()
    {
        return _currentDirectoryPath ?? Directory.GetCurrentDirectory();
    }
}
