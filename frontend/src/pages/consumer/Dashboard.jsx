import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useTranslation } from 'react-i18next';
import { billAPI, complaintAPI, connectionAPI } from '../../services/api';
import './Dashboard.css';

function Dashboard() {
    const { t, i18n } = useTranslation();
    const navigate = useNavigate();
    const [bills, setBills] = useState([]);
    const [complaints, setComplaints] = useState([]);
    const [connectionRequests, setConnectionRequests] = useState([]);
    const [loading, setLoading] = useState(true);
    const [isLangDropdownOpen, setIsLangDropdownOpen] = useState(false);
    const user = JSON.parse(sessionStorage.getItem('user') || '{}');

    // Language handling logic
    const changeLanguage = (lng) => {
        i18n.changeLanguage(lng);
        localStorage.setItem('language', lng);
    };

    const toggleLangDropdown = () => {
        setIsLangDropdownOpen(!isLangDropdownOpen);
    };

    const handleLanguageSelect = (lng) => {
        changeLanguage(lng);
        setIsLangDropdownOpen(false);
    };

    const getLanguageLabel = (lng) => {
        switch (lng) {
            case 'en': return 'English';
            case 'hi': return 'हिंदी';
            case 'mr': return 'मराठी';
            default: return 'Language';
        }
    };

    useEffect(() => {
        if (!user.id) {
            navigate('/login');
            return;
        }
        fetchDashboardData();
    }, []);

    const fetchDashboardData = async () => {
        try {
            if (user.id) {
                const [billsRes, complaintsRes, connectionsRes] = await Promise.all([
                    billAPI.getConsumerBillsWithPenalty(user.id),
                    complaintAPI.getConsumerComplaints(user.id),
                    connectionAPI.getConsumerRequests(user.id)
                ]);

                setBills(billsRes.data || []);
                setComplaints(complaintsRes.data || []);
                setConnectionRequests(connectionsRes.data || []);
            }
        } catch (error) {
            console.error('Failed to fetch dashboard data:', error);
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="dashboard">
            <header className="dashboard-header">
                <div className="header-left">
                    <button onClick={() => navigate(-1)} className="btn-back">← {t('common.back')}</button>
                    <h1>{t('dashboard.welcome')}, {user.name}!</h1>
                </div>
                <div style={{ display: 'flex', alignItems: 'center' }}>
                    {/* Language Dropdown */}
                    <div className="language-dropdown">
                        <button className="lang-btn" onClick={toggleLangDropdown}>
                            {getLanguageLabel(i18n.language)} ▾
                        </button>
                        {isLangDropdownOpen && (
                            <div className="lang-menu">
                                <div onClick={() => handleLanguageSelect('en')}>English</div>
                                <div onClick={() => handleLanguageSelect('mr')}>मराठी</div>
                                <div onClick={() => handleLanguageSelect('hi')}>हिंदी</div>
                            </div>
                        )}
                    </div>
                    <button onClick={() => {
                        sessionStorage.clear();
                        window.location.href = '/';
                    }} className="btn-logout">{t('nav.logout')}</button>
                </div>
            </header>

            <div className="dashboard-content">
                {loading ? (
                    <div className="loading-spinner">{t('common.loading')}</div>
                ) : (
                    <>
                        <section className="bills-section">
                            <h2>💰 {t('bills.waterBill')} & {t('bills.propertyTax')}</h2>
                            {bills.length === 0 ? (
                                <p className="no-data">{t('bills.noWaterBills')}</p>
                            ) : (
                                <div className="bills-grid">
                                    {bills.map((bill) => (
                                        <div key={bill.id} className="bill-card">
                                            <h3>{bill.billType === 'WATER' ? `💧 ${t('bills.waterBill')}` : `🏠 ${t('bills.propertyTax')}`}</h3>
                                            <div className="bill-details">
                                                <p><strong>{t('bills.billDate')}:</strong> {bill.billDate}</p>
                                                <p><strong>{t('bills.dueDate')}:</strong> {bill.dueDate || 'N/A'}</p>
                                                <p><strong>{t('bills.baseAmount')}:</strong> ₹{bill.baseAmount}</p>
                                                {bill.penaltyAmount > 0 && (
                                                    <>
                                                        <p className="penalty"><strong>{t('bills.penaltyAmount')} ({bill.monthsDelayed} {t('bills.monthsDelayed')}):</strong> ₹{bill.penaltyAmount}</p>
                                                        <p className="total"><strong>{t('bills.totalAmount')}:</strong> ₹{bill.totalAmount}</p>
                                                    </>
                                                )}
                                                <span className={`status ${bill.status.toLowerCase()}`}>{bill.status}</span>
                                            </div>
                                            {bill.status === 'PENDING' && (
                                                <button className="btn btn-primary">{t('bills.payNow')}</button>
                                            )}
                                        </div>
                                    ))}
                                </div>
                            )}
                        </section>

                        <section className="complaints-section">
                            <h2>📋 {t('services.complaints')}</h2>
                            {complaints.length === 0 ? (
                                <p className="no-data">No complaints found.</p>
                            ) : (
                                <div className="complaints-grid">
                                    {complaints.map((complaint) => (
                                        <div key={complaint.id} className="complaint-card">
                                            <h3>
                                                <span>{complaint.type} Complaint</span>
                                                <span className={`status ${complaint.status.toLowerCase()}`}>{complaint.status}</span>
                                            </h3>
                                            <div className="complaint-details">
                                                <p><strong>Date:</strong> {complaint.complaintDate}</p>
                                                <p><strong>Description:</strong> {complaint.description}</p>
                                                {complaint.resolutionDate && (
                                                    <p><strong>Resolution Date:</strong> {complaint.resolutionDate}</p>
                                                )}
                                            </div>
                                        </div>
                                    ))}
                                </div>
                            )}
                        </section>

                        <section className="connections-section">
                            <h2>🔌 {t('services.newConnection')}</h2>
                            {connectionRequests.length === 0 ? (
                                <p className="no-data">No connection requests found.</p>
                            ) : (
                                <div className="connections-grid">
                                    {connectionRequests.map((request) => (
                                        <div key={request.id} className="connection-card">
                                            <h3>
                                                <span>{request.serviceType} Connection</span>
                                                <span className={`status ${request.status.toLowerCase()}`}>{request.status}</span>
                                            </h3>
                                            <div className="connection-details">
                                                <p><strong>Applied On:</strong> {request.appliedDate}</p>
                                                <p><strong>Applicant:</strong> {request.applicantName}</p>
                                                <p><strong>Address:</strong> {request.address || 'N/A'}</p>
                                                <p><strong>Phone:</strong> {request.phone || 'N/A'}</p>
                                                {request.resolutionDate && (
                                                    <p><strong>Resolution Date:</strong> {request.resolutionDate}</p>
                                                )}
                                            </div>
                                        </div>
                                    ))}
                                </div>
                            )}
                        </section>
                    </>
                )}
            </div>
        </div>
    );
}

export default Dashboard;
