import { useState } from 'react';
import { recordGrade } from '../../../services/academicRecordService';
export default function GradeForm({ studentId, enrolment, onChange, onError }) {
  const [grade, setGrade] = useState(enrolment.grade ?? '');
  const [saving, setSaving] = useState(false);
  async function save(event) {
    event.preventDefault();
    if (
      enrolment.grade !== null &&
      Number(grade) < enrolment.grade &&
      !window.confirm(
        'A nova nota é inferior à registada. Confirmas que estás a corrigir uma nota incorreta?'
      )
    )
      return;
    setSaving(true);
    onError('');
    try {
      await recordGrade(studentId, enrolment.id, Number(grade));
      onChange();
    } catch (error) {
      onError(error.message);
    } finally {
      setSaving(false);
    }
  }
  return (
    <form className="form-actions" onSubmit={save}>
      <input
        className="input"
        style={{ width: 85 }}
        aria-label={`Nota de ${enrolment.courseUnit.name}`}
        required
        type="number"
        min="0"
        max="20"
        step="0.1"
        value={grade}
        onChange={(e) => setGrade(e.target.value)}
      />
      <button className="button" disabled={saving}>
        {saving ? 'A guardar…' : enrolment.grade === null ? 'Guardar nota' : 'Corrigir nota'}
      </button>
    </form>
  );
}
