import React, { useEffect, useState } from 'react';
import toast from 'react-hot-toast';
import { getMyLeaves, withdrawLeave } from '../services/api';

const STATUS_CLASS = {
  PENDING: 'badge-pending', APPROVED: 'badge-approved',
  REJECTED: 'badge-rejected', WITHDRAWN: 'badge-withdrawn', AUTO_APPROVED: 'badge-auto_approved'
};

export default function MyLeaves() {
  const [leaves, setLeaves] = useState([]);
  const [loading, setLoading] = useState(true);

  const fetchLeaves = () => {
    getMyLeaves().then(r => { setLeaves(r.data); setLoading(false); }).catch(() => setLoading(false));
  };

  useEffect(() => { fetchLeaves(); }, []);

  const handleWithdraw = async (id) => {
    if (!window.confirm('Withdraw this leave application?')) return;
    try {
      await withdrawLeave(id);
      toast.success('Leave withdrawn');
      fetchLeaves();
    } catch (err) {
      toast.error(err.response?.data?.message || 'Failed to withdraw');
    }
  };

  return (
    <div>
      <div className="page-header">
        <h1>My Leave Applications</h1>
        <p>Track all your past and pending leave requests</p>
      </div>
      <div className="card">
        {loading ? <p className="text-muted">Loading...</p> : leaves.length === 0 ? (
          <div className="empty-state">No leave applications found.</div>
        ) : (
          <div className="table-wrap">
            <table>
              <thead><tr>
                <th>Type</th><th>From</th><th>To</th><th>Days</th><th>Reason</th><th>Status</th><th>Remarks</th><th>Applied</th><th>Action</th>
              </tr></thead>
              <tbody>
                {leaves.map(l => (
                  <tr key={l.id}>
                    <td><strong>{l.leaveType}</strong></td>
                    <td>{l.fromDate}</td>
                    <td>{l.toDate}</td>
                    <td>{l.numberOfDays}</td>
                    <td style={{maxWidth:180, whiteSpace:'nowrap', overflow:'hidden', textOverflow:'ellipsis'}}>{l.reason}</td>
                    <td><span className={`badge ${STATUS_CLASS[l.status]}`}>{l.status}</span></td>
                    <td className="text-muted">{l.managerRemarks || '-'}</td>
                    <td className="text-muted">{l.appliedAt?.split('T')[0]}</td>
                    <td>
                      {(l.status === 'PENDING' || l.status === 'APPROVED') && !l.withdrawn && (
                        <button className="btn btn-danger btn-sm" onClick={() => handleWithdraw(l.id)}>Withdraw</button>
                      )}
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}
      </div>
    </div>
  );
}
