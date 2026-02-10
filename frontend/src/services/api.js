import axios from 'axios';

const API_BASE_URL = 'http://localhost:8030/api';

// Create axios instance
const api = axios.create({
    baseURL: API_BASE_URL,
    headers: {
        'Content-Type': 'application/json',
    },
});

// Request interceptor
api.interceptors.request.use(
    (config) => {
        const token = sessionStorage.getItem('token');
        if (token) {
            config.headers.Authorization = `Bearer ${token}`;
        }
        return config;
    },
    (error) => {
        return Promise.reject(error);
    }
);

// Response interceptor
api.interceptors.response.use(
    (response) => response.data,
    (error) => {
        if (error.response?.status === 401) {
            sessionStorage.removeItem('token');
            sessionStorage.removeItem('user');
            window.location.href = '/login';
        }
        return Promise.reject(error.response?.data || error.message);
    }
);

// Auth APIs
export const authAPI = {
    sendOTP: (email, purpose) => api.post('/auth/send-otp', { email, purpose }),
    verifyOTP: (email, otpCode) => api.post('/auth/verify-otp', { emailOrMobile: email, otp: otpCode }),
    register: (data) => api.post('/auth/register', data),
    login: (data) => api.post('/auth/login', data),
};

// Bill APIs
export const billAPI = {
    getWaterBill: (consumerServiceNumber) => api.get(`/bills/water/${consumerServiceNumber}`),
    getWaterBillWithPenalty: (consumerServiceNumber) => api.get(`/bills/water/penalty/${consumerServiceNumber}`),
    getPropertyTaxBill: (consumerServiceNumber) => api.get(`/bills/property/${consumerServiceNumber}`),
    getPropertyTaxBillWithPenalty: (consumerServiceNumber) => api.get(`/bills/property/penalty/${consumerServiceNumber}`),
    getConsumerBills: (consumerId) => api.get(`/bills/consumer/${consumerId}`),
    getConsumerBillsWithPenalty: (consumerId) => api.get(`/bills/consumer/penalty/${consumerId}`),
    calculatePropertyTax: (data) => api.post(`/bills/property/calculate`, data),
    generateSampleBills: () => api.post(`/bills/generate-samples`),
    downloadWaterBillPdf: (consumerServiceNumber) =>
        api.get(`/bills/water/${consumerServiceNumber}/pdf`, { responseType: 'blob' }),
    downloadPropertyTaxPdf: (consumerServiceNumber) =>
        api.get(`/bills/property/${consumerServiceNumber}/pdf`, { responseType: 'blob' }),
};

// Complaint APIs
export const complaintAPI = {
    register: (data) => api.post('/complaints/register', data),
    getConsumerComplaints: (consumerId) => api.get(`/complaints/consumer/${consumerId}`),
    getAllComplaints: () => api.get('/complaints/all'),
    updateStatus: (complaintId, status) => api.put(`/complaints/${complaintId}/status?status=${status}`),
};

// New Connection APIs
export const connectionAPI = {
    apply: (data) => api.post('/connections/apply', data),
    getConsumerRequests: (consumerId) => api.get(`/connections/consumer/${consumerId}`),
    getAllRequests: () => api.get('/connections/all'),
    getPendingRequests: () => api.get('/connections/pending'),
    getRequestById: (id) => api.get(`/connections/${id}`),
    updateStatus: (id, status) => api.put(`/connections/${id}/status?status=${status}`),
};

// Gallery APIs
export const galleryAPI = {
    getAll: () => api.get('/gallery'),
    add: (data) => api.post('/gallery', data),
    delete: (id) => api.delete(`/gallery/${id}`),
};

// Payment APIs
export const paymentAPI = {
    makePayment: (data) => api.post('/payments', data),
    getConsumerPayments: (consumerId) => api.get(`/payments/consumer/${consumerId}`),
    getAllPayments: () => api.get('/payments/all'),
};

// Admin APIs
export const adminAPI = {
    getDashboardStats: () => api.get('/admin/dashboard'),
    getAllConsumers: () => api.get('/admin/consumers'),
    getConsumerById: (id) => api.get(`/admin/consumers/${id}`),
};

export default api;
