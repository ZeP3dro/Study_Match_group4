import { useState } from 'react';
import Alert from '../../components/Alert';
import Card from '../../components/Card';
import Field from '../../components/Field';
import { registerTeacher, updateTeacher } from '../../services/teacherService';

export default function TeacherForm({ teacher, onSaved, onCancel }) {
  // Ao editar, preenche o formulário com os dados do docente.
  const [name, setName] = useState(teacher?.name || '');
  const [department, setDepartment] = useState(teacher?.department || '');
  const [error, setError] = useState('');
  const [saving, setSaving] = useState(false);

  async function save(event) {
    event.preventDefault();
    setSaving(true);
    setError('');
    try {
      const form = { name, department };
      // Guarda as alterações ou regista um novo docente.
      const saved = teacher ? await updateTeacher(teacher.id, form) : await registerTeacher(form);
      onSaved(saved);
    } catch (error) {
      setError(error.message);
    } finally {
      setSaving(false);
    }
  }

  return (
    <Card title={teacher ? 'Editar docente' : 'Registar docente'}>
      <form className="form" onSubmit={save}>
        <Field label="Nome completo">
          <input
            className="input"
            required
            maxLength={120}
            value={name}
            onChange={(event) => setName(event.target.value)}
          />
        </Field>
        <Field label="Departamento">
          <input
            className="input"
            required
            maxLength={120}
            value={department}
            onChange={(event) => setDepartment(event.target.value)}
          />
        </Field>
        {!teacher && (
          <p className="small muted">
            O número segue a sequência única 1, 2, 3… O email institucional e a palavra-passe
            inicial são gerados automaticamente.
          </p>
        )}
        {error && <Alert type="danger">{error}</Alert>}
        <div className="form-actions">
          <button className="button button--primary" disabled={saving}>
            {saving ? 'A guardar…' : teacher ? 'Guardar alterações' : 'Criar docente'}
          </button>
          <button type="button" className="button" disabled={saving} onClick={onCancel}>
            Cancelar
          </button>
        </div>
      </form>
    </Card>
  );
}
