using System.ComponentModel;
using System.Runtime.CompilerServices;

namespace Editor1.Models
{
    public class EditorTab : INotifyPropertyChanged
    {
        private string _fileName = "";
        private string _filePath = "";
        private string _content = "";
        private bool _isModified;

        public string FileName
        {
            get => _fileName;
            set { _fileName = value; OnPropertyChanged(); OnPropertyChanged(nameof(Header)); }
        }

        public string FilePath
        {
            get => _filePath;
            set { _filePath = value; OnPropertyChanged(); }
        }

        public string Content
        {
            get => _content;
            set
            {
                if (_content != value)
                {
                    _content = value;
                    OnPropertyChanged();
                }
            }
        }

        public bool IsModified
        {
            get => _isModified;
            set { _isModified = value; OnPropertyChanged(); OnPropertyChanged(nameof(Header)); }
        }

        public string Header => IsModified ? $"● {FileName}" : FileName;

        public event PropertyChangedEventHandler? PropertyChanged;

        protected void OnPropertyChanged([CallerMemberName] string? name = null)
        {
            PropertyChanged?.Invoke(this, new PropertyChangedEventArgs(name));
        }
    }
}
