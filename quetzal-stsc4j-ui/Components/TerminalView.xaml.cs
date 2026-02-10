using System;
using System.Diagnostics;
using System.IO;
using System.Windows;
using System.Windows.Controls;

namespace quetzal_stsc4j_ui.Components;

public partial class TerminalView : UserControl
{
    private Process _process;
    private string _currentDirectory = Directory.GetCurrentDirectory();

    public TerminalView()
    {
        InitializeComponent();
        InitializeTerminal();
    }

    private void InitializeTerminal()
    {
        AppendOutput($"Directorio: {_currentDirectory}\n");
        AppendOutput("Escribe 'cd <ruta>' para cambiar de directorio\n");
        AppendOutput("Escribe 'dir' o 'ls' para listar archivos\n");
        AppendOutput("Escribe 'exit' para salir\n\n");
    }

    private void TerminalInput_KeyDown(object sender, System.Windows.Input.KeyEventArgs e)
    {
        if (e.Key == System.Windows.Input.Key.Return)
        {
            string command = TerminalInput.Text.Trim();
            ExecuteCommand(command);
            TerminalInput.Clear();
            e.Handled = true;
        }
    }

    private void ExecuteCommand(string command)
    {
        if (string.IsNullOrEmpty(command))
            return;

        AppendOutput($"PS {_currentDirectory}> {command}\n");

        if (command.Equals("exit", StringComparison.OrdinalIgnoreCase))
        {
            AppendOutput("Terminal cerrada.\n");
            return;
        }

        if (command.StartsWith("cd ", StringComparison.OrdinalIgnoreCase))
        {
            string path = command.Substring(3).Trim();
            ChangeDirectory(path);
            return;
        }

        if (command.Equals("dir", StringComparison.OrdinalIgnoreCase) || 
            command.Equals("ls", StringComparison.OrdinalIgnoreCase))
        {
            ListDirectory();
            return;
        }

        // Para otros comandos, intentar ejecutar en PowerShell
        try
        {
            _process = new Process
            {
                StartInfo = new ProcessStartInfo
                {
                    FileName = "powershell.exe",
                    Arguments = $"-NoProfile -Command \"{command}\"",
                    UseShellExecute = false,
                    RedirectStandardOutput = true,
                    RedirectStandardError = true,
                    CreateNoWindow = true,
                    WorkingDirectory = _currentDirectory
                }
            };

            _process.OutputDataReceived += (s, e) =>
            {
                if (!string.IsNullOrEmpty(e.Data))
                {
                    AppendOutput(e.Data + "\n");
                }
            };

            _process.ErrorDataReceived += (s, e) =>
            {
                if (!string.IsNullOrEmpty(e.Data))
                {
                    AppendOutput(e.Data + "\n");
                }
            };

            _process.Start();
            _process.BeginOutputReadLine();
            _process.BeginErrorReadLine();
            _process.WaitForExit();
        }
        catch (Exception ex)
        {
            AppendOutput($"Error: {ex.Message}\n");
        }
    }

    private void ChangeDirectory(string path)
    {
        try
        {
            string fullPath = Path.GetFullPath(Path.Combine(_currentDirectory, path));
            if (Directory.Exists(fullPath))
            {
                _currentDirectory = fullPath;
                AppendOutput($"Directorio cambiado a: {_currentDirectory}\n");
            }
            else
            {
                AppendOutput($"Error: El directorio '{path}' no existe.\n");
            }
        }
        catch (Exception ex)
        {
            AppendOutput($"Error: {ex.Message}\n");
        }
    }

    private void ListDirectory()
    {
        try
        {
            var info = new DirectoryInfo(_currentDirectory);
            AppendOutput("\n--- Directorios ---\n");
            foreach (var dir in info.GetDirectories())
            {
                AppendOutput($"[DIR] {dir.Name}\n");
            }
            AppendOutput("\n--- Archivos ---\n");
            foreach (var file in info.GetFiles())
            {
                AppendOutput($"{file.Name}\n");
            }
            AppendOutput("\n");
        }
        catch (Exception ex)
        {
            AppendOutput($"Error: {ex.Message}\n");
        }
    }

    private void AppendOutput(string text)
    {
        TerminalOutput.Dispatcher.Invoke(() =>
        {
            TerminalOutput.AppendText(text);
            TerminalOutput.ScrollToEnd();
        });
    }

    public void ExecuteCompilerCommand(string filePath)
    {
        ExecuteCommand($"quetzal-stsc4j compile \"{filePath}\"");
    }

    public void ExecutePackageCommand(string projectPath)
    {
        ExecuteCommand($"quetzal-stsc4j package \"{projectPath}\"");
    }

    public void ExecuteConvertCommand(string filePath)
    {
        ExecuteCommand($"quetzal-stsc4j convert \"{filePath}\"");
    }

    public void ExecuteRunCommand(string className)
    {
        ExecuteCommand($"java {className}");
    }
}
