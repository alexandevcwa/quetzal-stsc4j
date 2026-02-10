using System;
using System.IO;
using System.Windows;
using System.Windows.Controls;

namespace quetzal_stsc4j_ui.Components;

public partial class AppToolbar : UserControl
{
    // Delegados para eventos
    public event Action<string> CompileRequested;
    public event Action<string> RunRequested;
    public event Action SaveRequested;
    public event Action<string> PackageRequested;
    public event Action<string> ConvertRequested;

    public AppToolbar()
    {
        InitializeComponent();
    }

    private void BtnCompile_Click(object sender, RoutedEventArgs e)
    {
        CompileRequested?.Invoke(GetCurrentFilePath());
    }

    private void BtnRun_Click(object sender, RoutedEventArgs e)
    {
        RunRequested?.Invoke(GetCurrentFilePath());
    }

    private void BtnSave_Click(object sender, RoutedEventArgs e)
    {
        SaveRequested?.Invoke();
    }

    private void BtnPackage_Click(object sender, RoutedEventArgs e)
    {
        PackageRequested?.Invoke(GetCurrentDirectoryPath());
    }

    private void BtnConvert_Click(object sender, RoutedEventArgs e)
    {
        ConvertRequested?.Invoke(GetCurrentFilePath());
    }

    private string GetCurrentFilePath()
    {
        var mainWindow = Window.GetWindow(this) as QuetzalStsc4jUI;
        return mainWindow?.GetCurrentFilePath() ?? "";
    }

    private string GetCurrentDirectoryPath()
    {
        var mainWindow = Window.GetWindow(this) as QuetzalStsc4jUI;
        return mainWindow?.GetCurrentDirectoryPath() ?? Directory.GetCurrentDirectory();
    }
}


