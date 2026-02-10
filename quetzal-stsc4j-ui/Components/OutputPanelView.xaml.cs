using System.Windows.Controls;

namespace quetzal_stsc4j_ui.Components;

public partial class OutputPanelView : UserControl
{
    private TerminalView _terminalComponent;

    public OutputPanelView()
    {
        InitializeComponent();
        InitializeTerminal();
    }

    private void InitializeTerminal()
    {
        _terminalComponent = new TerminalView();
        TerminalContainer.Children.Add(_terminalComponent);
    }

    public void AppendCompileOutput(string text)
    {
        CompileOutput.AppendText(text + "\n");
        CompileOutput.ScrollToEnd();
    }

    public void ClearCompileOutput()
    {
        CompileOutput.Clear();
    }

    public TerminalView GetTerminal()
    {
        return _terminalComponent;
    }
}


