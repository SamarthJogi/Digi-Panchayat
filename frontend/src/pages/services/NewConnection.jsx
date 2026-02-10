import { useState, useEffect } from 'react';
import { useTranslation } from 'react-i18next';
import { useNavigate } from 'react-router-dom';
import { connectionAPI } from '../../services/api';
import logo from '../../assets/logo.png';
import './Services.css';

function NewConnection() {
    const { t } = useTranslation();
    const navigate = useNavigate();
    const [connections, setConnections] = useState([]);
    const [loading, setLoading] = useState(false);
    const [showForm, setShowForm] = useState(false);
    const [formData, setFormData] = useState({
        connectionType: 'WATER',
        applicantName: '',
        address: '',
        phone: '',
        documents: null
    });
    const user = JSON.parse(sessionStorage.getItem('user') || '{}');

    useEffect(() => {
        if (!sessionStorage.getItem('isAuthenticated')) {
            navigate('/login');
            return;
        }
        fetchConnections();
    }, []);

    const fetchConnections = async () => {
        setLoading(true);
        try {
            if (user.id) {
                const response = await connectionAPI.getConsumerRequests(user.id);
                setConnections(response.data || []);
            }
        } catch (error) {
            console.error('Failed to fetch connection requests:', error);
        } finally {
            setLoading(false);
        }
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setLoading(true);
        try {
            const applicationData = {
                consumerId: user.id,
                serviceType: formData.connectionType,
                applicantName: formData.applicantName,
                address: formData.address,
                phone: formData.phone,
                documents: formData.documents
            };

            await connectionAPI.apply(applicationData);
            alert('Connection request submitted successfully!');
            setShowForm(false);
            setFormData({ connectionType: 'WATER', applicantName: '', address: '', phone: '', documents: null });
            fetchConnections();
        } catch (error) {
            alert('Failed to submit request: ' + (error.message || 'Unknown error'));
        } finally {
            setLoading(false);
        }
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
                    <button onClick={() => navigate('/services/complaints')}>📋 {t('services.complaints')}</button>
                    <button onClick={() => navigate('/services/new-connection')} className="active">🔌 {t('services.newConnection')}</button>
                    <button onClick={() => navigate('/dashboard')}>📊 {t('nav.dashboard')}</button>
                    <button onClick={handleLogout} className="btn-logout">{t('nav.logout')}</button>
                </div>
            </nav>

            <div className="service-content">
                <div className="service-header">
                    <h1>🔌 {t('connections.title')}</h1>
                    <p>{t('connections.subtitle')}</p>
                </div>

                <div className="action-buttons">
                    <button
                        onClick={() => setShowForm(!showForm)}
                        className="btn btn-primary"
                    >
                        {showForm ? t('common.cancel') : `+ ${t('connections.applyNew')}`}
                    </button>
                </div>

                {showForm && (
                    <div className="form-section">
                        <h3>{t('connections.formTitle')}</h3>
                        <form onSubmit={handleSubmit} className="connection-form">
                            <div className="form-group">
                                <label>{t('connections.connectionType')}</label>
                                <select
                                    value={formData.connectionType}
                                    onChange={(e) => setFormData({ ...formData, connectionType: e.target.value })}
                                >
                                    <option value="WATER">{t('connections.waterConnection')}</option>
                                    <option value="PROPERTY">{t('connections.propertyConnection')}</option>
                                </select>
                            </div>

                            <div className="form-group">
                                <label>{t('connections.applicantName')}</label>
                                <input
                                    type="text"
                                    value={formData.applicantName}
                                    onChange={(e) => setFormData({ ...formData, applicantName: e.target.value })}
                                    required
                                    placeholder={t('connections.applicantName')}
                                />
                            </div>

                            <div className="form-group">
                                <label>{t('connections.address')}</label>
                                <textarea
                                    value={formData.address}
                                    onChange={(e) => setFormData({ ...formData, address: e.target.value })}
                                    required
                                    rows="3"
                                    placeholder={t('connections.address')}
                                />
                            </div>

                            <div className="form-group">
                                <label>{t('connections.phone')}</label>
                                <input
                                    type="tel"
                                    value={formData.phone}
                                    onChange={(e) => setFormData({ ...formData, phone: e.target.value })}
                                    required
                                    placeholder={t('connections.phone')}
                                />
                            </div>

                            <div className="form-group">
                                <label>{t('connections.uploadDocs')}</label>
                                <input
                                    type="file"
                                    accept=".pdf,.jpg,.jpeg,.png"
                                    onChange={(e) => setFormData({ ...formData, documents: e.target.files[0] })}
                                    required
                                />
                                <small>{t('connections.docHint')}</small>
                            </div>

                            <button type="submit" className="btn btn-primary" disabled={loading}>
                                {loading ? t('connections.submitting') : t('connections.submit')}
                            </button>
                        </form>
                    </div>
                )}

                <div className="connections-section">
                    <h3>{t('connections.yourRequests')}</h3>
                    {loading ? (
                        <p>{t('common.loading')}</p>
                    ) : connections.length === 0 ? (
                        <p className="no-data">{t('connections.noRequests')}</p>
                    ) : (
                        <div className="connections-list">
                            {connections.map((connection) => (
                                <div key={connection.id} className="connection-card">
                                    <div className="connection-header">
                                        <h4>{connection.serviceType} Connection</h4>
                                        <span className={`status ${connection.status.toLowerCase()}`}>
                                            {connection.status}
                                        </span>
                                    </div>
                                    <div className="connection-body">
                                        <p><strong>{t('connections.applicantName')}:</strong> {connection.applicantName}</p>
                                        <p><strong>{t('connections.address')}:</strong> {connection.address}</p>
                                        <p><strong>{t('connections.phone')}:</strong> {connection.phone}</p>
                                        <p><strong>{t('connections.appliedOn')}:</strong> {connection.appliedDate || 'N/A'}</p>
                                        {connection.approvedDate && (
                                            <p><strong>{t('connections.approvedOn')}:</strong> {connection.approvedDate}</p>
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

export default NewConnection;
