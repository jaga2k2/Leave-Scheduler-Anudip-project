import React, { useEffect, useState } from 'react';
import { getLeaveSummary } from '../services/api';

export default function Reports() {
  const [data, setData] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    getLeaveSummary().then(r => { setData(r.data); setLoading(false); }).catch(() => setLoading(false));
  }, []);

  const totalLeaves = data.reduce((s, r) => s + r.totalLeavesTaken, 0);
  const totalPending = data.reduce((s, r) => s + r.pendingCount, 0);

  return (
    <div>
      <div className="page-header">
        <h1>Leave Reports</h1>
        <p>Summary of leave usage across your organization</p>
      </div>

      <div className="stats-grid" style={{marginBottom:24}}>
        <div className="stat-card blue">
          <div className="stat-label">Total Employees</div>
          <div className="stat-value" style={{color:'var(--accent)'}}>{data.length}</div>
        </div>
        <div className="stat-card yellow">
          <div className="stat-label">Total Leaves Taken</div>
          <div className="stat-value" style={{color:'var(--yellow)'}}>{totalLeaves}</div>
          <div className="stat-sub">days this year</div>
        </div>
        <div className="stat-card purple">
          <div className="stat-label">Pending Approvals</div>
          <div className="stat-value" style={{color:'var(--accent2)'}}>{totalPending}</div>
        </div>
      </div>

      <div className="card">
        <div className="section-title">Employee Leave Summary</div>
        {loading ? <p className="text-muted">Loading...</p> : data.length === 0 ? (
          <div className="empty-state">No data available.</div>
        ) : (
          <div className="table-wrap">
            <table>
              <thead>
                <tr>
                  <th>Employee</th>
                  <th>Department</th>
                  <th>Annual</th>
                  <th>Sick</th>
                  <th>Casual</th>
                  <th>Total Taken</th>
                  <th>Pending</th>
                </tr>
              </thead>
              <tbody>
                {data.map((r, i) => (
                  <tr key={i}>
                    <td><strong>{r.employeeName}</strong></td>
                    <td className="text-muted">{r.department}</td>
                    <td>{r.annualTaken}</td>
                    <td>{r.sickTaken}</td>
                    <td>{r.casualTaken}</td>
                    <td><strong>{r.totalLeavesTaken}</strong></td>
                    <td>
                      {r.pendingCount > 0
                        ? <span className="badge badge-pending">{r.pendingCount} pending</span>
                        : <span className="text-muted">-</span>
                      }
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
