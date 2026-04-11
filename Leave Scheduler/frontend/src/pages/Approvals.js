import React, { useEffect, useState } from 'react';
import toast from 'react-hot-toast';
import { getPendingApprovals, actOnLeave } from '../services/api';

export default function Approvals() {
  const [leaves, setLeaves] = useState([]);
  const [loading, setLoading] = useState(true);
  const [modal, setModal] = useState(null);
  const [remarks, setRemarks] = useState('');

  const fetchLeaves = () => {
    getPendingApprovals().then(r => {
      setLeaves(r.data);
      setLoading(false);
    }).catch(() => setLoading(false));
  };

  useEffect(() => {
    fetchLeaves();
  }, []);

  const openModal = (leave, action) => {
    setModal({ leave, action });
    setRemarks('');
  };

  const closeModal = () => {
    setModal(null);
    setRemarks('');
  };

  const handleAction = async () => {
    try {
      await actOnLeave(modal.leave.id, { action: modal.action, remarks });
      toast.success(`Leave ${modal.action.toLowerCase()}d successfully`);
      closeModal();
      fetchLeaves();
    } catch (err) {
      toast.error(err.response?.data?.message || 'Action failed');
    }
  };

  return (
    <div>
      <div className="page-header">
        <h1>Pending Approvals</h1>
        <p>Review and action leave requests from your team</p>
      </div>
      <div className="card">
        {loading ? <p className="text-muted">Loading...</p> : leaves.length === 0 ? (
          <div className="empty-state">
            <svg fill="none" viewBox="0 0 24 24" stroke="currentColor"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth={1.5} d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z" /></svg>
            No pending approvals. All caught up!
          </div>
        ) : (
          <div className="table-wrap">
            <table>
              <thead><tr>
                <th>Employee</th><th>Dept</th><th>Type</th><th>From</th><th>To</th><th>Days</th><th>Reason</th><th>Applied</th><th>Actions</th>
              </tr></thead>
              <tbody>
                {leaves.map(l => (
                  <tr key={l.id}>
                    <td><strong>{l.employeeName}</strong><br /><span className="text-muted">{l.employeeId}</span></td>
                    <td>{l.department}</td>
                    <td>{l.leaveType}</td>
                    <td>{l.fromDate}</td>
                    <td>{l.toDate}</td>
                    <td>{l.numberOfDays}</td>
                    <td style={{ maxWidth: 160, whiteSpace: 'nowrap', overflow: 'hidden', textOverflow: 'ellipsis' }}>{l.reason}</td>
                    <td className="text-muted">{l.appliedAt?.split('T')[0]}</td>
                    <td>
                      <div className="flex gap-2">
                        <button className="btn btn-success btn-sm" onClick={() => openModal(l, 'APPROVE')}>Approve</button>
                        <button className="btn btn-danger btn-sm" onClick={() => openModal(l, 'REJECT')}>Reject</button>
                      </div>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}
      </div>

      {modal && (
        <div className="modal-overlay" onClick={closeModal}>
          <div className="modal" onClick={e => e.stopPropagation()}>
            <div className="modal-header">
              <h2>{modal.action === 'APPROVE' ? 'Approve Leave' : 'Reject Leave'}</h2>
              <button className="modal-close" onClick={closeModal}>x</button>
            </div>
            <div className="modal-body">
              <p className="text-muted" style={{ marginBottom: 16 }}>
                {modal.action === 'APPROVE' ? 'Approve' : 'Reject'} leave for <strong>{modal.leave.employeeName}</strong> ({modal.leave.fromDate} to {modal.leave.toDate}, {modal.leave.numberOfDays} days)
              </p>
              <div className="form-group">
                <label>Remarks {modal.action === 'REJECT' ? '(required)' : '(optional)'}</label>
                <textarea value={remarks} onChange={e => setRemarks(e.target.value)} placeholder="Add your remarks..." />
              </div>
            </div>
            <div className="modal-footer">
              <button className="btn btn-ghost" onClick={closeModal}>Cancel</button>
              <button
                className={`btn ${modal.action === 'APPROVE' ? 'btn-success' : 'btn-danger'}`}
                onClick={handleAction}
                disabled={modal.action === 'REJECT' && !remarks.trim()}
              >
                Confirm {modal.action === 'APPROVE' ? 'Approval' : 'Rejection'}
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}
