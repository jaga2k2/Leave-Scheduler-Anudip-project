import React, { useState } from 'react';
import toast from 'react-hot-toast';
import { changePassword } from '../services/api';

export default function ChangePassword() {
  const [form, setForm] = useState({ oldPassword: '', newPassword: '', confirmPassword: '' });
  const [loading, setLoading] = useState(false);

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (form.newPassword !== form.confirmPassword) {
      toast.error('New passwords do not match');
      return;
    }
    if (form.newPassword.length < 6) {
      toast.error('Password must be at least 6 characters');
      return;
    }
    setLoading(true);
    try {
      await changePassword({ oldPassword: form.oldPassword, newPassword: form.newPassword });
      toast.success('Password changed successfully');
      setForm({ oldPassword: '', newPassword: '', confirmPassword: '' });
    } catch (err) {
      toast.error(err.response?.data?.message || 'Failed to change password');
    } finally {
      setLoading(false);
    }
  };

  const set = (k, v) => setForm(f => ({...f, [k]: v}));

  return (
    <div>
      <div className="page-header">
        <h1>Change Password</h1>
        <p>Update your account password</p>
      </div>
      <div className="card" style={{maxWidth:440}}>
        <form onSubmit={handleSubmit}>
          <div className="form-group">
            <label>Current Password</label>
            <input
              type="password"
              value={form.oldPassword}
              onChange={e => set('oldPassword', e.target.value)}
              placeholder="Enter current password"
              required
            />
          </div>
          <div className="form-group">
            <label>New Password</label>
            <input
              type="password"
              value={form.newPassword}
              onChange={e => set('newPassword', e.target.value)}
              placeholder="Enter new password"
              required
            />
          </div>
          <div className="form-group">
            <label>Confirm New Password</label>
            <input
              type="password"
              value={form.confirmPassword}
              onChange={e => set('confirmPassword', e.target.value)}
              placeholder="Re-enter new password"
              required
            />
          </div>
          <button type="submit" className="btn btn-primary" disabled={loading}>
            {loading ? 'Updating...' : 'Update Password'}
          </button>
        </form>
      </div>
    </div>
  );
}
