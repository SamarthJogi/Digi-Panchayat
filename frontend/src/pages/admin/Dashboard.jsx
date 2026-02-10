import { useState, useEffect } from 'react';
import { adminAPI, connectionAPI, complaintAPI, galleryAPI } from '../../services/api';
import FileUpload from '../../components/common/FileUpload';
import './AdminDashboard.css';

function AdminDashboard() {
    const [activeTab, setActiveTab] = useState('dashboard');
    const [stats, setStats] = useState({});
    const [connections, setConnections] = useState([]);
    const [complaints, setComplaints] = useState([]);
    const [galleryImages, setGalleryImages] = useState([]);
    const [loading, setLoading] = useState(true);
    const [message, setMessage] = useState('');

    // Gallery form
    const [galleryForm, setGalleryForm] = useState({
        title: '',
        description: '',
        imageData: '',
    });

    useEffect(() => {
        fetchDashboardData();
    }, [activeTab]);

    const fetchDashboardData = async () => {
        setLoading(true);
        try {
            if (activeTab === 'dashboard') {
                const response = await adminAPI.getDashboardStats();
                setStats(response.data || {});
            } else if (activeTab === 'connections') {
                const response = await connectionAPI.getAllRequests();
                setConnections(response.data || []);
            } else if (activeTab === 'complaints') {
                const response = await complaintAPI.getAllComplaints();
                setComplaints(response.data || []);
            } else if (activeTab === 'gallery') {
                const response = await galleryAPI.getAll();
                setGalleryImages(response.data || []);
            }
        } catch (error) {
            console.error('Failed to fetch data:', error);
        } finally {
            setLoading(false);
        }
    };

    const handleConnectionStatus = async (id, status) => {
        try {
            await connectionAPI.updateStatus(id, status);
            setMessage(`Connection request ${status.toLowerCase()}`);
            fetchDashboardData();
        } catch (error) {
            setMessage('Failed to update status');
        }
    };

    const handleComplaintStatus = async (id, status) => {
        try {
            await complaintAPI.updateStatus(id, status);
            setMessage(`Complaint marked as ${status.toLowerCase()}`);
            fetchDashboardData();
        } catch (error) {
            setMessage('Failed to update status');
        }
    };

    const handleAddGalleryImage = async (e) => {
        e.preventDefault();
        try {
            await galleryAPI.add(galleryForm);
            setMessage('Image added to gallery successfully');
            setGalleryForm({ title: '', description: '', imageData: '' });
            fetchDashboardData();
        } catch (error) {
            setMessage('Failed to add image');
        }
    };

    const handleDeleteGalleryImage = async (id) => {
        if (!confirm('Delete this image?')) return;
        try {
            await galleryAPI.delete(id);
            setMessage('Image deleted successfully');
            fetchDashboardData();
        } catch (error) {
            setMessage('Failed to delete image');
        }
    };

    return (
        <div className="admin-dashboard">
            <header className="admin-header">
                <h1>🛠️ Admin Dashboard</h1>
                <button onClick={() => {
                    sessionStorage.clear();
                    window.location.href = '/';
                }} className="btn-logout">Logout</button>
            </header>

            <nav className="admin-nav">
                <button className={activeTab === 'dashboard' ? 'active' : ''} onClick={() => setActiveTab('dashboard')}>
                    📊 Dashboard
                </button>
                <button className={activeTab === 'connections' ? 'active' : ''} onClick={() => setActiveTab('connections')}>
                    🔌 Connections
                </button>
                <button className={activeTab === 'complaints' ? 'active' : ''} onClick={() => setActiveTab('complaints')}>
                    📋 Complaints
                </button>
                <button className={activeTab === 'gallery' ? 'active' : ''} onClick={() => setActiveTab('gallery')}>
                    🖼️ Gallery
                </button>
            </nav>

            <div className="admin-content">
                {loading ? (
                    <div className="loading">Loading...</div>
                ) : (
                    <>
                        {activeTab === 'dashboard' && (
                            <div className="stats-grid">
                                <div className="stat-card">
                                    <h3>Total Consumers</h3>
                                    <p className="stat-value">{stats.totalConsumers || 0}</p>
                                </div>
                                <div className="stat-card">
                                    <h3>Pending Bills</h3>
                                    <p className="stat-value">{stats.pendingBills || 0}</p>
                                </div>
                                <div className="stat-card">
                                    <h3>Total Revenue</h3>
                                    <p className="stat-value">₹{stats.totalRevenue || 0}</p>
                                </div>
                                <div className="stat-card">
                                    <h3>Pending Complaints</h3>
                                    <p className="stat-value">{stats.pendingComplaints || 0}</p>
                                </div>
                            </div>
                        )}

                        {activeTab === 'connections' && (
                            <div className="data-table">
                                <h2>Connection Requests</h2>
                                {connections.length === 0 ? (
                                    <p>No connection requests</p>
                                ) : (
                                    connections.map((conn) => (
                                        <div key={conn.id} className="data-card">
                                            <div className="data-row">
                                                <strong>Applicant:</strong> {conn.applicantName}
                                            </div>
                                            <div className="data-row">
                                                <strong>Type:</strong> {conn.serviceType}
                                            </div>
                                            <div className="data-row">
                                                <strong>Date:</strong> {conn.appliedDate}
                                            </div>
                                            <div className="data-row">
                                                <strong>Status:</strong> <span className={`badge ${conn.status.toLowerCase()}`}>{conn.status}</span>
                                            </div>
                                            {conn.status === 'PENDING' && (
                                                <div className="data-actions">
                                                    <button onClick={() => handleConnectionStatus(conn.id, 'APPROVED')} className="btn-approve">
                                                        Approve
                                                    </button>
                                                    <button onClick={() => handleConnectionStatus(conn.id, 'REJECTED')} className="btn-reject">
                                                        Reject
                                                    </button>
                                                </div>
                                            )}
                                        </div>
                                    ))
                                )}
                            </div>
                        )}

                        {activeTab === 'complaints' && (
                            <div className="data-table">
                                <h2>All Complaints</h2>
                                {complaints.length === 0 ? (
                                    <p>No complaints</p>
                                ) : (
                                    complaints.map((complaint) => (
                                        <div key={complaint.id} className="data-card">
                                            <div className="data-row">
                                                <strong>Type:</strong> {complaint.type}
                                            </div>
                                            <div className="data-row">
                                                <strong>Description:</strong> {complaint.description}
                                            </div>
                                            <div className="data-row">
                                                <strong>Date:</strong> {complaint.complaintDate}
                                            </div>
                                            <div className="data-row">
                                                <strong>Status:</strong> <span className={`badge ${complaint.status.toLowerCase()}`}>{complaint.status}</span>
                                            </div>
                                            {complaint.imageData && (
                                                <div className="data-row">
                                                    <img src={complaint.imageData} alt="Complaint" style={{ maxWidth: '200px', borderRadius: '8px' }} />
                                                </div>
                                            )}
                                            {complaint.status !== 'RESOLVED' && (
                                                <div className="data-actions">
                                                    {complaint.status === 'PENDING' && (
                                                        <button onClick={() => handleComplaintStatus(complaint.id, 'IN_PROGRESS')} className="btn-primary">
                                                            Mark In Progress
                                                        </button>
                                                    )}
                                                    <button onClick={() => handleComplaintStatus(complaint.id, 'RESOLVED')} className="btn-approve">
                                                        Mark Resolved
                                                    </button>
                                                </div>
                                            )}
                                        </div>
                                    ))
                                )}
                            </div>
                        )}

                        {activeTab === 'gallery' && (
                            <div className="gallery-admin">
                                <div className="gallery-form">
                                    <h2>Add New Image</h2>
                                    <form onSubmit={handleAddGalleryImage}>
                                        <div className="form-group">
                                            <label>Title</label>
                                            <input
                                                type="text"
                                                value={galleryForm.title}
                                                onChange={(e) => setGalleryForm({ ...galleryForm, title: e.target.value })}
                                                required
                                            />
                                        </div>
                                        <div className="form-group">
                                            <label>Description</label>
                                            <textarea
                                                value={galleryForm.description}
                                                onChange={(e) => setGalleryForm({ ...galleryForm, description: e.target.value })}
                                                rows="3"
                                                required
                                            />
                                        </div>
                                        <FileUpload
                                            label="Image"
                                            onFileSelect={(base64) => setGalleryForm({ ...galleryForm, imageData: base64 })}
                                            preview={galleryForm.imageData}
                                        />
                                        <button type="submit" className="btn-primary">Add Image</button>
                                    </form>
                                </div>

                                <div className="gallery-list">
                                    <h2>Gallery Images</h2>
                                    <div className="gallery-grid-admin">
                                        {galleryImages.map((img) => (
                                            <div key={img.id} className="gallery-item-admin">
                                                <img src={img.imageData} alt={img.title} />
                                                <div className="gallery-item-info">
                                                    <h4>{img.title}</h4>
                                                    <p>{img.description}</p>
                                                    <button onClick={() => handleDeleteGalleryImage(img.id)} className="btn-delete">
                                                        Delete
                                                    </button>
                                                </div>
                                            </div>
                                        ))}
                                    </div>
                                </div>
                            </div>
                        )}
                    </>
                )}

                {message && <div className="admin-message">{message}</div>}
            </div>
        </div>
    );
}

export default AdminDashboard;
