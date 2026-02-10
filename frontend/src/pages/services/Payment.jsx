import { useState, useEffect } from 'react';
import { useTranslation } from 'react-i18next';
import { useParams, useNavigate } from 'react-router-dom';
import { billAPI, paymentAPI } from '../../services/api';
import Navbar from '../../components/common/Navbar';
import './Services.css';

function Payment() {
    const { t } = useTranslation();
    const { billType, billId } = useParams();
    const navigate = useNavigate();
    const [billDetails, setBillDetails] = useState(null);
    const [loading, setLoading] = useState(true);
    const [paymentMethod, setPaymentMethod] = useState('upi');

    useEffect(() => {
        // In a real app, we would fetch bill details using billId
        // For now, we'll mock it or just show general payment info
        setLoading(false);
    }, [billId]);

    const handlePayment = async (e) => {
        e.preventDefault();
        setLoading(true);

        try {
            // Simulate payment processing
            await new Promise(resolve => setTimeout(resolve, 2000));

            if (paymentMethod === 'cash') {
                alert('Please visit the nearest counter to pay cash. Reference ID generated.');
            } else {
                alert('Payment Successful!');
            }

            navigate('/dashboard');
        } catch (error) {
            alert('Payment Failed');
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="service-page">
            <Navbar />

            <div className="service-content" style={{ marginTop: '120px' }}>
                <div className="service-header">
                    <h1>{t('payment.title')}</h1>
                    <p>{t('payment.subtitle')}</p>
                </div>

                <div className="payment-container" style={{ maxWidth: '600px', margin: '0 auto', padding: '20px', background: 'white', borderRadius: '10px', boxShadow: '0 4px 6px rgba(0,0,0,0.1)' }}>
                    <div className="bill-summary">
                        <h3>{t('payment.billDetails')}</h3>
                        <p><strong>{t('payment.type')}:</strong> {billType === 'water' ? t('bills.waterBill') : t('bills.propertyTax')}</p>
                        <p><strong>{t('payment.billId')}:</strong> {billId}</p>
                    </div>

                    <form onSubmit={handlePayment} className="payment-form">
                        <h3>{t('payment.selectMethod')}</h3>

                        <div className="payment-methods" style={{ display: 'flex', flexDirection: 'column', gap: '15px', marginTop: '20px' }}>
                            <label className="payment-option" style={{ padding: '15px', border: '1px solid #ddd', borderRadius: '8px', cursor: 'pointer', display: 'flex', alignItems: 'center', gap: '10px' }}>
                                <input
                                    type="radio"
                                    name="payment"
                                    value="upi"
                                    checked={paymentMethod === 'upi'}
                                    onChange={(e) => setPaymentMethod(e.target.value)}
                                />
                                <span>{t('payment.upi')}</span>
                            </label>

                            <label className="payment-option" style={{ padding: '15px', border: '1px solid #ddd', borderRadius: '8px', cursor: 'pointer', display: 'flex', alignItems: 'center', gap: '10px' }}>
                                <input
                                    type="radio"
                                    name="payment"
                                    value="card"
                                    checked={paymentMethod === 'card'}
                                    onChange={(e) => setPaymentMethod(e.target.value)}
                                />
                                <span>{t('payment.card')}</span>
                            </label>

                            <label className="payment-option" style={{ padding: '15px', border: '1px solid #ddd', borderRadius: '8px', cursor: 'pointer', display: 'flex', alignItems: 'center', gap: '10px' }}>
                                <input
                                    type="radio"
                                    name="payment"
                                    value="netbanking"
                                    checked={paymentMethod === 'netbanking'}
                                    onChange={(e) => setPaymentMethod(e.target.value)}
                                />
                                <span>{t('payment.netbanking')}</span>
                            </label>

                            <label className="payment-option" style={{ padding: '15px', border: '1px solid #ddd', borderRadius: '8px', cursor: 'pointer', display: 'flex', alignItems: 'center', gap: '10px' }}>
                                <input
                                    type="radio"
                                    name="payment"
                                    value="cash"
                                    checked={paymentMethod === 'cash'}
                                    onChange={(e) => setPaymentMethod(e.target.value)}
                                />
                                <span>{t('payment.cash')}</span>
                            </label>
                        </div>

                        <button
                            type="submit"
                            className="btn btn-primary"
                            style={{ width: '100%', marginTop: '30px', padding: '12px' }}
                            disabled={loading}
                        >
                            {loading ? t('payment.processing') : paymentMethod === 'cash' ? t('payment.generateSlip') : t('payment.payNow')}
                        </button>
                    </form>
                </div>
            </div>
        </div>
    );
}

export default Payment;
