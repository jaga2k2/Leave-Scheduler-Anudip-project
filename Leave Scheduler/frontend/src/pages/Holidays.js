import React, { useEffect, useState } from 'react';
import toast from 'react-hot-toast';
import { getHolidays, createHoliday, deleteHoliday } from '../services/api';
import { useAuth } from '../context/AuthContext';

const MONTHS = ['Jan','Feb','Mar','Apr','May','Jun','Jul','Aug','Sep','Oct','Nov','Dec'];

export default function Holidays() {
  const { user } = useAuth();
  const isAdmin = user?.role === 'ADMIN';
  const [holidays, setHolidays] = useState([]);
  const [loading, setLoading] = useState(true);
  const [showForm, setShowForm] = useState(false);
  const [form, setForm] = useState({ name: '', date: '', description: '', type: 'NATIONAL' });

  const fetch = () => {
    getHolidays().then(r => { setHolidays(r.data); setLoading(false); }).catch(() => setLoading(false));
  };
  useEffect(() => { fetch(); }, []);

  const handleAdd = async (e) => {
    e.preventDefault();
    try {
      await createHoliday(form);
      toast.success('Holiday added');
      setShowForm(false);
      setForm({ name: '', date: '', description: '', type: 'NATIONAL' });
      fetch();
    } catch (err) {
      toast.error('Failed to add holiday');
    }
  };

  const handleDelete = async (id) => {
    if (!window.confirm('Delete this holiday?')) return;
    try {
      await deleteHoliday(id);
      toast.success('Holiday deleted');
      fetch();
    } catch {
      toast.error('Failed to delete');
    }
  };

  // Group by month
  const grouped = holidays.reduce((acc, h) => {
    const month = parseInt(h.date.split('-')[1]) - 1;
    if (!acc[month]) acc[month] = [];
    acc[month].push(h);
    return acc;
  }, {});

  const TYPE_COLOR = { NATIONAL: 'var(--accent)', OPTIONAL: 'var(--yellow)', COMPANY: 'var(--accent2)' };

  return (
    <div>
      <div className="page-header flex-between">
        <div>
          <h1>Public Holidays</h1>
          <p>Official holiday calendar for {new Date().getFullYear()}</p>
        </div>
        {isAdmin && (
          <button className="btn btn-primary" onClick={() => setShowForm(!showForm)}>
            + Add Holiday
          </button>
        )}
      </div>

      {showForm && (
        <div className="card" style={{maxWidth:500, marginBottom:24}}>
          <div className="section-title">Add New Holiday</div>
          <form onSubmit={handleAdd}>
            <div className="form-group">
              <label>Holiday Name</label>
              <input value={form.name} onChange={e => setForm(f=>({...f,name:e.target.value}))} placeholder="e.g. Republic Day" required />
            </div>
            <div className="form-grid">
              <div className="form-group">
                <label>Date</label>
                <input type="date" value={form.date} onChange={e => setForm(f=>({...f,date:e.target.value}))} required />
              </div>
              <div className="form-group">
                <label>Type</label>
                <select value={form.type} onChange={e => setForm(f=>({...f,type:e.target.value}))}>
                  <option value="NATIONAL">National</option>
                  <option value="OPTIONAL">Optional</option>
                  <option value="COMPANY">Company</option>
                </select>
              </div>
            </div>
            <div className="form-group">
              <label>Description (optional)</label>
              <input value={form.description} onChange={e => setForm(f=>({...f,description:e.target.value}))} placeholder="Brief description" />
            </div>
            <div className="flex gap-2">
              <button type="submit" className="btn btn-primary">Add Holiday</button>
              <button type="button" className="btn btn-ghost" onClick={() => setShowForm(false)}>Cancel</button>
            </div>
          </form>
        </div>
      )}

      {loading ? <p className="text-muted">Loading...</p> : holidays.length === 0 ? (
        <div className="card"><div className="empty-state">No holidays found for this year.</div></div>
      ) : (
        <div style={{display:'flex', flexDirection:'column', gap:20}}>
          {Object.entries(grouped).sort(([a],[b]) => a-b).map(([month, items]) => (
            <div key={month} className="card">
              <div className="section-title" style={{color:'var(--text-muted)', fontSize:13, textTransform:'uppercase', letterSpacing:'0.5px'}}>
                {MONTHS[parseInt(month)]}
              </div>
              <div className="holiday-list">
                {items.map(h => (
                  <div key={h.id} className="holiday-item">
                    <div style={{
                      width:8, height:8, borderRadius:'50%',
                      background: TYPE_COLOR[h.type] || 'var(--text-muted)',
                      flexShrink:0
                    }}/>
                    <div className="holiday-date">
                      {new Date(h.date).toLocaleDateString('en-IN',{day:'2-digit',month:'short'})}
                    </div>
                    <div style={{flex:1}}>
                      <div className="holiday-name">{h.name}</div>
                      {h.description && <div className="text-muted">{h.description}</div>}
                    </div>
                    <span className="badge" style={{background:'rgba(255,255,255,0.05)', color:'var(--text-muted)'}}>{h.type}</span>
                    {isAdmin && (
                      <button className="btn btn-danger btn-sm" onClick={() => handleDelete(h.id)}>Delete</button>
                    )}
                  </div>
                ))}
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
}
