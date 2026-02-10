import { useNavigate } from 'react-router-dom';
import { useTranslation } from 'react-i18next';

const ServicesGrid = () => {
    const { t } = useTranslation();
    const navigate = useNavigate();

    return (
        <section id="services" className="services">
            <div className="container">
                <h2>{t('services.title')}</h2>
                <div className="services-grid">
                    <div className="service-card" onClick={() => navigate('/services/water-bills')} style={{ cursor: 'pointer' }}>
                        <div className="service-icon">💧</div>
                        <h3>{t('services.waterBilling')}</h3>
                        <p>Manage water bills with automated penalty calculations</p>
                    </div>
                    <div className="service-card" onClick={() => navigate('/services/property-tax')} style={{ cursor: 'pointer' }}>
                        <div className="service-icon">🏠</div>
                        <h3>{t('services.propertyTax')}</h3>
                        <p>Calculate and pay property tax online</p>
                    </div>
                    <div className="service-card" onClick={() => navigate('/services/complaints')} style={{ cursor: 'pointer' }}>
                        <div className="service-icon">📋</div>
                        <h3>{t('services.complaints')}</h3>
                        <p>Register and track your complaints with image support</p>
                    </div>
                    <div className="service-card" onClick={() => navigate('/services/new-connection')} style={{ cursor: 'pointer' }}>
                        <div className="service-icon">🔌</div>
                        <h3>{t('services.newConnection')}</h3>
                        <p>Apply for new water and property connections</p>
                    </div>
                </div>
            </div>
        </section>
    );
};

export default ServicesGrid;
