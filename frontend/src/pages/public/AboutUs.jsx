import './AboutUs.css';

function AboutUs() {
    return (
        <div className="about-page">
            <div className="about-hero">
                <h1>About Digi Panchayat</h1>
                <p>Empowering villages through digital governance</p>
            </div>

            <div className="about-content">
                <section className="about-section">
                    <h2>Our Mission</h2>
                    <p>To digitalize panchayat services and make governance accessible, transparent, and efficient for all citizens.</p>
                </section>

                <section className="about-section">
                    <h2>What We Offer</h2>
                    <div className="features-grid">
                        <div className="feature">
                            <h3>💧 Digital Water Billing</h3>
                            <p>Automated billing with penalty calculations</p>
                        </div>
                        <div className="feature">
                            <h3>🏠 Property Tax Management</h3>
                            <p>Easy tax calculation and online payment</p>
                        </div>
                        <div className="feature">
                            <h3>📋 Complaint System</h3>
                            <p>24/7 complaint registration with tracking</p>
                        </div>
                        <div className="feature">
                            <h3>🔌 New Connections</h3>
                            <p>Online application for new services</p>
                        </div>
                    </div>
                </section>

                <section className="about-section">
                    <h2>Contact Us</h2>
                    <div className="contact-info">
                        <p>📧 Email: info@digipanchayat.gov.in</p>
                        <p>📞 Phone: 1800-XXX-XXXX</p>
                        <p>📍 Address: Panchayat Office, Village Name, District</p>
                    </div>
                </section>
            </div>
        </div>
    );
}

export default AboutUs;
