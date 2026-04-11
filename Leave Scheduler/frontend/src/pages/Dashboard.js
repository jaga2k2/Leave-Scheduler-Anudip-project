import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { getMyBalance, getMyLeaves } from '../services/api';
import { useAuth } from '../context/AuthContext';

const STATUS_CLASS = {
  PENDING: 'badge-pending',
  APPROVED: 'badge-approved',
  REJECTED: 'badge-rejected',
  WITHDRAWN: 'badge-withdrawn',
  AUTO_APPROVED: 'badge-auto_approved'
};

export default function Dashboard() {
  const { user } = useAuth();
  const navigate = useNavigate();
  const [balance, setBalance] = useState(null);
  const [leaves, setLeaves] = useState([]);

  useEffect(() => {
    getMyBalance().then(r => setBalance(r.data)).catch(() => {});
    getMyLeaves().then(r => setLeaves(r.data)).catch(() => {});
  }, []);

  const recent = leaves.slice(0, 5);
  const pending = leaves.filter(l => l.status === 'PENDING').length;

  return (
    <div>
      <div className="page-header">
        <h1>Welcome back, {user?.fullName?.split(' ')[0]}</h1>
        <p>Here's your leave overview for {new Date().getFullYear()}</p>
      </div>

      {balance && (
        <div className="stats-grid">
          <div className="stat-card blue">
            <div className="stat-label">Annual Leave</div>
            <div className="stat-value" style={{ color: 'var(--accent)' }}>{balance.annualBalance}</div>
            <div className="stat-sub">{balance.annualUsed} used of {balance.annualTotal}</div>
            <div className="progress-bar mt-4">
              <div className="progress-fill" style={{ width: `${(balance.annualUsed / balance.annualTotal) * 100}%`, background: 'var(--accent)' }} />
            </div>
          </div>
          <div className="stat-card green">
            <div className="stat-label">Sick Leave</div>
            <div className="stat-value" style={{ color: 'var(--green)' }}>{balance.sickBalance}</div>
            <div className="stat-sub">{balance.sickUsed} used of {balance.sickTotal}</div>
            <div className="progress-bar mt-4">
              <div className="progress-fill" style={{ width: `${(balance.sickUsed / balance.sickTotal) * 100}%`, background: 'var(--green)' }} />
            </div>
          </div>
          <div className="stat-card yellow">
            <div className="stat-label">Casual Leave</div>
            <div className="stat-value" style={{ color: 'var(--yellow)' }}>{balance.casualBalance}</div>
            <div className="stat-sub">{balance.casualUsed} used of {balance.casualTotal}</div>
            <div className="progress-bar mt-4">
              <div className="progress-fill" style={{ width: `${(balance.casualUsed / balance.casualTotal) * 100}%`, background: 'var(--yellow)' }} />
            </div>
          </div>
          <div className="stat-card purple">
            <div className="stat-label">Pending Requests</div>
            <div className="stat-value" style={{ color: 'var(--accent2)' }}>{pending}</div>
            <div className="stat-sub">Awaiting approval</div>
          </div>
        </div>
      )}

      <div className="card">
        <div className="flex-between mb-4">
          <div className="section-title" style={{ marginBottom: 0 }}>Recent Leave Applications</div>
          <button className="btn btn-ghost btn-sm" onClick={() => navigate('/history')}>View All</button>
        </div>
        {recent.length === 0 ? (
          <div className="empty-state">
            <svg fill="none" viewBox="0 0 24 24" stroke="currentColor"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth={1.5} d="M9 5H7a2 2 0 00-2 2v12a2 2 0 002 2h10a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2" /></svg>
            No leave applications yet.
            <div className="mt-4"><button className="btn btn-primary" onClick={() => navigate('/apply')}>Apply for Leave</button></div>
          </div>
        ) : (
          <div className="table-wrap">
            <table>
              <thead><tr>
                <th>Type</th><th>From</th><th>To</th><th>Days</th><th>Status</th>
              </tr></thead>
              <tbody>
                {recent.map(l => (
                  <tr key={l.id}>
                    <td>{l.leaveType}</td>
                    <td>{l.fromDate}</td>
                    <td>{l.toDate}</td>
                    <td>{l.numberOfDays}</td>
                    <td><span className={`badge ${STATUS_CLASS[l.status]}`}>{l.status}</span></td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}
      </div>

      <div style={{ marginTop: 16, textAlign: 'right' }}>
        <button className="btn btn-primary" onClick={() => navigate('/apply')}>
          + Apply for Leave
        </button>
      </div>
    </div>
  );
}
