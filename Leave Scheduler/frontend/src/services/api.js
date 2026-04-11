import axios from 'axios';

const api = axios.create({ baseURL: '/api' });

api.interceptors.request.use(config => {
  const token = localStorage.getItem('token');
  if (token) config.headers.Authorization = `Bearer ${token}`;
  return config;
});

api.interceptors.response.use(
  res => res,
  err => {
    if (err.response?.status === 401) {
      localStorage.clear();
      window.location.href = '/login';
    }
    return Promise.reject(err);
  }
);

// Auth
export const login = (data) => api.post('/auth/login', data);
export const register = (data) => api.post('/auth/register', data);
export const changePassword = (data) => api.post('/auth/change-password', data);

// Leaves
export const applyLeave = (data) => api.post('/leaves/apply', data);
export const withdrawLeave = (id) => api.put(`/leaves/${id}/withdraw`);
export const getMyLeaves = () => api.get('/leaves/my');
export const getMyBalance = () => api.get('/leaves/balance');

// Manager
export const getPendingApprovals = () => api.get('/manager/pending');
export const getTeamLeaves = () => api.get('/manager/team-leaves');
export const actOnLeave = (id, data) => api.put(`/manager/leaves/${id}/action`, data);

// Holidays
export const getHolidays = () => api.get('/holidays');
export const createHoliday = (data) => api.post('/holidays', data);
export const deleteHoliday = (id) => api.delete(`/holidays/${id}`);

// Reports
export const getLeaveSummary = () => api.get('/reports/leave-summary');

// Admin
export const getUsers = () => api.get('/admin/users');
export const createUser = (data) => api.post('/admin/users', data);
export const updateUser = (id, data) => api.put(`/admin/users/${id}`, data);
export const deleteUser = (id) => api.delete(`/admin/users/${id}`);

export default api;
