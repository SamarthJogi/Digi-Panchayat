import { useState, useEffect } from 'react';
import { useTranslation } from 'react-i18next';
import { useNavigate } from 'react-router-dom';
import { complaintAPI } from '../../services/api';
import logo from '../../assets/logo.png';
import './Services.css';

function Complaints() {
    const { t } = useTranslation();
    const navigate = useNavigate();
    const [complaints, setComplaints] = useState([]);
    const [loading, setLoading] = useState(false);
    const [showForm, setShowForm] = useState(false);
    const [formData, setFormData] = useState({
        subject: '',
        description: '',
        category: 'WATER',
        image: null
    });
    const user = JSON.parse(sessionStorage.getItem('user') || '{}');

    useEffect(() => {
        if (!sessionStorage.getItem('isAuthenticated')) {
            navigate('/login');
            return;
        }
        fetchComplaints();
    }, []);

    const fetchComplaints = async () => {
        setLoading(true);
        try {
            if (user.id) {
                const response = await complaintAPI.getConsumerComplaints(user.id);
                setComplaints(response.data || []);
            }
        } catch (error) {
            console.error('Failed to fetch complaints:', error);
        } finally {
            setLoading(false);
        }
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setLoading(true);
        try {
            // Convert image to base64 if present
            let imageData = null;
            if (formData.image) {
                imageData = await convertToBase64(formData.image);
            }

            const complaintData = {
                consumerId: user.id || null, // Use null if user.id is undefined
                type: formData.category,
                description: `${formData.subject}\n\n${formData.description}`,
                imageData: imageData
            };

            await complaintAPI.register(complaintData);
            alert('Complaint registered successfully!');
            setShowForm(false);
            setFormData({ subject: '', description: '', category: 'WATER', image: null });
            fetchComplaints();
        } catch (error) {
            alert('Failed to register complaint: ' + (error.message || 'Unknown error'));
        } finally {
            setLoading(false);
        }
    };

    // Helper function to convert file to base64
    const convertToBase64 = (file) => {
        return new Promise((resolve, reject) => {
            const reader = new FileReader();
            reader.readAsDataURL(file);
            reader.onload = () => resolve(reader.result);
            reader.onerror = (error) => reject(error);
        });
    };

    const handleLogout = () => {
        sessionStorage.clear();
        navigate('/');
    };

    return (
        <div className="service-page">
            <nav className="service-nav">
                <div className="nav-brand" onClick={() => navigate('/')} style={{ display: 'flex', alignItems: 'center', gap: '15px' }}>
                    <img src={logo} alt="Digi Panchayat Logo" style={{ height: '50px' }} />
                    <h2 style={{ color: 'white', margin: 0 }}>{t('common.appTitle')}</h2>
                </div>
                <div className="nav-links">
                    <button onClick={() => navigate('/services/water-bills')}>💧 {t('services.waterBilling')}</button>
                    <button onClick={() => navigate('/services/property-tax')}>🏠 {t('services.propertyTax')}</button>
                    <button onClick={() => navigate('/services/complaints')} className="active">📋 {t('services.complaints')}</button>
                    <button onClick={() => navigate('/services/new-connection')}>🔌 {t('services.newConnection')}</button>
                    <button onClick={() => navigate('/dashboard')}>📊 {t('nav.dashboard')}</button>
                    <button onClick={handleLogout} className="btn-logout">{t('nav.logout')}</button>
                </div>
            </nav>

            <div className="service-content">
                <div className="service-header">
                    <h1>📋 {t('complaints.title')}</h1>
                    <p>{t('complaints.subtitle')}</p>
                </div>

                <div className="action-buttons">
                    <button
                        onClick={() => setShowForm(!showForm)}
                        className="btn btn-primary"
                    >
                        {showForm ? t('complaints.cancel') : `+ ${t('complaints.registerNew')}`}
                    </button>
                </div>

                {showForm && (
                    <div className="form-section">
                        <h3>{t('complaints.formTitle')}</h3>
                        <form onSubmit={handleSubmit} className="complaint-form">
                            <div className="form-group">
                                <label>{t('complaints.category')}</label>
                                <select
                                    value={formData.category}
                                    onChange={(e) => setFormData({ ...formData, category: e.target.value })}
                                >
                                    <option value="WATER">{t('complaints.water')}</option>
                                    <option value="ELECTRICITY">{t('complaints.electricity')}</option>
                                    <option value="ROAD">{t('complaints.road')}</option>
                                    <option value="SANITATION">{t('complaints.sanitation')}</option>
                                    <option value="OTHER">{t('complaints.other')}</option>
                                </select>
                            </div>

                            <div className="form-group">
                                <label>{t('complaints.subject')}</label>
                                <input
                                    type="text"
                                    value={formData.subject}
                                    onChange={(e) => setFormData({ ...formData, subject: e.target.value })}
                                    required
                                    placeholder={t('complaints.subject')}
                                />
                            </div>

                            <div className="form-group">
                                <label>{t('complaints.description')}</label>
                                <textarea
                                    value={formData.description}
                                    onChange={(e) => setFormData({ ...formData, description: e.target.value })}
                                    required
                                    rows="4"
                                    placeholder={t('complaints.description')}
                                />
                            </div>

                            <div className="form-group">
                                <label>{t('complaints.uploadImage')}</label>
                                <input
                                    type="file"
                                    accept="image/*"
                                    onChange={(e) => setFormData({ ...formData, image: e.target.files[0] })}
                                />
                            </div>

                            <button type="submit" className="btn btn-primary" disabled={loading}>
                                {loading ? t('complaints.submitting') : t('complaints.submit')}
                            </button>
                        </form>
                    </div>
                )}

                <div className="complaints-section">
                    <h3>{t('complaints.yourComplaints')}</h3>
                    {loading ? (
                        <p>{t('common.loading')}</p>
                    ) : complaints.length === 0 ? (
                        <p className="no-data">{t('complaints.noComplaints')}</p>
                    ) : (
                        <div className="complaints-list">
                            {complaints.map((complaint) => (
                                <div key={complaint.id} className="complaint-card">
                                    <div className="complaint-header">
                                        <h4>{complaint.subject}</h4>
                                        <span className={`status ${complaint.status.toLowerCase()}`}>
                                            {complaint.status}
                                        </span>
                                    </div>
                                    <div className="complaint-body">
                                        <p><strong>{t('complaints.category')}:</strong> {complaint.category}</p>
                                        <p><strong>{t('complaints.description')}:</strong> {complaint.description}</p>
                                        <p><strong>{t('complaints.registered')}:</strong> {complaint.complaintDate || 'N/A'}</p>
                                        {complaint.resolvedDate && (
                                            <p><strong>{t('complaints.resolved')}:</strong> {complaint.resolvedDate}</p>
                                        )}
                                    </div>
                                </div>
                            ))}
                        </div>
                    )}
                </div>
            </div>
        </div>
    );
}

export default Complaints;
