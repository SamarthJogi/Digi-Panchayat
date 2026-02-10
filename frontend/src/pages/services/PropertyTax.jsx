import { useState, useEffect } from 'react';
import { useTranslation } from 'react-i18next';
import { useNavigate } from 'react-router-dom';
import { billAPI } from '../../services/api';
import logo from '../../assets/logo.png';
import './Services.css';

function PropertyTax() {
    const { t } = useTranslation();
    const navigate = useNavigate();
    const [bills, setBills] = useState([]);
    const [loading, setLoading] = useState(false);
    const [showCalculator, setShowCalculator] = useState(false);
    const [propertyData, setPropertyData] = useState({
        usageType: 'RESIDENTIAL',
        area: '',
        consumerServiceNumber: ''
    });
    const [consumerNumber, setConsumerNumber] = useState('');
    const [searchBill, setSearchBill] = useState(null);
    const [calculatedTax, setCalculatedTax] = useState(null);
    const user = JSON.parse(sessionStorage.getItem('user') || '{}');

    useEffect(() => {
        if (!sessionStorage.getItem('isAuthenticated')) {
            navigate('/login');
            return;
        }
        fetchPropertyBills();
    }, []);

    const fetchPropertyBills = async () => {
        setLoading(true);
        try {
            if (user.id) {
                const response = await billAPI.getConsumerBillsWithPenalty(user.id);
                const propertyBills = (response.data || []).filter(bill => bill.billType === 'PROPERTY_TAX');
                setBills(propertyBills);
            }
        } catch (error) {
            console.error('Failed to fetch property bills:', error);
        } finally {
            setLoading(false);
        }
    };

    const handleCalculateTax = async (e) => {
        e.preventDefault();
        setLoading(true);
        try {
            // Remove consumerId since we don't have user.id from login
            const response = await billAPI.calculatePropertyTax(propertyData);
            setCalculatedTax(response.data);
            alert('Tax calculated successfully! Amount: ₹' + (response.data.baseAmount || response.data.amount));
        } catch (error) {
            alert('Failed to calculate tax: ' + (error.message || 'Unknown error'));
        } finally {
            setLoading(false);
        }
    };

    const handleSearchBill = async (e) => {
        e.preventDefault();
        setLoading(true);
        try {
            const response = await billAPI.getPropertyTaxBillWithPenalty(consumerNumber);
            setSearchBill(response.data);
        } catch (error) {
            alert('Bill not found for consumer number: ' + consumerNumber);
            setSearchBill(null);
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
                    <button onClick={() => navigate('/services/property-tax')} className="active">🏠 {t('services.propertyTax')}</button>
                    <button onClick={() => navigate('/services/complaints')}>📋 {t('services.complaints')}</button>
                    <button onClick={() => navigate('/services/new-connection')}>🔌 {t('services.newConnection')}</button>
                    <button onClick={() => navigate('/dashboard')}>📊 {t('nav.dashboard')}</button>
                    <button onClick={handleLogout} className="btn-logout">{t('nav.logout')}</button>
                </div>
            </nav>

            <div className="service-content">
                <div className="service-header">
                    <h1>🏠 {t('bills.propertyTitle')}</h1>
                    <p>{t('bills.propertySubtitle')}</p>
                </div>

                <div className="action-buttons">
                    <button
                        onClick={() => setShowCalculator(!showCalculator)}
                        className="btn btn-primary"
                    >
                        {showCalculator ? t('bills.hideCalculator') : `🧮 ${t('bills.calculateTax')}`}
                    </button>
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
                            <h3>🏠 {t('bills.propertyTax')} - {searchBill.consumerServiceNumber}</h3>
                            <div className="bill-details">
                                <p><strong>{t('bills.billDate')}:</strong> {searchBill.billDate}</p>
                                <p><strong>{t('bills.dueDate')}:</strong> {searchBill.dueDate || 'N/A'}</p>
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
                                {searchBill.status === 'PENDING' && (
                                    <button className="btn btn-primary" onClick={() => navigate(`/payment/property-tax/${searchBill.id}`)}>{t('bills.payNow')}</button>
                                )}
                            </div>
                        </div>
                    )}
                </div>

                {showCalculator && (
                    <div className="calculator-section">
                        <h3>{t('bills.calculatorTitle')}</h3>
                        <form onSubmit={handleCalculateTax} className="property-form">
                            <div className="form-group">
                                <label>{t('bills.searchPlaceholder')} (Optional)</label>
                                <input
                                    type="text"
                                    value={propertyData.consumerServiceNumber}
                                    onChange={(e) => setPropertyData({ ...propertyData, consumerServiceNumber: e.target.value })}
                                    placeholder={t('bills.searchPlaceholder')}
                                />
                            </div>

                            <div className="form-group">
                                <label>{t('bills.usageType')}</label>
                                <select
                                    value={propertyData.usageType}
                                    onChange={(e) => setPropertyData({ ...propertyData, usageType: e.target.value })}
                                >
                                    <option value="RESIDENTIAL">{t('bills.residential')}</option>
                                    <option value="RENTED">{t('bills.rented')}</option>
                                    <option value="COMMERCIAL">{t('bills.commercial')}</option>
                                </select>
                            </div>

                            <div className="form-group">
                                <label>{t('bills.area')}</label>
                                <input
                                    type="number"
                                    value={propertyData.area}
                                    onChange={(e) => setPropertyData({ ...propertyData, area: e.target.value })}
                                    required
                                    placeholder={t('bills.area')}
                                />
                            </div>

                            <button type="submit" className="btn btn-primary" disabled={loading}>
                                {loading ? t('bills.calculating') : t('bills.calculateTax')}
                            </button>
                        </form>

                        {calculatedTax && (
                            <div className="tax-result">
                                <h4>{t('bills.calculatedTax')}</h4>
                                <p className="tax-amount">₹{calculatedTax.baseAmount || calculatedTax.amount}</p>
                                <p>{t('bills.taxSuccess')}</p>
                            </div>
                        )}
                    </div>
                )}

                <div className="bills-section">
                    <h3>{t('bills.propertyTitle')}</h3>
                    {loading ? (
                        <p>{t('common.loading')}</p>
                    ) : bills.length === 0 ? (
                        <p className="no-data">{t('bills.noPropertyBills')}</p>
                    ) : (
                        <div className="bills-grid">
                            {bills.map((bill) => (
                                <div key={bill.id} className="bill-card">
                                    <h3>🏠 {t('bills.propertyTax')}</h3>
                                    <div className="bill-details">
                                        <p><strong>{t('bills.billDate')}:</strong> {bill.billDate}</p>
                                        <p><strong>{t('bills.dueDate')}:</strong> {bill.dueDate || 'N/A'}</p>
                                        <p><strong>{t('bills.baseAmount')}:</strong> ₹{bill.baseAmount}</p>
                                        {bill.penaltyAmount > 0 && (
                                            <>
                                                <p className="penalty"><strong>{t('bills.penaltyAmount')}:</strong> ₹{bill.penaltyAmount}</p>
                                                <p className="total"><strong>{t('bills.totalAmount')}:</strong> ₹{bill.totalAmount}</p>
                                            </>
                                        )}
                                        <span className={`status ${bill.status.toLowerCase()}`}>{bill.status}</span>
                                    </div>
                                    {bill.status === 'PENDING' && (
                                        <button className="btn btn-primary" onClick={() => navigate(`/payment/property-tax/${bill.id}`)}>{t('bills.payNow')}</button>
                                    )}
                                </div>
                            ))}
                        </div>
                    )}
                </div>
            </div>
        </div>
    );
}

export default PropertyTax;
