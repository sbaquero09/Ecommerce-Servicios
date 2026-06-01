import { useState, useEffect, useCallback } from 'react';

// ─── HTTP Method Badge ──────────────────────────────────────────
export function Badge({ method }) {
  return <span className={`badge badge-${method}`}>{method}</span>;
}

// ─── API Response Box ───────────────────────────────────────────
function ApiResponse({ response }) {
  if (!response) return null;
  const isSuccess = response.type === 'success';
  return (
    <div className={`api-response ${response.type}`}>
      <div className="api-response-header">
        <Badge method={response.method} />
        <span>{isSuccess ? '✓' : '✗'} {response.message}</span>
      </div>
      {response.data && (
        <div className="api-response-body">
          {JSON.stringify(response.data, null, 2)}
        </div>
      )}
    </div>
  );
}

// ─── Confirm Delete Dialog ──────────────────────────────────────
function ConfirmDialog({ id, entityName, onConfirm, onCancel }) {
  return (
    <div className="confirm-overlay">
      <div className="confirm-box">
        <h3>⚠ Confirmar eliminación</h3>
        <p>
          ¿Estás seguro de que deseas eliminar el registro con ID{' '}
          <strong>{id}</strong> de <strong>{entityName}</strong>?
          Esta acción no se puede deshacer.
        </p>
        <div className="btn-row">
          <button className="btn btn-delete" onClick={() => onConfirm(id)}>
            Sí, eliminar
          </button>
          <button className="btn btn-ghost" onClick={onCancel}>
            Cancelar
          </button>
        </div>
      </div>
    </div>
  );
}

// ─── Form Field ─────────────────────────────────────────────────
function FormField({ field, value, onChange }) {
  const { key, label, type = 'text', placeholder, required } = field;

  if (type === 'boolean') {
    return (
      <div className="form-field">
        <label className="form-label">
          {label} {required && <span style={{ color: 'var(--accent)' }}>*</span>}
        </label>
        <div style={{ display: 'flex', alignItems: 'center', gap: 8, paddingTop: 4 }}>
          <input
            type="checkbox"
            className="form-input"
            checked={Boolean(value)}
            onChange={e => onChange(key, e.target.checked)}
          />
          <span style={{ fontSize: 12, color: 'var(--text-sec)' }}>
            {Boolean(value) ? 'true' : 'false'}
          </span>
        </div>
      </div>
    );
  }

  if (type === 'select') {
    return (
      <div className="form-field">
        <label className="form-label">
          {label} {required && <span style={{ color: 'var(--accent)' }}>*</span>}
        </label>
        <select
          className="form-select"
          value={value ?? ''}
          onChange={e => onChange(key, e.target.value)}
        >
          <option value="">— seleccionar —</option>
          {field.options.map(o => (
            <option key={o.value} value={o.value}>{o.label}</option>
          ))}
        </select>
      </div>
    );
  }

  return (
    <div className="form-field">
      <label className="form-label">
        {label} {required && <span style={{ color: 'var(--accent)' }}>*</span>}
      </label>
      <input
        type={type === 'decimal' ? 'number' : type === 'integer' ? 'number' : 'text'}
        step={type === 'decimal' ? '0.01' : type === 'integer' ? '1' : undefined}
        className="form-input"
        value={value ?? ''}
        placeholder={placeholder || ''}
        onChange={e => {
          const v = e.target.value;
          if (type === 'decimal') onChange(key, v === '' ? '' : parseFloat(v));
          else if (type === 'integer') onChange(key, v === '' ? '' : parseInt(v, 10));
          else onChange(key, v);
        }}
      />
    </div>
  );
}

