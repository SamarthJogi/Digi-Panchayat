import Navbar from '../../components/common/Navbar';
import ServicesGrid from '../../components/common/ServicesGrid'; // Import reusable component
import './LandingPage.css'; // Reusing LandingPage styles for consistency

const Services = () => {
    return (
        <div className="landing-page">
            <Navbar />
            <div className="main-content" style={{ marginTop: '120px' }}>
                <ServicesGrid />
            </div>
            <footer className="footer">
                <div className="container">
                    <p>© 2026 Digi Panchayat. All rights reserved.</p>
                </div>
            </footer>
        </div>
    );
};

export default Services;
