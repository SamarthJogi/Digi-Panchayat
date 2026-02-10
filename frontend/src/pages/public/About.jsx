import { useTranslation } from 'react-i18next';
import Navbar from '../../components/common/Navbar';
import './LandingPage.css';

const About = () => {
    const { t } = useTranslation();

    return (
        <div className="landing-page">
            <Navbar />
            <div className="main-content" style={{
                marginTop: '100px',
                minHeight: '70vh',
                display: 'flex',
                alignItems: 'center',
                justifyContent: 'center'
            }}>
                <div className="container" style={{ textAlign: 'center', maxWidth: '900px' }}>
                    <h2 style={{ fontSize: '2.5rem', marginBottom: '20px', color: 'white' }}>{t('nav.about')}</h2>
                    <p style={{ fontSize: '1.25rem', lineHeight: '1.8', color: 'white', fontWeight: '500' }}>
                        {t('about.content')}
                    </p>
                </div>
            </div>
            <footer className="footer">
                <div className="container">
                    <p>© 2026 Digi Panchayat. All rights reserved.</p>
                </div>
            </footer>
        </div>
    );
};

export default About;
