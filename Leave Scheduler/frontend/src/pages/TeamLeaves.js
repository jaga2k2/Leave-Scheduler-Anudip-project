import React, { useEffect, useState } from 'react';
import { getTeamLeaves } from '../services/api';

const STATUS_CLASS = {
  PENDING: 'badge-pending',
  APPROVED: 'badge-approved',
  REJECTED: 'badge-rejected',
  WITHDRAWN: 'badge-withdrawn',
  AUTO_APPROVED: 'badge-auto_approved'
};

export default function TeamLeaves() {
  const [leaves, setLeaves] = useState([]);
  const [loading, setLoading] = useState(true);
  const [filter, setFilter] = useState('ALL');

  useEffect(() => {
    getTeamLeaves().then(r => {
      setLeaves(r.data);
      setLoading(false);
    }).catch(() => setLoading(false));
  }, []);

  const filtered = filter === 'ALL' ? leaves : leaves.filter(l => l.status === filter);

  return (
    <div>
      <div className="page-header">
        <h1>Team Leaves</h1>
        <p>Overview of all leave applications from your team</p>
      </div>
      <div className="card">
        <div className="flex gap-2 mb-4">
          {['ALL', 'PENDING', 'APPROVED', 'REJECTED', 'WITHDRAWN'].map(s => (
            <button key={s} className={`btn btn-sm ${filter === s ? 'btn-primary' : 'btn-ghost'}`} onClick={() => setFilter(s)}>{s}</button>
          ))}
        </div>
        {loading ? <p className="text-muted">Loading...</p> : filtered.length === 0 ? (
          <div className="empty-state">No leaves found.</div>
        ) : (
          <div className="table-wrap">
            <table>
              <thead><tr>
                <th>Employee</th><th>Type</th><th>From</th><th>To</th><th>Days</th><th>Status</th><th>Remarks</th>
              </tr></thead>
              <tbody>
                {filtered.map(l => (
                  <tr key={l.id}>
                    <td><strong>{l.employeeName}</strong><br /><span className="text-muted">{l.department}</span></td>
                    <td>{l.leaveType}</td>
                    <td>{l.fromDate}</td>
                    <td>{l.toDate}</td>
                    <td>{l.numberOfDays}</td>
                    <td><span className={`badge ${STATUS_CLASS[l.status]}`}>{l.status}</span></td>
                    <td className="text-muted">{l.managerRemarks || '-'}</td>
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
