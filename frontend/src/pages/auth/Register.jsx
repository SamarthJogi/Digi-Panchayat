import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useTranslation } from 'react-i18next';
import { authAPI } from '../../services/api';
import Navbar from '../../components/common/Navbar';
import '../auth/Login.css';

function Register() {
    const { t } = useTranslation();
    const navigate = useNavigate();
    const [step, setStep] = useState(1); // 1: form, 2: OTP
    const [formData, setFormData] = useState({
        name: '',
        email: '',
        phone: '',
        address: '',
        password: '',
        confirmPassword: '',
        otpCode: '',
    });
    const [loading, setLoading] = useState(false);
    const [message, setMessage] = useState('');

    const handleRegister = async (e) => {
        e.preventDefault();

        if (formData.password !== formData.confirmPassword) {
            setMessage('Passwords do not match');
            return;
        }

        setLoading(true);
        setMessage('');

        try {
            const response = await authAPI.register({
                name: formData.name,
                email: formData.email,
                mobile: formData.phone,
                address: formData.address,
                password: formData.password,
            });

            setMessage('Registration successful! OTP sent to your email.');
            setStep(2);
        } catch (error) {
            setMessage(error.message || 'Registration failed');
        } finally {
            setLoading(false);
        }
    };

    const handleVerifyOTP = async (e) => {
        e.preventDefault();
        setLoading(true);
        setMessage('');

        try {
            const response = await authAPI.verifyOTP(formData.email, formData.otpCode);
            if (response.success) {
                setMessage('Account verified! Redirecting to login...');
                setTimeout(() => navigate('/login'), 2000);
            } else {
                setMessage('Invalid OTP. Please try again.');
            }
        } catch (error) {
            setMessage(error.message || 'Verification failed');
        } finally {
            setLoading(false);
        }
    };

    const handleResendOTP = async () => {
        setLoading(true);
        try {
            await authAPI.sendOTP(formData.email, 'Account Verification');
            setMessage('OTP resent successfully!');
        } catch (error) {
            setMessage('Failed to resend OTP');
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="login-page">
            <Navbar />
            <div className="auth-content">
                <div className="login-container" style={{ maxWidth: '500px' }}>
                    <div className="login-card">
                        <h1 className="login-title">{t('auth.register')}</h1>

                        {step === 1 ? (
                            <form onSubmit={handleRegister}>
                                <div className="form-group">
                                    <label>{t('auth.name')}</label>
                                    <input
                                        type="text"
                                        value={formData.name}
                                        onChange={(e) => setFormData({ ...formData, name: e.target.value })}
                                        required
                                        placeholder="Enter your full name"
                                    />
                                </div>

                                <div className="form-group">
                                    <label>{t('auth.email')}</label>
                                    <input
                                        type="email"
                                        value={formData.email}
                                        onChange={(e) => setFormData({ ...formData, email: e.target.value })}
                                        required
                                        placeholder="Enter your email address"
                                    />
                                </div>

                                <div className="form-group">
                                    <label>{t('auth.phone')}</label>
                                    <input
                                        type="tel"
                                        value={formData.phone}
                                        onChange={(e) => setFormData({ ...formData, phone: e.target.value })}
                                        required
                                        placeholder="Enter your mobile number"
                                    />
                                </div>

                                <div className="form-group">
                                    <label>{t('auth.address')}</label>
                                    <textarea
                                        value={formData.address}
                                        onChange={(e) => setFormData({ ...formData, address: e.target.value })}
                                        rows="2"
                                        required
                                        style={{ width: '100%', padding: 'var(--space-3)', borderRadius: 'var(--radius-md)', border: '1px solid var(--border)' }}
                                        placeholder="Enter your complete address"
                                    />
                                </div>

                                <div className="form-group">
                                    <label>{t('auth.password')}</label>
                                    <input
                                        type="password"
                                        value={formData.password}
                                        onChange={(e) => setFormData({ ...formData, password: e.target.value })}
                                        required
                                        minLength="6"
                                        placeholder="Enter password (min. 6 chars)"
                                    />
                                </div>

                                <div className="form-group">
                                    <label>Confirm Password</label>
                                    <input
                                        type="password"
                                        value={formData.confirmPassword}
                                        onChange={(e) => setFormData({ ...formData, confirmPassword: e.target.value })}
                                        required
                                        minLength="6"
                                        placeholder="Re-enter your password"
                                    />
                                </div>

                                <button type="submit" className="btn btn-primary" disabled={loading}>
                                    {loading ? t('common.loading') : t('auth.register')}
                                </button>
                            </form>
                        ) : (
                            <form onSubmit={handleVerifyOTP}>
                                <p className="otp-sent-message">
                                    ✉️ OTP sent to {formData.email}<br />
                                    Check your email and enter the 6-digit code below.
                                </p>

                                <div className="form-group">
                                    <label>{t('auth.otpCode')}</label>
                                    <input
                                        type="text"
                                        maxLength="6"
                                        value={formData.otpCode}
                                        onChange={(e) => setFormData({ ...formData, otpCode: e.target.value })}
                                        required
                                        style={{ textAlign: 'center', fontSize: '1.5rem', letterSpacing: '0.5rem' }}
                                        placeholder="______"
                                    />
                                </div>

                                <button type="submit" className="btn btn-primary" disabled={loading}>
                                    {loading ? t('common.loading') : t('auth.verifyOTP')}
                                </button>

                                <button type="button" onClick={handleResendOTP} className="btn btn-secondary" style={{ marginTop: 'var(--space-3)' }} disabled={loading}>
                                    Resend OTP
                                </button>
                            </form>
                        )}

                        {message && <p className={`message ${message.includes('success') || message.includes('verified') ? 'success' : 'error'}`}>{message}</p>}

                        <p className="register-link">
                            Already have an account? <a href="/login">{t('auth.login')}</a>
                        </p>
                    </div>
                </div>
            </div>
            <footer className="footer">
                <div className="container">
                    <p>© 2026 Digi Panchayat. All rights reserved.</p>
                </div>
            </footer>
        </div>
    );
}

export default Register;
