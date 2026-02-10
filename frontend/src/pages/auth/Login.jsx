import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useTranslation } from 'react-i18next';
import { authAPI } from '../../services/api';
import Navbar from '../../components/common/Navbar';
import './Login.css';

function Login() {
    const { t } = useTranslation();
    const navigate = useNavigate();
    const [step, setStep] = useState(1); // 1: email, 2: OTP verification
    const [formData, setFormData] = useState({
        email: '',
        password: '',
        otpCode: '',
    });
    const [loading, setLoading] = useState(false);
    const [message, setMessage] = useState('');

    useEffect(() => {
        const isAuthenticated = sessionStorage.getItem('isAuthenticated');
        if (isAuthenticated) {
            const user = JSON.parse(sessionStorage.getItem('user') || '{}');
            if (user.role === 'ADMIN') {
                navigate('/admin');
            } else {
                navigate('/services');
            }
        }
    }, [navigate]);

    const handleSendOTP = async (e) => {
        e.preventDefault();
        setLoading(true);
        setMessage('');

        try {
            // Validate credentials (this will trigger OTP content on backend)
            const response = await authAPI.login({
                emailOrMobile: formData.email,
                password: formData.password,
            });
            setMessage(response.message);
            setStep(2);
        } catch (error) {
            setMessage(error.message || 'Login failed. Please check credentials.');
        } finally {
            setLoading(false);
        }
    };

    const handleLogin = async (e) => {
        e.preventDefault();
        setLoading(true);
        setMessage('');

        try {
            // Verify OTP and get User details (including role)
            const response = await authAPI.verifyOTP(formData.email, formData.otpCode);

            if (response.success) {
                const user = response.data;

                // Store user details and token
                sessionStorage.setItem('user', JSON.stringify(user));
                // In a real JWT setup, the backend would return a token here too. 
                // For now, we simulate it or use what's available.
                sessionStorage.setItem('token', 'dummy-token-' + Date.now());
                sessionStorage.setItem('isAuthenticated', 'true');

                setMessage('Login successful!');

                // Redirect based on role
                setTimeout(() => {
                    if (user.role === 'ADMIN') {
                        window.location.href = '/admin';
                    } else {
                        window.location.href = '/services';
                    }
                }, 1000);
            } else {
                setMessage(response.message || 'Invalid OTP');
            }
        } catch (error) {
            setMessage(error.message || 'Verification failed');
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="login-page">
            <Navbar />
            <div className="auth-content">
                <div className="login-container">
                    <div className="login-card">
                        <h1 className="login-title">{t('auth.login')}</h1>

                        {step === 1 ? (
                            <form onSubmit={handleSendOTP}>
                                <div className="form-group">
                                    <label>{t('auth.email')}</label>
                                    <input
                                        type="email"
                                        value={formData.email}
                                        onChange={(e) => setFormData({ ...formData, email: e.target.value })}
                                        required
                                    />
                                </div>

                                <div className="form-group">
                                    <label>{t('auth.password')}</label>
                                    <input
                                        type="password"
                                        value={formData.password}
                                        onChange={(e) => setFormData({ ...formData, password: e.target.value })}
                                        required
                                    />
                                </div>

                                <button type="submit" className="btn btn-primary" disabled={loading}>
                                    {loading ? t('common.loading') : t('auth.login')}
                                </button>
                            </form>
                        ) : (
                            <form onSubmit={handleLogin}>
                                <p className="otp-sent-message">OTP sent to {formData.email}</p>

                                <div className="form-group">
                                    <label>{t('auth.otpCode')}</label>
                                    <input
                                        type="text"
                                        maxLength="6"
                                        value={formData.otpCode}
                                        onChange={(e) => setFormData({ ...formData, otpCode: e.target.value })}
                                        required
                                    />
                                </div>

                                <div className="form-actions">
                                    <button type="button" onClick={() => setStep(1)} className="btn btn-secondary">
                                        {t('common.back')}
                                    </button>
                                    <button type="submit" className="btn btn-primary" disabled={loading}>
                                        {loading ? t('common.loading') : t('auth.verifyOTP')}
                                    </button>
                                </div>
                            </form>
                        )}

                        {message && <p className={`message ${message.includes('success') ? 'success' : 'error'}`}>{message}</p>}

                        <p className="register-link">
                            Don't have an account? <a href="/register">{t('auth.register')}</a>
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

export default Login;
