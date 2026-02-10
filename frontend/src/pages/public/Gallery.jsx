import { useTranslation } from 'react-i18next';
import Navbar from '../../components/common/Navbar';
import './LandingPage.css';

const Gallery = () => {
    const { t } = useTranslation();

    return (
        <div className="landing-page">
            <Navbar />
            <div className="main-content" style={{ marginTop: '140px', minHeight: '60vh' }}>
                <div className="container">
                    <h2>{t('nav.gallery')}</h2>
                    <p>{t('gallery.comingSoon')}</p>
                    <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(200px, 1fr))', gap: '20px', marginTop: '20px' }}>
                        <div style={{ height: '200px', background: '#eee', borderRadius: '10px' }}></div>
                        <div style={{ height: '200px', background: '#eee', borderRadius: '10px' }}></div>
                        <div style={{ height: '200px', background: '#eee', borderRadius: '10px' }}></div>
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
};

export default Gallery;
