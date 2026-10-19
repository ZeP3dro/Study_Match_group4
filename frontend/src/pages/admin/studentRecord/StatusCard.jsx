import AccountStateForm from '../../../components/AccountStateForm';
import { useState } from 'react';
import Alert from '../../../components/Alert';
import Card from '../../../components/Card';
import Field from '../../../components/Field';
import { STATUS_LABELS } from '../../../utils/format';
import { changeStudentStatus } from '../../../services/studentService';

export default function StatusCard({ student, onChange }) {
  const [status, setStatus] = useState(student.status);
  const [error, setError] = useState('');
  const [saving, setSaving] = useState(false);

  async function save(event) {
    event.preventDefault();
    setSaving(true);
    setError('');
    try {
      await changeStudentStatus(student.id, status);
      onChange();
    } catch (error) {
      setError(error.message);
    } finally {
      setSaving(false);
    }
  }

  return (
    <Card title="Estado do estudante">
      <form className="form" onSubmit={save}>
        <Field label="Estado académico">
          <select
            className="select"
            value={status}
            onChange={(event) => setStatus(event.target.value)}
          >
            {Object.entries(STATUS_LABELS).map(([value, label]) => (
              <option
                key={value}
                value={value}
                disabled={student.status === 'GRADUATED' && value === 'ACTIVE'}
              >
                {label}
              </option>
            ))}
          </select>
        </Field>
        <p className="small muted">
          Só estudantes ativos se podem inscrever em UC. Um estudante diplomado não pode voltar a
          Ativo.
        </p>
        {error && <Alert type="danger">{error}</Alert>}
        <div>
          <button className="button" disabled={saving || status === student.status}>
            {saving ? 'A guardar…' : 'Guardar estado'}
          </button>
        </div>
      </form>
      <hr className="divider" />
      <AccountStateForm key={String(student.active)} account={student} onChange={onChange} />
    </Card>
  );
}
