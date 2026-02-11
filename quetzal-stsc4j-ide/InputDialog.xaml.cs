using System.Windows;

namespace Editor1
{
    public partial class InputDialog : Window
    {
        public string ResponseText => ResponseTextBox.Text;

        public InputDialog(string title, string prompt)
        {
            InitializeComponent();
            TitleText.Text = title;
            PromptText.Text = prompt;
            ResponseTextBox.Focus();
        }

        private void BtnOk_Click(object sender, RoutedEventArgs e)
        {
            DialogResult = true;
            Close();
        }

        private void BtnCancel_Click(object sender, RoutedEventArgs e)
        {
            DialogResult = false;
            Close();
        }
    }
}
