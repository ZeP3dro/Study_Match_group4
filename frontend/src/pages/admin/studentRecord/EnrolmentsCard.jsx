import { useState } from 'react';
import Alert from '../../../components/Alert';
import Card from '../../../components/Card';
import Field from '../../../components/Field';
import AcademicHistoryTable from '../../../components/AcademicHistoryTable';
import GradeForm from './GradeForm';
import { enrol } from '../../../services/academicRecordService';

export default function EnrolmentsCard({ student, studentId, enrolments, units, years, onChange }) {
  const [unitId, setUnitId] = useState('');
  const [yearId, setYearId] = useState(
    years.find((year) => year.current)?.id || years[0]?.id || ''
  );
  const [error, setError] = useState('');
  const [saving, setSaving] = useState(false);
  const yearEnrolments = enrolments.filter((item) => item.academicYearId === Number(yearId));
  const firstEcts = yearEnrolments
    .filter((item) => item.courseUnit.semester === 1)
    .reduce((sum, item) => sum + item.courseUnit.ects, 0);
  const secondEcts = yearEnrolments
    .filter((item) => item.courseUnit.semester === 2)
    .reduce((sum, item) => sum + item.courseUnit.ects, 0);
  const canEnrol = student.active && student.status === 'ACTIVE';
  const available = units.filter((unit) => {
    if (enrolments.some((item) => item.courseUnit.id === unit.id && item.passed)) return false;
    return !yearEnrolments.some(
      (item) =>
        item.courseUnit.id === unit.id ||
        (unit.optionGroup &&
          unit.curricularYear === item.courseUnit.curricularYear &&
          unit.optionGroup === item.courseUnit.optionGroup)
    );
  });

  async function add(event) {
    event.preventDefault();
    setSaving(true);
    setError('');
    try {
      await enrol(studentId, Number(unitId), Number(yearId));
      setUnitId('');
      onChange();
    } catch (error) {
      setError(error.message);
    } finally {
      setSaving(false);
    }
  }

  return (
    <Card
      title="Histórico académico"
      subtitle="Inscreve o estudante em UC e regista ou corrige as notas."
    >
      {!canEnrol && (
        <Alert type="warning">
          Só uma conta ativa com estado académico Ativo se pode inscrever.
        </Alert>
      )}
      <form className="form" onSubmit={add}>
        <div className="form-row">
          <Field label="Ano letivo">
            <select
              className="select"
              required
              value={yearId}
              onChange={(event) => {
                setYearId(event.target.value);
                setUnitId('');
              }}
            >
              {years.map((year) => (
                <option value={year.id} key={year.id}>
                  {year.label}
                </option>
              ))}
            </select>
          </Field>
          <Field label="Unidade curricular">
            <select
              className="select"
              required
              disabled={!canEnrol}
              value={unitId}
              onChange={(event) => setUnitId(event.target.value)}
            >
              <option value="">Selecionar UC</option>
              {available.map((unit) => (
                <option value={unit.id} key={unit.id}>
                  {unit.code} · {unit.name} · {unit.ects} ECTS
                </option>
              ))}
            </select>
          </Field>
        </div>
        <div className="form-actions">
          <span className="pill">1.º semestre: {firstEcts} / 42 ECTS</span>
          <span className="pill">2.º semestre: {secondEcts} / 42 ECTS</span>
          <span className="pill">Ano: {firstEcts + secondEcts} / 75 ECTS</span>
        </div>
        <p className="small muted">
          Uma UC reprovada pode ser repetida noutro ano. Uma UC aprovada não pode voltar a ser
          inscrita.
        </p>
        <div>
          <button
            className="button button--primary"
            disabled={saving || !canEnrol || !available.length}
          >
            {saving ? 'A inscrever…' : 'Inscrever em UC'}
          </button>
        </div>
      </form>
      {error && <Alert type="danger">{error}</Alert>}
      <AcademicHistoryTable enrolments={enrolments}>
        {(item) => (
          <GradeForm
            key={`${item.id}-${item.grade}`}
            studentId={studentId}
            enrolment={item}
            onChange={onChange}
            onError={setError}
          />
        )}
      </AcademicHistoryTable>
    </Card>
  );
}
