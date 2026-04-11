import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import toast from 'react-hot-toast';
import { applyLeave } from '../services/api';

export default function ApplyLeave() {
  const navigate = useNavigate();
  const [form, setForm] = useState({
    leaveType: 'ANNUAL',
    fromDate: '',
    toDate: '',
    reason: '',
    addressDuringLeave: '',
    superiorEmail: '',
  });
  const [loading, setLoading] = useState(false);

  const set = (k, v) => setForm(f => ({ ...f, [k]: v }));

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (form.fromDate > form.toDate) {
      toast.error('From date cannot be after To date');
      return;
    }
    setLoading(true);
    try {
      await applyLeave(form);
      toast.success('Leave application submitted!');
      navigate('/history');
    } catch (err) {
      toast.error(err.response?.data?.message || 'Failed to apply');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div>
      <div className="page-header">
        <h1>Apply for Leave</h1>
        <p>Fill in the details to submit your leave request</p>
      </div>
      <div className="card" style={{ maxWidth: 640 }}>
        <form onSubmit={handleSubmit}>
          <div className="form-group">
            <label>Leave Type</label>
            <select value={form.leaveType} onChange={e => set('leaveType', e.target.value)}>
              <option value="ANNUAL">Annual Leave</option>
              <option value="SICK">Sick Leave</option>
              <option value="CASUAL">Casual Leave</option>
              <option value="MATERNITY">Maternity Leave</option>
              <option value="PATERNITY">Paternity Leave</option>
              <option value="UNPAID">Unpaid Leave</option>
            </select>
          </div>
          <div className="form-grid">
            <div className="form-group">
              <label>From Date</label>
              <input type="date" value={form.fromDate} onChange={e => set('fromDate', e.target.value)} required min={new Date().toISOString().split('T')[0]} />
            </div>
            <div className="form-group">
              <label>To Date</label>
              <input type="date" value={form.toDate} onChange={e => set('toDate', e.target.value)} required min={form.fromDate || new Date().toISOString().split('T')[0]} />
            </div>
          </div>
          <div className="form-group">
            <label>Reason for Leave</label>
            <textarea value={form.reason} onChange={e => set('reason', e.target.value)} placeholder="Briefly describe the reason..." required />
          </div>
          <div className="form-group">
            <label>Address During Leave</label>
            <textarea value={form.addressDuringLeave} onChange={e => set('addressDuringLeave', e.target.value)} placeholder="Where can you be reached during leave?" rows={2} />
          </div>
          <div className="form-group">
            <label>Superior's Email (optional)</label>
            <input type="email" value={form.superiorEmail} onChange={e => set('superiorEmail', e.target.value)} placeholder="manager@company.com" />
          </div>
          <div className="flex gap-3" style={{ marginTop: 8 }}>
            <button type="submit" className="btn btn-primary" disabled={loading}>{loading ? 'Submitting...' : 'Submit Application'}</button>
            <button type="button" className="btn btn-ghost" onClick={() => navigate('/history')}>Cancel</button>
          </div>
        </form>
      </div>
    </div>
  );
}
