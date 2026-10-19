import { Fragment, useState } from 'react';
import Alert from '../../components/Alert';
import Card from '../../components/Card';
import Field from '../../components/Field';
import Loading from '../../components/Loading';
import PageHeader from '../../components/PageHeader';
import KnowledgeAreaForm from './KnowledgeAreaForm';
import AreaAssignmentForm from './AreaAssignmentForm';
import useLoader from '../../hooks/useLoader';
import { deleteKnowledgeArea, getKnowledgeAreas } from '../../services/knowledgeAreaService';
import { getCourseUnits } from '../../services/studyPlanService';

/**
 * UC12 manage knowledge areas and UC13 assign knowledge area to course unit.
 */
export default function KnowledgeAreasAdminPage() {
  const { data, error, reload } = useLoader(() =>
    Promise.all([getKnowledgeAreas(), getCourseUnits()])
  );
  const [search, setSearch] = useState('');
  const [editingArea, setEditingArea] = useState(null);
  const [openAreaId, setOpenAreaId] = useState(null);
  const [actionError, setActionError] = useState('');

  if (error && !data) return <Alert type="danger">{error}</Alert>;
  if (!data) return <Loading />;

  const [areas, courseUnits] = data;
  const visibleAreas = areas.filter((area) =>
    area.name.toLowerCase().includes(search.trim().toLowerCase())
  );

  async function handleDelete(area) {
    setActionError('');
    if (!window.confirm(`Eliminar a área "${area.name}"?`)) return;
    try {
      await deleteKnowledgeArea(area.id);
      reload();
    } catch (deleteError) {
      setActionError(deleteError.message);
    }
  }

  return (
    <div className="page">
      <PageHeader
        title="Uma estrutura para o conhecimento."
        subtitle="Organiza as competências e associa as unidades curriculares às áreas."
      />

      <div className="toolbar">
        <Field label="Pesquisar áreas">
          <input
            className="input"
            placeholder="Nome da área…"
            value={search}
            onChange={(event) => setSearch(event.target.value)}
          />
        </Field>
      </div>

      <Card>
        {actionError && <Alert type="danger">{actionError}</Alert>}
        <div className="table-wrapper">
          <table className="table">
            <thead>
              <tr>
                <th>Área de conhecimento</th>
                <th className="number">UC associadas</th>
                <th>Ações</th>
              </tr>
            </thead>
            <tbody>
              {visibleAreas.map((area) => (
                <Fragment key={area.id}>
                  <tr>
                    <td>{area.name}</td>
                    <td className="number">{area.courseUnits.length}</td>
                    <td>
                      <span className="identity small">
                        <button
                          type="button"
                          className="button button--link"
                          onClick={() => setEditingArea(area)}
                        >
                          Editar
                        </button>
                        ·
                        <button
                          type="button"
                          className="button button--link"
                          onClick={() => setOpenAreaId(openAreaId === area.id ? null : area.id)}
                        >
                          Ver UC
                        </button>
                        ·
                        <button
                          type="button"
                          className="button button--link button--danger"
                          onClick={() => handleDelete(area)}
                        >
                          Eliminar
                        </button>
                      </span>
                    </td>
                  </tr>
                  {openAreaId === area.id && (
                    <tr className="table__detail">
                      <td colSpan={3}>
                        {area.courseUnits.length === 0
                          ? 'Sem unidades curriculares.'
                          : area.courseUnits.map((courseUnit) => courseUnit.name).join(' · ')}
                      </td>
                    </tr>
                  )}
                </Fragment>
              ))}
            </tbody>
          </table>
        </div>
        <p className="small muted">
          Os nomes das áreas são únicos, independentemente de maiúsculas e espaços.
        </p>
      </Card>

      <div className="grid-2 grid-2--equal">
        <KnowledgeAreaForm
          key={editingArea?.id ?? 'new'}
          area={editingArea}
          onSaved={() => {
            setEditingArea(null);
            reload();
          }}
          onCancel={() => setEditingArea(null)}
        />
        <AreaAssignmentForm areas={areas} courseUnits={courseUnits} onSaved={reload} />
      </div>
    </div>
  );
}
