import './FileUpload.css';

function FileUpload({ label, accept = "image/*", maxSize = 512000, onFileSelect, preview }) {
    const handleFileChange = async (e) => {
        const file = e.target.files[0];
        if (!file) return;

        // Validate size
        if (file.size > maxSize) {
            alert(`File size must be less than ${Math.round(maxSize / 1024)}KB`);
            e.target.value = '';
            return;
        }

        // Convert to base64
        const reader = new FileReader();
        reader.onload = () => {
            onFileSelect(reader.result);
        };
        reader.onerror = () => {
            alert('Failed to read file');
        };
        reader.readAsDataURL(file);
    };

    return (
        <div className="file-upload">
            <label className="file-upload-label">{label}</label>
            <input
                type="file"
                accept={accept}
                onChange={handleFileChange}
                className="file-upload-input"
            />
            {preview && (
                <div className="file-preview">
                    <img src={preview} alt="Preview" />
                </div>
            )}
            <p className="file-upload-hint">Max size: {Math.round(maxSize / 1024)}KB</p>
        </div>
    );
}

export default FileUpload;
