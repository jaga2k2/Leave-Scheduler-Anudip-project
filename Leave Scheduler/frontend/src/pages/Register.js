import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import toast from 'react-hot-toast';
import { register as registerApi } from '../services/api';
import { useAuth } from '../context/AuthContext';

const DEPARTMENTS = ['ENGINEERING', 'MANAGEMENT', 'HR', 'FINANCE', 'OPERATIONS'];

export default function Register() {
  const [form, setForm] = useState({
    firstName: '',
    lastName: '',
    username: '',
    email: '',
    password: '',
    confirmPassword: '',
    department: 'ENGINEERING',
    designation: ''
  });
  const [loading, setLoading] = useState(false);
  const { login } = useAuth();
  const navigate = useNavigate();

  const set = (key, value) => setForm(current => ({ ...current, [key]: value }));

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (form.password.length < 6) {
      toast.error('Password must be at least 6 characters');
      return;
    }
    if (form.password !== form.confirmPassword) {
      toast.error('Passwords do not match');
      return;
    }

    setLoading(true);
    try {
      const payload = {
        firstName: form.firstName,
        lastName: form.lastName,
        username: form.username,
        email: form.email,
        password: form.password,
        department: form.department,
        designation: form.designation
      };
      const res = await registerApi(payload);
      const { token, ...userData } = res.data;
      login(userData, token);
      toast.success('Account created');
      navigate('/dashboard');
    } catch (err) {
      toast.error(err.response?.data?.message || 'Registration failed');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="login-page">
      <div className="login-card register-card">
        <div className="login-logo">
          <h1>Create Account</h1>
          <p>Register as an employee</p>
        </div>
        <form onSubmit={handleSubmit}>
          <div className="form-grid">
            <div className="form-group">
              <label>First Name</label>
              <input value={form.firstName} onChange={e => set('firstName', e.target.value)} required />
            </div>
            <div className="form-group">
              <label>Last Name</label>
              <input value={form.lastName} onChange={e => set('lastName', e.target.value)} required />
            </div>
          </div>
          <div className="form-group">
            <label>Username</label>
            <input value={form.username} onChange={e => set('username', e.target.value)} required />
          </div>
          <div className="form-group">
            <label>Email</label>
            <input type="email" value={form.email} onChange={e => set('email', e.target.value)} required />
          </div>
          <div className="form-grid">
            <div className="form-group">
              <label>Department</label>
              <select value={form.department} onChange={e => set('department', e.target.value)}>
                {DEPARTMENTS.map(department => (
                  <option key={department} value={department}>{department}</option>
                ))}
              </select>
            </div>
            <div className="form-group">
              <label>Designation</label>
              <input value={form.designation} onChange={e => set('designation', e.target.value)} />
            </div>
          </div>
          <div className="form-grid">
            <div className="form-group">
              <label>Password</label>
              <input type="password" value={form.password} onChange={e => set('password', e.target.value)} required />
            </div>
            <div className="form-group">
              <label>Confirm Password</label>
              <input type="password" value={form.confirmPassword} onChange={e => set('confirmPassword', e.target.value)} required />
            </div>
          </div>
          <button type="submit" className="btn btn-primary" style={{ width: '100%', justifyContent: 'center', marginTop: 8 }} disabled={loading}>
            {loading ? 'Creating account...' : 'Register'}
          </button>
        </form>
        <p className="auth-switch">
          Already have an account? <Link to="/login">Sign in</Link>
        </p>
      </div>
    </div>
  );
}
