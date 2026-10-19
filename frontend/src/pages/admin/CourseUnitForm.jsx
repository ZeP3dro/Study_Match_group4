import { useState } from 'react';
import Alert from '../../components/Alert';
import Card from '../../components/Card';
import Field from '../../components/Field';
import { createCourseUnit, updateCourseUnit } from '../../services/studyPlanService';

/**
 * Create a new course unit, or edit the selected one (the code cannot change).
 */
export default function CourseUnitForm({
  courseUnit,
  defaultCurricularYear,
  areas,
  onSaved,
  onCancel
}) {
  const isNew = !courseUnit;
  const [form, setForm] = useState({
    code: courseUnit?.code ?? '',
    name: courseUnit?.name ?? '',
    knowledgeAreaId: courseUnit?.knowledgeAreaId ?? '',
    curricularYear: courseUnit?.curricularYear ?? defaultCurricularYear,
    ects: courseUnit?.ects ?? 6,
    semester: courseUnit?.semester ?? 1,
    type: courseUnit?.type ?? 'MANDATORY',
    optionGroup: courseUnit?.optionGroup ?? ''
  });
  const [error, setError] = useState('');
  const [fieldErrors, setFieldErrors] = useState({});

  function updateField(name, value) {
    setForm({ ...form, [name]: value });
  }

  async function handleSubmit(event) {
    event.preventDefault();
    setError('');
    setFieldErrors({});
    const request = {
      ...form,
      ects: Number(form.ects),
      curricularYear: Number(form.curricularYear),
      semester: Number(form.semester),
      knowledgeAreaId: form.knowledgeAreaId === '' ? null : Number(form.knowledgeAreaId),
      optionGroup: form.type === 'OPTIONAL' ? form.optionGroup : null
    };
    try {
      if (isNew) {
        await createCourseUnit(request);
      } else {
        await updateCourseUnit(courseUnit.id, request);
      }
      onSaved();
    } catch (saveError) {
      setError(saveError.message);
      setFieldErrors(saveError.fieldErrors || {});
    }
  }

  return (
    <Card title={isNew ? 'Nova unidade curricular' : 'Editar unidade curricular'}>
      <form className="form" onSubmit={handleSubmit}>
        <Field label="Código" error={fieldErrors.code}>
          <input
            className="input"
            value={form.code}
            disabled={!isNew}
            onChange={(event) => updateField('code', event.target.value)}
          />
        </Field>
        <Field label="Nome" error={fieldErrors.name}>
          <input
            className="input"
            value={form.name}
            onChange={(event) => updateField('name', event.target.value)}
          />
        </Field>
        <Field label="Área de conhecimento">
          <select
            className="select"
            value={form.knowledgeAreaId}
            onChange={(event) => updateField('knowledgeAreaId', event.target.value)}
          >
            <option value="">— Sem área (conta só para a média global)</option>
            {areas.map((area) => (
              <option key={area.id} value={area.id}>
                {area.name}
              </option>
            ))}
          </select>
        </Field>
        <div className="form-row">
          <Field label="Ano curricular" error={fieldErrors.curricularYear}>
            <select
              className="select"
              value={form.curricularYear}
              onChange={(event) => updateField('curricularYear', event.target.value)}
            >
              <option value={1}>1.º</option>
              <option value={2}>2.º</option>
              <option value={3}>3.º</option>
            </select>
          </Field>
          <Field label="ECTS" error={fieldErrors.ects}>
            <input
              className="input"
              type="number"
              value={form.ects}
              onChange={(event) => updateField('ects', event.target.value)}
            />
          </Field>
          <Field label="Semestre">
            <select
              className="select"
              value={form.semester}
              onChange={(event) => updateField('semester', event.target.value)}
            >
              <option value={1}>1.º</option>
              <option value={2}>2.º</option>
            </select>
          </Field>
        </div>
        <Field label="Tipo">
          <select
            className="select"
            value={form.type}
            onChange={(event) => updateField('type', event.target.value)}
          >
            <option value="MANDATORY">Obrigatória</option>
            <option value="OPTIONAL">Opcional</option>
          </select>
        </Field>
        {form.type === 'OPTIONAL' && (
          <Field label="Grupo de opção">
            <input
              className="input"
              required
              maxLength={50}
              placeholder="Ex.: OPCAO-3-2"
              value={form.optionGroup}
              onChange={(event) => updateField('optionGroup', event.target.value)}
            />
            <span className="small muted">
              Alternativas do mesmo grupo têm os mesmos ECTS e semestre. O grupo conta uma vez no
              plano.
            </span>
          </Field>
        )}
        <p className="small muted">
          Código único. ECTS maior que 0. Cada ano curricular soma no máximo 60 ECTS.
        </p>

        {error && <Alert type="danger">{error}</Alert>}

        <div className="form-actions">
          <button type="submit" className="button button--primary">
            {isNew ? 'Criar unidade curricular' : 'Guardar alterações'}
          </button>
          <button type="button" className="button" onClick={onCancel}>
            Cancelar
          </button>
        </div>
      </form>
    </Card>
  );
}