// ─── Entity Page Component ──────────────────────────────────────
export default function EntityPage({
  title,
  endpoint,
  api,
  columns,
  createFields,
  updateFields,
  readOnly = false,
  noUpdate = false,
  extraActions,
  infoBanner,
}) {
  const [data,          setData]          = useState([]);
  const [loading,       setLoading]       = useState(false);
  const [searchId,      setSearchId]      = useState('');
  const [searchResult,  setSearchResult]  = useState(null);
  const [formData,      setFormData]      = useState({});
  const [editId,        setEditId]        = useState(null);
  const [showCreate,    setShowCreate]    = useState(false);
  const [apiResponse,   setApiResponse]   = useState(null);
  const [confirmDelete, setConfirmDelete] = useState(null);

  const notify = (type, method, message, responseData) =>
    setApiResponse({ type, method, message, data: responseData });

  // GET ALL
  const loadAll = useCallback(async () => {
    setLoading(true);
    try {
      const result = await api.getAll();
      setData(Array.isArray(result) ? result : []);
      notify('success', 'GET', `${Array.isArray(result) ? result.length : 0} registro(s) cargado(s)`, null);
    } catch (err) {
      notify('error', 'GET', err.message);
    } finally {
      setLoading(false);
    }
  }, [api]);

  useEffect(() => { loadAll(); }, [loadAll]);

  // GET BY ID
  const handleSearch = async () => {
    if (!searchId.trim()) return;
    try {
      const result = await api.getById(searchId.trim());
      setSearchResult(result);
      notify('success', 'GET', `Registro encontrado — ID: ${searchId}`, result);
    } catch (err) {
      setSearchResult(null);
      notify('error', 'GET', err.message);
    }
  };

  // Field change handler
  const setField = (key, val) => setFormData(prev => ({ ...prev, [key]: val }));

  // Open CREATE form
  const openCreate = () => {
    setShowCreate(true);
    setEditId(null);
    setFormData({});
    setApiResponse(null);
  };

  // POST
  const handleCreate = async () => {
    try {
      const result = await api.create(formData);
      notify('success', 'POST', 'Registro creado exitosamente', result);
      setFormData({});
      setShowCreate(false);
      loadAll();
    } catch (err) {
      notify('error', 'POST', err.message);
    }
  };

  // Open EDIT form
  const openEdit = (row) => {
    setEditId(row.id);
    setShowCreate(false);
    setApiResponse(null);
    const fields = updateFields || createFields;
    const initial = {};
    fields.forEach(f => { initial[f.key] = row[f.key] ?? (f.type === 'boolean' ? false : ''); });
    setFormData(initial);
  };

  // PUT
  const handleUpdate = async () => {
    try {
      const result = await api.update(editId, formData);
      notify('success', 'PUT', `ID ${editId} actualizado exitosamente`, result);
      setEditId(null);
      setFormData({});
      loadAll();
    } catch (err) {
      notify('error', 'PUT', err.message);
    }
  };

  // DELETE
  const handleDelete = async (id) => {
    try {
      await api.delete(id);
      notify('success', 'DELETE', `ID ${id} eliminado exitosamente`);
      setConfirmDelete(null);
      if (editId === id) { setEditId(null); setFormData({}); }
      loadAll();
    } catch (err) {
      notify('error', 'DELETE', err.message);
      setConfirmDelete(null);
    }
  };

  const activeFields = editId ? (updateFields || createFields) : createFields;
  const showForm = showCreate || editId !== null;
  const formMode = editId !== null ? 'PUT' : 'POST';

  return (
    <div>
      {/* ── TOP ROW ACTIONS ── */}
      <div style={{ display: 'flex', gap: 10, marginBottom: 20, flexWrap: 'wrap' }}>
        <button className="btn btn-get" onClick={loadAll} disabled={loading}>
          {loading ? <span className="spinner" /> : null}
          <Badge method="GET" /> Cargar todos
        </button>
        {!readOnly && (
          <button className="btn btn-post" onClick={openCreate}>
            <Badge method="POST" /> Nuevo registro
          </button>
        )}
        {extraActions}
      </div>

      {infoBanner && <div className="info-banner">ℹ {infoBanner}</div>}

      {/* ── GET ALL TABLE ── */}
      <div className="section-card">
        <div className="section-header">
          <Badge method="GET" />
          <span className="section-title">Todos los registros</span>
          <span className="section-subtitle">GET {endpoint}/all</span>
        </div>
        <div className="section-body" style={{ padding: 0 }}>
          <div className="table-wrapper">
            <table>
              <thead>
                <tr>
                  {columns.map(c => <th key={c.key}>{c.label}</th>)}
                  {!readOnly && <th>Acciones</th>}
                </tr>
              </thead>
              <tbody>
                {loading ? (
                  <tr className="loading-row">
                    <td colSpan={columns.length + (readOnly ? 0 : 1)}>
                      <span className="spinner" /> Cargando…
                    </td>
                  </tr>
                ) : data.length === 0 ? (
                  <tr>
                    <td colSpan={columns.length + (readOnly ? 0 : 1)}>
                      <div className="empty-state">Sin registros</div>
                    </td>
                  </tr>
                ) : data.map(row => (
                  <tr key={row.id}>
                    {columns.map(c => (
                      <td key={c.key} className={c.key === 'id' ? 'id-cell' : ''}>
                        {c.render ? c.render(row[c.key], row)
                          : row[c.key] === true  ? <span className="tag-true">true</span>
                          : row[c.key] === false ? <span className="tag-false">false</span>
                          : row[c.key] === null || row[c.key] === undefined
                            ? <span style={{ color: 'var(--text-dim)' }}>—</span>
                          : String(row[c.key])}
                      </td>
                    ))}
                    {!readOnly && (
                      <td>
                        <div className="btn-row">
                          {!noUpdate && (
                            <button className="btn btn-put btn-sm" onClick={() => openEdit(row)}>
                              ✎ Editar
                            </button>
                          )}
                          <button className="btn btn-delete btn-sm" onClick={() => setConfirmDelete(row.id)}>
                            ✕ Eliminar
                          </button>
                        </div>
                      </td>
                    )}
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>
      </div>

      {/* ── GET BY ID ── */}
      <div className="section-card">
        <div className="section-header">
          <Badge method="GET" />
          <span className="section-title">Buscar por ID</span>
          <span className="section-subtitle">GET {endpoint}/{'{id}'}</span>
        </div>
        <div className="section-body">
          <div className="search-row">
            <div className="search-input-wrap">
              <label className="form-label">ID del registro</label>
              <input
                type="number"
                className="form-input"
                style={{ width: 160 }}
                placeholder="ej: 1"
                value={searchId}
                onChange={e => setSearchId(e.target.value)}
                onKeyDown={e => e.key === 'Enter' && handleSearch()}
              />
            </div>
            <button className="btn btn-get" onClick={handleSearch} disabled={!searchId.trim()}>
              Buscar
            </button>
            {searchResult && (
              <button className="btn btn-ghost" onClick={() => { setSearchResult(null); setSearchId(''); }}>
                Limpiar
              </button>
            )}
          </div>
          {searchResult && (
            <div className="result-card">
              {Object.entries(searchResult).map(([k, v]) => (
                <div key={k}>
                  <strong>{k}:</strong>{' '}
                  {v === null || v === undefined ? '—' : String(v)}
                </div>
              ))}
            </div>
          )}
        </div>
      </div>

      {/* ── CREATE / EDIT FORM ── */}
      {showForm && (
        <div className="section-card">
          <div className="section-header">
            <Badge method={formMode} />
            <span className="section-title">
              {formMode === 'POST' ? 'Crear nuevo registro' : `Editar registro — ID: ${editId}`}
            </span>
            <span className="section-subtitle">
              {formMode === 'POST' ? `POST ${endpoint}` : `PUT ${endpoint}/${editId}`}
            </span>
          </div>
          <div className="section-body">
            <div className="form-grid">
              {activeFields.map(field => (
                <FormField
                  key={field.key}
                  field={field}
                  value={formData[field.key]}
                  onChange={setField}
                />
              ))}
            </div>
            <div className="btn-row">
              {formMode === 'POST' ? (
                <button className="btn btn-post" onClick={handleCreate}>
                  ＋ Crear registro
                </button>
              ) : (
                <button className="btn btn-put" onClick={handleUpdate}>
                  ✎ Guardar cambios
                </button>
              )}
              <button className="btn btn-ghost" onClick={() => {
                setShowCreate(false); setEditId(null); setFormData({}); setApiResponse(null);
              }}>
                Cancelar
              </button>
            </div>
          </div>
        </div>
      )}

      {/* ── API RESPONSE ── */}
      <ApiResponse response={apiResponse} />

      {/* ── CONFIRM DELETE DIALOG ── */}
      {confirmDelete && (
        <ConfirmDialog
          id={confirmDelete}
          entityName={title}
          onConfirm={handleDelete}
          onCancel={() => setConfirmDelete(null)}
        />
      )}
    </div>
  );
}
