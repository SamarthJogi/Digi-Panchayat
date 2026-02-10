import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { complaintAPI } from '../../services/api';
import FileUpload from '../../components/common/FileUpload';
import './ComplaintForm.css';

function ComplaintForm() {
    const navigate = useNavigate();
    const user = JSON.parse(localStorage.getItem('user') || '{}');
    const [formData, setFormData] = useState({
        type: 'WATER',
        description: '',
        imageData: '',
    });
    const [loading, setLoading] = useState(false);
    const [message, setMessage] = useState('');

    const handleSubmit = async (e) => {
        e.preventDefault();
        setLoading(true);
        setMessage('');

        try {
            const response = await complaintAPI.register({
                consumerId: user.id,
                type: formData.type,
                description: formData.description,
                imageData: formData.imageData,
            });

            setMessage('Complaint registered successfully!');
            setTimeout(() => navigate('/dashboard'), 2000);
        } catch (error) {
            setMessage(error.message || 'Failed to register complaint');
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="page-container">
            <div className="form-card">
                <h1>📋 Register Complaint</h1>

                <form onSubmit={handleSubmit}>
                    <div className="form-group">
                        <label>Complaint Type</label>
                        <select
                            value={formData.type}
                            onChange={(e) => setFormData({ ...formData, type: e.target.value })}
                            required
                        >
                            <option value="WATER">💧 Water Supply</option>
                            <option value="PROPERTY_TAX">🏠 Property Tax</option>
                        </select>
                    </div>

                    <div className="form-group">
                        <label>Description</label>
                        <textarea
                            value={formData.description}
                            onChange={(e) => setFormData({ ...formData, description: e.target.value })}
                            rows="5"
                            required
                            placeholder="Describe your complaint in detail..."
                        />
                    </div>

                    <FileUpload
                        label="Upload Image (Optional)"
                        accept="image/*"
                        maxSize={512000}
                        onFileSelect={(base64) => setFormData({ ...formData, imageData: base64 })}
                        preview={formData.imageData}
                    />

                    <div className="form-actions">
                        <button type="button" onClick={() => navigate('/dashboard')} className="btn btn-secondary">
                            Cancel
                        </button>
                        <button type="submit" className="btn btn-primary" disabled={loading}>
                            {loading ? 'Submitting...' : 'Submit Complaint'}
                        </button>
                    </div>
                </form>

                {message && <p className={`message ${message.includes('success') ? 'success' : 'error'}`}>{message}</p>}
            </div>
        </div>
    );
}

export default ComplaintForm;
