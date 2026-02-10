import { useTranslation } from 'react-i18next';
import { useNavigate } from 'react-router-dom';
import Navbar from '../../components/common/Navbar';
import ServicesGrid from '../../components/common/ServicesGrid';
import './LandingPage.css';

function LandingPage() {
    const { t } = useTranslation();
    const navigate = useNavigate();


    const scrollToServices = () => {
        const servicesSection = document.getElementById('services');
        if (servicesSection) {
            servicesSection.scrollIntoView({ behavior: 'smooth' });
        }
    };

    return (
        <div className="landing-page">
            <Navbar />

            <section className="hero" style={{ marginTop: '120px' }}> {/* Add margin to account for fixed navbar */}
                <div className="container hero-content">
                    <h1 className="hero-title">{t('hero.title')}</h1>
                    <p className="hero-subtitle">{t('hero.subtitle')}</p>
                    <button onClick={() => navigate('/register')} className="btn btn-hero">
                        {t('hero.cta')}
                    </button>

                    <div className="scroll-down" onClick={scrollToServices}>
                        <svg width="40" height="40" viewBox="0 0 24 24" fill="none" stroke="white" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
                            <path d="M7 13l5 5 5-5M7 6l5 5 5-5" />
                        </svg>
                    </div>
                </div>
            </section>

            <ServicesGrid />

            <footer className="footer">
                <div className="container">
                    <p>© 2026 Digi Panchayat. All rights reserved.</p>
                </div>
            </footer>
        </div>
    );
}

export default LandingPage;
