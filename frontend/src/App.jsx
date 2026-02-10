import { Routes, Route, Navigate } from 'react-router-dom';
import { useTranslation } from 'react-i18next';
import './App.css';

// Pages
import LandingPage from './pages/public/LandingPage';
import Services from './pages/public/Services'; // Import Services
import About from './pages/public/About'; // Import About
import Gallery from './pages/public/Gallery'; // Import Gallery
import Login from './pages/auth/Login';
import Register from './pages/auth/Register';
import ConsumerDashboard from './pages/consumer/Dashboard';
import AdminDashboard from './pages/admin/Dashboard';

// Service Pages
import WaterBills from './pages/services/WaterBills';
import PropertyTax from './pages/services/PropertyTax';
import Complaints from './pages/services/Complaints';

import NewConnection from './pages/services/NewConnection';
import Payment from './pages/services/Payment';

function App() {
  const { i18n } = useTranslation();

  return (
    <div className="app">
      <Routes>
        <Route path="/" element={<Navigate to="/home" replace />} />
        <Route path="/home" element={<LandingPage />} />
        <Route path="/services" element={<Services />} /> {/* Add Services route */}
        <Route path="/about" element={<About />} /> {/* Add About route */}
        <Route path="/gallery" element={<Gallery />} /> {/* Add Gallery route */}
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Register />} />
        <Route path="/dashboard" element={<ConsumerDashboard />} />
        <Route path="/admin" element={<AdminDashboard />} />

        {/* Service Routes */}
        <Route path="/services/water-bills" element={<WaterBills />} />
        <Route path="/services/property-tax" element={<PropertyTax />} />
        <Route path="/services/complaints" element={<Complaints />} />
        <Route path="/services/new-connection" element={<NewConnection />} />
        <Route path="/payment/:billType/:billId" element={<Payment />} />
      </Routes>
    </div>
  );
}

export default App;
