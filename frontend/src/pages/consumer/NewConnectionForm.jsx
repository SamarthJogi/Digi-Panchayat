import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { connectionAPI } from '../../services/api';
import FileUpload from '../../components/common/FileUpload';
import './ComplaintForm.css';

function NewConnectionForm() {
    const navigate = useNavigate();
    const user = JSON.parse(localStorage.getItem('user') || '{}');
    const [formData, setFormData] = useState({
        serviceType: 'WATER',
        applicantName: user.name || '',
        aadharDocument: '',
        panDocument: '',
    });
    const [loading, setLoading] = useState(false);
    const [message, setMessage] = useState('');

    const handleSubmit = async (e) => {
        e.preventDefault();

        if (!formData.aadharDocument || !formData.panDocument) {
            setMessage('Please upload both Aadhar and PAN documents');
            return;
        }

        setLoading(true);
        setMessage('');

        try {
            const response = await connectionAPI.apply({
                serviceType: formData.serviceType,
                applicantName: formData.applicantName,
                consumerId: user.id,
                aadharDocument: formData.aadharDocument,
                panDocument: formData.panDocument,
            });

            setMessage('Connection request submitted successfully!');
            setTimeout(() => navigate('/dashboard'), 2000);
        } catch (error) {
            setMessage(error.message || 'Failed to submit request');
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="page-container">
            <div className="form-card">
                <h1>🔌 Apply for New Connection</h1>

                <form onSubmit={handleSubmit}>
                    <div className="form-group">
                        <label>Service Type</label>
                        <select
                            value={formData.serviceType}
                            onChange={(e) => setFormData({ ...formData, serviceType: e.target.value })}
                            required
                        >
                            <option value="WATER">💧 Water Connection</option>
                            <option value="PROPERTY">🏠 Property Connection</option>
                        </select>
                    </div>

                    <div className="form-group">
                        <label>Applicant Name</label>
                        <input
                            type="text"
                            value={formData.applicantName}
                            onChange={(e) => setFormData({ ...formData, applicantName: e.target.value })}
                            required
                        />
                    </div>

                    <FileUpload
                        label="Aadhar Document *"
                        accept="image/*,application/pdf"
                        maxSize={512000}
                        onFileSelect={(base64) => setFormData({ ...formData, aadharDocument: base64 })}
                        preview={formData.aadharDocument?.startsWith('data:image') ? formData.aadharDocument : null}
                    />

                    <FileUpload
                        label="PAN Document *"
                        accept="image/*,application/pdf"
                        maxSize={512000}
                        onFileSelect={(base64) => setFormData({ ...formData, panDocument: base64 })}
                        preview={formData.panDocument?.startsWith('data:image') ? formData.panDocument : null}
                    />

                    <div className="form-actions">
                        <button type="button" onClick={() => navigate('/dashboard')} className="btn btn-secondary">
                            Cancel
                        </button>
                        <button type="submit" className="btn btn-primary" disabled={loading}>
                            {loading ? 'Submitting...' : 'Submit Application'}
                        </button>
                    </div>
                </form>

                {message && <p className={`message ${message.includes('success') ? 'success' : 'error'}`}>{message}</p>}
            </div>
        </div>
    );
}

export default NewConnectionForm;
