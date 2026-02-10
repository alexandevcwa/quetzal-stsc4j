using System;
using System.IO;
using System.Windows.Controls;

namespace quetzal_stsc4j_ui.Components;

public partial class CodeEditorView : UserControl
{
    private string _currentFilePath;
    private bool _isModified;

    public string CurrentFilePath
    {
        get => _currentFilePath;
        set
        {
            _currentFilePath = value;
            UpdateFileName();
        }
    }

    public bool IsModified
    {
        get => _isModified;
        set
        {
            _isModified = value;
            UpdateFileName();
        }
    }

    public string Text
    {
        get => EditorTexto.Text;
        set => EditorTexto.Text = value;
    }

    public CodeEditorView()
    {
        InitializeComponent();
    }

    private void EditorTexto_TextChanged(object sender, TextChangedEventArgs e)
    {
        IsModified = true;
    }

    private void UpdateFileName()
    {
        string fileName = string.IsNullOrEmpty(_currentFilePath)
            ? "Sin guardar"
            : Path.GetFileName(_currentFilePath);

        if (IsModified && !fileName.EndsWith("*"))
        {
            fileName += " *";
        }

        FileNameRun.Text = fileName;
    }

    public void ClearEditor()
    {
        EditorTexto.Clear();
        _currentFilePath = null;
        IsModified = false;
    }
}

