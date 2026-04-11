import React from 'react';
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import { Toaster } from 'react-hot-toast';
import { AuthProvider, useAuth } from './context/AuthContext';
import Sidebar from './components/Sidebar';
import Login from './pages/Login';
import Register from './pages/Register';
import Dashboard from './pages/Dashboard';
import ApplyLeave from './pages/ApplyLeave';
import MyLeaves from './pages/MyLeaves';
import Approvals from './pages/Approvals';
import TeamLeaves from './pages/TeamLeaves';
import Reports from './pages/Reports';
import Holidays from './pages/Holidays';
import ChangePassword from './pages/ChangePassword';
import Admin from './pages/Admin';
import './index.css';

const MANAGER_ROLES = ['MANAGER','BUSINESS_MANAGER','MANAGING_DIRECTOR','ADMIN'];

function ProtectedLayout() {
  const { user } = useAuth();
  if (!user) return <Navigate to="/login" replace />;
  const isManager = MANAGER_ROLES.includes(user.role);

  return (
    <div className="app-layout">
      <Sidebar />
      <main className="main-content">
        <Routes>
          <Route path="/dashboard" element={<Dashboard />} />
          <Route path="/apply" element={<ApplyLeave />} />
          <Route path="/history" element={<MyLeaves />} />
          <Route path="/holidays" element={<Holidays />} />
          <Route path="/change-password" element={<ChangePassword />} />
          {isManager && <>
            <Route path="/approvals" element={<Approvals />} />
            <Route path="/team-leaves" element={<TeamLeaves />} />
            <Route path="/reports" element={<Reports />} />
          </>}
          {user.role === 'ADMIN' && <Route path="/admin" element={<Admin />} />}
          <Route path="*" element={<Navigate to="/dashboard" replace />} />
        </Routes>
      </main>
    </div>
  );
}

function AppRoutes() {
  const { user, loading } = useAuth();
  if (loading) return <div style={{display:'flex',alignItems:'center',justifyContent:'center',height:'100vh',color:'var(--text-muted)'}}>Loading...</div>;
  return (
    <Routes>
      <Route path="/login" element={user ? <Navigate to="/dashboard" replace /> : <Login />} />
      <Route path="/register" element={user ? <Navigate to="/dashboard" replace /> : <Register />} />
      <Route path="/*" element={<ProtectedLayout />} />
    </Routes>
  );
}

export default function App() {
  return (
    <AuthProvider>
      <BrowserRouter>
        <AppRoutes />
        <Toaster
          position="top-right"
          toastOptions={{
            style: { background: 'var(--surface)', color: 'var(--text)', border: '1px solid var(--border)' },
            success: { iconTheme: { primary: 'var(--green)', secondary: '#fff' } },
            error: { iconTheme: { primary: 'var(--red)', secondary: '#fff' } },
          }}
        />
      </BrowserRouter>
    </AuthProvider>
  );
}
