import { useState } from 'react';
import Alert from '../../components/Alert';
import Card from '../../components/Card';
import Field from '../../components/Field';
import { assignKnowledgeArea } from '../../services/studyPlanService';

/**
 * UC13: assign, replace or remove the knowledge area of a course unit.
 */
export default function AreaAssignmentForm({ areas, courseUnits, onSaved }) {
  const [courseUnitId, setCourseUnitId] = useState('');
  const [knowledgeAreaId, setKnowledgeAreaId] = useState('');
  const [message, setMessage] = useState(null);

  const courseUnit = courseUnits.find((item) => item.id === Number(courseUnitId));
  const replacesArea =
    courseUnit?.knowledgeAreaId && String(courseUnit.knowledgeAreaId) !== String(knowledgeAreaId);

  function chooseCourseUnit(id) {
    setCourseUnitId(id);
    setMessage(null);
    const chosen = courseUnits.find((item) => item.id === Number(id));
    setKnowledgeAreaId(chosen?.knowledgeAreaId ?? '');
  }

  async function handleSubmit(event) {
    event.preventDefault();
    if (
      replacesArea &&
      !window.confirm('Confirmas a substituição ou remoção da área atual desta UC?')
    )
      return;
    try {
      await assignKnowledgeArea(
        courseUnit.id,
        knowledgeAreaId === '' ? null : Number(knowledgeAreaId)
      );
      setMessage({
        type: 'success',
        text: 'Associação guardada. Os perfis dos estudantes já usam a nova área.'
      });
      onSaved();
    } catch (saveError) {
      setMessage({ type: 'danger', text: saveError.message });
    }
  }

  return (
    <Card title="Associar área a uma UC">
      <form className="form" onSubmit={handleSubmit}>
        <Field label="Unidade curricular">
          <select
            className="select"
            value={courseUnitId}
            onChange={(event) => chooseCourseUnit(event.target.value)}
          >
            <option value="">Escolhe…</option>
            {courseUnits.map((item) => (
              <option key={item.id} value={item.id}>
                {item.name}
              </option>
            ))}
          </select>
        </Field>
        <Field label="Área de conhecimento">
          <select
            className="select"
            value={knowledgeAreaId}
            disabled={!courseUnit}
            onChange={(event) => setKnowledgeAreaId(event.target.value)}
          >
            <option value="">— Sem área</option>
            {areas.map((area) => (
              <option key={area.id} value={area.id}>
                {area.name}
              </option>
            ))}
          </select>
        </Field>
        <p className="small muted">
          Cada UC pertence a no máximo uma área. «Sem área» remove a associação.
        </p>

        {replacesArea && (
          <Alert type="warning" title="Substituir a área atual?">
            {courseUnit.name} está associada a {courseUnit.knowledgeAreaName}. Ao guardar, a
            associação é substituída e o perfil dos estudantes com nota nesta UC é recalculado.
          </Alert>
        )}
        {message && <Alert type={message.type}>{message.text}</Alert>}

        <div>
          <button type="submit" className="button button--primary" disabled={!courseUnit}>
            Guardar associação
          </button>
        </div>
      </form>
    </Card>
  );
}
