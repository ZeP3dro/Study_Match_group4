import { useState } from 'react';
import Alert from '../../../components/Alert';
import Card from '../../../components/Card';
import Field from '../../../components/Field';
import { updateStudent } from '../../../services/studentService';
export default function IdentityCard({ student, onChange }) {
  const [form, setForm] = useState({
    name: student.name,
    firstEnrolmentYear: student.firstEnrolmentYear,
    entryGrade: student.entryGrade ?? ''
  });
  const [saving, setSaving] = useState(false);
  const [message, setMessage] = useState('');
  const [error, setError] = useState('');
  function change(field, value) {
    setForm({ ...form, [field]: value });
    setMessage('');
  }
  async function save(event) {
    event.preventDefault();
    setSaving(true);
    setError('');
    setMessage('');
    try {
      await updateStudent(student.id, {
        ...form,
        firstEnrolmentYear: Number(form.firstEnrolmentYear),
        entryGrade: form.entryGrade === '' ? null : Number(form.entryGrade)
      });
      setMessage('Dados guardados.');
      onChange();
    } catch (error) {
      setError(error.message);
    } finally {
      setSaving(false);
    }
  }
  return (
    <Card title="Editar dados do estudante">
      <form className="form" onSubmit={save}>
        <Field label="Nome completo">
          <input
            className="input"
            required
            value={form.name}
            onChange={(e) => change('name', e.target.value)}
          />
        </Field>
        <div className="form-row">
          <Field label="Primeira inscrição">
            <input
              className="input"
              required
              type="number"
              min="2000"
              max="9999"
              value={form.firstEnrolmentYear}
              onChange={(e) => change('firstEnrolmentYear', e.target.value)}
            />
          </Field>
          <Field label="Nota de ingresso · opcional">
            <input
              className="input"
              type="number"
              min="0"
              max="200"
              value={form.entryGrade}
              onChange={(e) => change('entryGrade', e.target.value)}
            />
          </Field>
        </div>
        {message && <Alert type="success">{message}</Alert>}
        {error && <Alert type="danger">{error}</Alert>}
        <div>
          <button className="button button--primary" disabled={saving}>
            {saving ? 'A guardar…' : 'Guardar dados'}
          </button>
        </div>
      </form>
    </Card>
  );
}
