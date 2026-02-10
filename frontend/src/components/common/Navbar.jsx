import { useState } from 'react';
import { useTranslation } from 'react-i18next';
import { useNavigate, Link } from 'react-router-dom';
import logo from '../../assets/logo.png';
import './Navbar.css';

const Navbar = () => {
    const { t, i18n } = useTranslation();
    const navigate = useNavigate();
    const [isLangDropdownOpen, setIsLangDropdownOpen] = useState(false);
    const isAuthenticated = sessionStorage.getItem('isAuthenticated') === 'true';

    const handleLogout = () => {
        sessionStorage.clear();
        navigate('/');
        window.location.reload(); // To refresh Navbar state
    };

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
            case 'hi': return 'हिंदी'; // Hindi
            case 'mr': return 'मराठी'; // Marathi
            default: return 'Language';
        }
    };

    return (
        <nav className="navbar">
            <div className="container nav-container">
                <div className="logo" onClick={() => navigate('/home')} style={{ cursor: 'pointer', display: 'flex', alignItems: 'center', gap: '15px' }}>
                    <img src={logo} alt="Digi Panchayat Logo" style={{ height: '100px' }} />
                    <h2>{t('common.appTitle')}</h2>
                </div>

                <div className="nav-right">
                    <div className="common-nav-links">
                        <Link to="/services">{t('nav.services')}</Link>
                        <Link to="/about">{t('nav.about')}</Link>
                        <Link to="/gallery">{t('nav.gallery')}</Link>
                        {isAuthenticated ? (
                            <button onClick={handleLogout} className="btn btn-primary btn-nav-login btn-logout-nav">
                                {t('nav.logout', 'Logout')}
                            </button>
                        ) : (
                            <button onClick={() => navigate('/login')} className="btn btn-primary btn-nav-login">
                                {t('nav.login')}
                            </button>
                        )}
                    </div>

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
                </div>
            </div>
        </nav>
    );
};

export default Navbar;
