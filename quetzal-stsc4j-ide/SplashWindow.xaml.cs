using System.Windows;
using System.Windows.Threading;

namespace Editor1
{
    public partial class SplashWindow : Window
    {
        public SplashWindow()
        {
            InitializeComponent();

            var timer = new DispatcherTimer { Interval = TimeSpan.FromSeconds(3) };
            timer.Tick += (s, e) =>
            {
                timer.Stop();
                var main = new MainWindow();
                main.Show();
                Close();
            };
            timer.Start();
        }
    }
}
