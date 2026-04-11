import React, { useEffect, useState } from 'react';
import toast from 'react-hot-toast';
import { getUsers, createUser, updateUser, deleteUser } from '../services/api';

const ROLES = ['EMPLOYEE', 'MANAGER', 'BUSINESS_MANAGER', 'MANAGING_DIRECTOR', 'ADMIN'];
const DEPTS = ['ENGINEERING', 'MANAGEMENT', 'HR', 'FINANCE', 'OPERATIONS'];

const emptyForm = {
  username: '',
  password: '',
  email: '',
  firstName: '',
  lastName: '',
  role: 'EMPLOYEE',
  department: 'ENGINEERING',
  designation: '',
  managerId: ''
};

export default function Admin() {
  const [users, setUsers] = useState([]);
  const [loading, setLoading] = useState(true);
  const [showModal, setShowModal] = useState(false);
  const [editing, setEditing] = useState(null);
  const [form, setForm] = useState(emptyForm);

  const fetch = () => {
    getUsers().then(r => {
      setUsers(r.data);
      setLoading(false);
    }).catch(() => setLoading(false));
  };

  useEffect(() => {
    fetch();
  }, []);

  const openCreate = () => {
    setEditing(null);
    setForm(emptyForm);
    setShowModal(true);
  };

  const openEdit = (u) => {
    setEditing(u);
    setForm({
      username: u.username,
      password: '',
      email: u.email,
      firstName: u.firstName,
      lastName: u.lastName,
      role: u.role,
      department: u.department || 'ENGINEERING',
      designation: u.designation || '',
      managerId: u.manager?.id || ''
    });
    setShowModal(true);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      if (editing) {
        const { username, password, ...rest } = form;
        await updateUser(editing.id, rest);
        toast.success('User updated');
      } else {
        await createUser(form);
        toast.success('User created');
      }
      setShowModal(false);
      fetch();
    } catch (err) {
      toast.error(err.response?.data?.message || 'Failed to save user');
    }
  };

  const handleDelete = async (id) => {
    if (!window.confirm('Delete this user? This cannot be undone.')) return;
    try {
      await deleteUser(id);
      toast.success('User deleted');
      fetch();
    } catch {
      toast.error('Failed to delete');
    }
  };

  const set = (k, v) => setForm(f => ({ ...f, [k]: v }));

  return (
    <div>
      <div className="page-header flex-between">
        <div>
          <h1>Admin - User Management</h1>
          <p>Manage all employees and their roles</p>
        </div>
        <button className="btn btn-primary" onClick={openCreate}>+ Add User</button>
      </div>

      <div className="card">
        {loading ? <p className="text-muted">Loading...</p> : users.length === 0 ? (
          <div className="empty-state">No users found.</div>
        ) : (
          <div className="table-wrap">
            <table>
              <thead><tr>
                <th>Name</th><th>Username</th><th>Email</th><th>Role</th><th>Dept</th><th>Manager</th><th>Active</th><th>Actions</th>
              </tr></thead>
              <tbody>
                {users.map(u => (
                  <tr key={u.id}>
                    <td><strong>{u.firstName} {u.lastName}</strong></td>
                    <td className="text-muted">{u.username}</td>
                    <td className="text-muted">{u.email}</td>
                    <td><span className="badge badge-pending" style={{ background: 'rgba(56,189,248,0.1)', color: 'var(--accent)' }}>{u.role}</span></td>
                    <td>{u.department}</td>
                    <td className="text-muted">{u.manager ? `${u.manager.firstName} ${u.manager.lastName}` : '-'}</td>
                    <td>
                      <span className={`badge ${u.active ? 'badge-approved' : 'badge-rejected'}`}>
                        {u.active ? 'Active' : 'Inactive'}
                      </span>
                    </td>
                    <td>
                      <div className="flex gap-2">
                        <button className="btn btn-ghost btn-sm" onClick={() => openEdit(u)}>Edit</button>
                        <button className="btn btn-danger btn-sm" onClick={() => handleDelete(u.id)}>Delete</button>
                      </div>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}
      </div>

      {showModal && (
        <div className="modal-overlay" onClick={() => setShowModal(false)}>
          <div className="modal" onClick={e => e.stopPropagation()}>
            <div className="modal-header">
              <h2>{editing ? 'Edit User' : 'Create New User'}</h2>
              <button className="modal-close" onClick={() => setShowModal(false)}>x</button>
            </div>
            <div className="modal-body">
              <form id="user-form" onSubmit={handleSubmit}>
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
                {!editing && (
                  <div className="form-group">
                    <label>Username</label>
                    <input value={form.username} onChange={e => set('username', e.target.value)} required />
                  </div>
                )}
                <div className="form-group">
                  <label>Email</label>
                  <input type="email" value={form.email} onChange={e => set('email', e.target.value)} required />
                </div>
                {!editing && (
                  <div className="form-group">
                    <label>Password</label>
                    <input type="password" value={form.password} onChange={e => set('password', e.target.value)} required />
                  </div>
                )}
                <div className="form-grid">
                  <div className="form-group">
                    <label>Role</label>
                    <select value={form.role} onChange={e => set('role', e.target.value)}>
                      {ROLES.map(r => <option key={r} value={r}>{r}</option>)}
                    </select>
                  </div>
                  <div className="form-group">
                    <label>Department</label>
                    <select value={form.department} onChange={e => set('department', e.target.value)}>
                      {DEPTS.map(d => <option key={d} value={d}>{d}</option>)}
                    </select>
                  </div>
                </div>
                <div className="form-group">
                  <label>Designation</label>
                  <input value={form.designation} onChange={e => set('designation', e.target.value)} placeholder="e.g. Software Engineer" />
                </div>
                <div className="form-group">
                  <label>Manager (ID)</label>
                  <input value={form.managerId} onChange={e => set('managerId', e.target.value)} placeholder="Manager's user ID (optional)" />
                </div>
              </form>
            </div>
            <div className="modal-footer">
              <button className="btn btn-ghost" onClick={() => setShowModal(false)}>Cancel</button>
              <button type="submit" form="user-form" className="btn btn-primary">
                {editing ? 'Save Changes' : 'Create User'}
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}
