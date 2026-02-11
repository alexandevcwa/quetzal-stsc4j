using System.Collections.ObjectModel;
using System.ComponentModel;
using System.IO;
using System.Runtime.CompilerServices;

namespace Editor1.Models
{
    public class FileNode : INotifyPropertyChanged
    {
        private string _name = "";
        private string _fullPath = "";
        private bool _isDirectory;
        private bool _isExpanded;
        private bool _isEditing;
        private string _editName = "";

        public string Name
        {
            get => _name;
            set { _name = value; OnPropertyChanged(); }
        }

        public string FullPath
        {
            get => _fullPath;
            set { _fullPath = value; OnPropertyChanged(); }
        }

        public bool IsDirectory
        {
            get => _isDirectory;
            set { _isDirectory = value; OnPropertyChanged(); OnPropertyChanged(nameof(Icon)); }
        }

        public bool IsExpanded
        {
            get => _isExpanded;
            set { _isExpanded = value; OnPropertyChanged(); OnPropertyChanged(nameof(Icon)); }
        }

        public bool IsEditing
        {
            get => _isEditing;
            set { _isEditing = value; OnPropertyChanged(); }
        }

        public string EditName
        {
            get => _editName;
            set { _editName = value; OnPropertyChanged(); }
        }

        public string Icon
        {
            get
            {
                if (IsDirectory)
                    return IsExpanded ? "📂" : "📁";

                var ext = Path.GetExtension(Name).ToLowerInvariant();
                return ext switch
                {
                    ".qz" => "🔷",
                    ".json" => "📋",
                    _ => "📄"
                };
            }
        }

        public ObservableCollection<FileNode> Children { get; set; } = [];

        public event PropertyChangedEventHandler? PropertyChanged;

        protected void OnPropertyChanged([CallerMemberName] string? name = null)
        {
            PropertyChanged?.Invoke(this, new PropertyChangedEventArgs(name));
        }
    }
}
