import { useState, useEffect } from 'react';
import { useTranslation } from 'react-i18next';
import { useNavigate } from 'react-router-dom';
import { billAPI } from '../../services/api';
import logo from '../../assets/logo.png';
import './Services.css';

function WaterBills() {
    const { t } = useTranslation();
    const navigate = useNavigate();
    const [bills, setBills] = useState([]);
    const [loading, setLoading] = useState(true);
    const [consumerNumber, setConsumerNumber] = useState('');
    const [searchBill, setSearchBill] = useState(null);
    const user = JSON.parse(sessionStorage.getItem('user') || '{}');

    useEffect(() => {
        // Check authentication
        if (!sessionStorage.getItem('isAuthenticated')) {
            navigate('/login');
            return;
        }
        fetchUserBills();
    }, []);

    const fetchUserBills = async () => {
        try {
            if (user.id) {
                const response = await billAPI.getConsumerBillsWithPenalty(user.id);
                // Filter only water bills
                const waterBills = (response.data || []).filter(bill => bill.billType === 'WATER');
                setBills(waterBills);
            }
        } catch (error) {
            console.error('Failed to fetch bills:', error);
        } finally {
            setLoading(false);
        }
    };

    const handleSearchBill = async (e) => {
        e.preventDefault();
        setLoading(true);
        try {
            const response = await billAPI.getWaterBillWithPenalty(consumerNumber);
            setSearchBill(response.data);
        } catch (error) {
            alert('Bill not found for consumer number: ' + consumerNumber);
            setSearchBill(null);
        } finally {
            setLoading(false);
        }
    };

    const handleDownloadPdf = async (consumerServiceNumber) => {
        try {
            const response = await billAPI.downloadWaterBillPdf(consumerServiceNumber);
            const blob = new Blob([response.data], { type: 'application/pdf' });
            const url = window.URL.createObjectURL(blob);
            const link = document.createElement('a');
            link.href = url;
            link.download = `WaterBill_${consumerServiceNumber}.pdf`;
            document.body.appendChild(link);
            link.click();
            document.body.removeChild(link);
            window.URL.revokeObjectURL(url);
        } catch (error) {
            alert('Failed to download PDF: ' + error.message);
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
                    <button onClick={() => navigate('/services/water-bills')} className="active">💧 {t('services.waterBilling')}</button>
                    <button onClick={() => navigate('/services/property-tax')}>🏠 {t('services.propertyTax')}</button>
                    <button onClick={() => navigate('/services/complaints')}>📋 {t('services.complaints')}</button>
                    <button onClick={() => navigate('/services/new-connection')}>🔌 {t('services.newConnection')}</button>
                    <button onClick={() => navigate('/dashboard')}>📊 {t('nav.dashboard')}</button>
                    <button onClick={handleLogout} className="btn-logout">{t('nav.logout')}</button>
                </div>
            </nav>

            <div className="service-content">
                <div className="service-header">
                    <h1>💧 {t('bills.waterTitle')}</h1>
                    <p>{t('bills.waterSubtitle')}</p>
                </div>

                {/* Search by Consumer Number */}
                <div className="search-section">
                    <h3>{t('bills.searchTitle')}</h3>
                    <form onSubmit={handleSearchBill} className="search-form">
                        <input
                            type="text"
                            placeholder={t('bills.searchPlaceholder')}
                            value={consumerNumber}
                            onChange={(e) => setConsumerNumber(e.target.value)}
                            required
                        />
                        <button type="submit" className="btn btn-primary">{t('bills.searchButton')}</button>
                    </form>

                    {searchBill && (
                        <div className="bill-card highlight">
                            <h3>💧 {t('bills.waterBill')} - {searchBill.consumerServiceNumber}</h3>
                            <div className="bill-details">
                                <p><strong>{t('bills.billDate')}:</strong> {searchBill.billDate}</p>
                                <p><strong>{t('bills.dueDate')}:</strong> {searchBill.dueDate || 'N/A'}</p>
                                <p><strong>{t('bills.unitsConsumed')}:</strong> {searchBill.unitsConsumed || 'N/A'}</p>
                                <p><strong>{t('bills.baseAmount')}:</strong> ₹{searchBill.baseAmount}</p>
                                {searchBill.penaltyAmount > 0 && (
                                    <>
                                        <p className="penalty"><strong>{t('bills.penaltyAmount')} ({searchBill.monthsDelayed} {t('bills.monthsDelayed')}):</strong> ₹{searchBill.penaltyAmount}</p>
                                        <p className="total"><strong>{t('bills.totalAmount')}:</strong> ₹{searchBill.totalAmount}</p>
                                    </>
                                )}
                                <span className={`status ${searchBill.status.toLowerCase()}`}>{searchBill.status}</span>
                            </div>
                            <div className="bill-actions">
                                <button onClick={() => handleDownloadPdf(searchBill.consumerServiceNumber)} className="btn btn-secondary">📄 {t('bills.downloadPdf')}</button>
                                {searchBill.status === 'PENDING' && (
                                    <button className="btn btn-primary" onClick={() => navigate(`/payment/water/${searchBill.id}`)}>{t('bills.payNow')}</button>
                                )}
                            </div>
                        </div>
                    )}
                </div>

                {/* User's Bills */}
                <div className="bills-section">
                    <h3>{t('bills.waterTitle')}</h3>
                    {loading ? (
                        <p>{t('common.loading')}</p>
                    ) : bills.length === 0 ? (
                        <p className="no-data">{t('bills.noWaterBills')}</p>
                    ) : (
                        <div className="bills-grid">
                            {bills.map((bill) => (
                                <div key={bill.id} className="bill-card">
                                    <h3>💧 {t('bills.waterBill')}</h3>
                                    <div className="bill-details">
                                        <p><strong>Consumer No:</strong> {bill.consumerServiceNumber}</p>
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
                                    <div className="bill-actions">
                                        <button onClick={() => handleDownloadPdf(bill.consumerServiceNumber)} className="btn btn-secondary">📄 {t('bills.downloadPdf')}</button>
                                        {bill.status === 'PENDING' && (
                                            <button className="btn btn-primary" onClick={() => navigate(`/payment/water/${bill.id}`)}>{t('bills.payNow')}</button>
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

export default WaterBills;
