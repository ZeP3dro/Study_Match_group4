import { useState } from 'react';
import Alert from '../../components/Alert';
import Card from '../../components/Card';
import Loading from '../../components/Loading';
import PageHeader from '../../components/PageHeader';
import CourseUnitForm from './CourseUnitForm';
import useLoader from '../../hooks/useLoader';
import { getKnowledgeAreas } from '../../services/knowledgeAreaService';
import { deleteCourseUnit, getCourseUnits, getStudyPlan } from '../../services/studyPlanService';

/**
 * UC06 manage course units of the study plan.
 */
export default function StudyPlanPage() {
  const { data, error, reload } = useLoader(() =>
    Promise.all([getStudyPlan(), getCourseUnits(), getKnowledgeAreas()])
  );
  const [curricularYear, setCurricularYear] = useState(1);
  const [selectedCourseUnit, setSelectedCourseUnit] = useState(null);
  const [creating, setCreating] = useState(false);
  const [actionError, setActionError] = useState('');

  if (error && !data) return <Alert type="danger">{error}</Alert>;
  if (!data) return <Loading />;

  const [studyPlan, courseUnits, areas] = data;
  const yearUnits = courseUnits.filter(
    (courseUnit) => courseUnit.curricularYear === curricularYear
  );
  const yearEcts = studyPlan.ectsByYear[curricularYear - 1];

  function handleSaved() {
    setSelectedCourseUnit(null);
    setCreating(false);
    reload();
  }

  return (
    <div className="page">
      <PageHeader
        title="O plano por trás de cada percurso."
        subtitle={`${studyPlan.programmeName} · ${studyPlan.durationYears} anos · ${studyPlan.totalEcts} ECTS`}
      />

      <div className="toolbar">
        {studyPlan.ectsByYear.map((ects, index) => (
          <button
            key={index}
            type="button"
            className={`pill pill--button ${curricularYear === index + 1 ? 'pill--active' : ''}`}
            onClick={() => setCurricularYear(index + 1)}
          >
            {index + 1}.º ano · {ects} ECTS
          </button>
        ))}
        <button
          type="button"
          className="button button--primary"
          onClick={() => {
            setSelectedCourseUnit(null);
            setCreating(true);
          }}
        >
          Nova unidade curricular +
        </button>
      </div>

      {actionError && <Alert type="danger">{actionError}</Alert>}
      <div className="grid-2">
        <div className="stack">
          {[1, 2].map((semester) => {
            const semesterUnits = yearUnits.filter(
              (courseUnit) => courseUnit.semester === semester
            );
            const countedGroups = new Set();
            const semesterEcts = semesterUnits.reduce((sum, unit) => {
              if (unit.type === 'OPTIONAL') {
                if (countedGroups.has(unit.optionGroup)) return sum;
                countedGroups.add(unit.optionGroup);
              }
              return sum + unit.ects;
            }, 0);
            return (
              <Card key={semester} title={`${curricularYear}.º ano · ${semester}.º semestre`}>
                <div className="table-wrapper">
                  <table className="table">
                    <thead>
                      <tr>
                        <th>Unidade curricular</th>
                        <th className="number">ECTS</th>
                        <th>Área</th>
                        <th>Ações</th>
                      </tr>
                    </thead>
                    <tbody>
                      {semesterUnits.map((courseUnit) => (
                        <tr
                          key={courseUnit.id}
                          className={`table__row--clickable ${selectedCourseUnit?.id === courseUnit.id ? 'table__row--selected' : ''}`}
                          onClick={() => {
                            setCreating(false);
                            setSelectedCourseUnit(courseUnit);
                          }}
                        >
                          <td>
                            {courseUnit.name}
                            <div className="small muted">{courseUnit.code}</div>
                          </td>
                          <td className="number">
                            {courseUnit.ects}
                            {courseUnit.optionGroup && (
                              <div className="small muted">{courseUnit.optionGroup}</div>
                            )}
                          </td>
                          <td>
                            {courseUnit.knowledgeAreaName || (
                              <span className="muted">Sem área</span>
                            )}
                          </td>
                          <td>
                            <button
                              className="button button--link button--danger"
                              onClick={async (event) => {
                                event.stopPropagation();
                                if (!window.confirm(`Eliminar a UC "${courseUnit.name}"?`)) return;
                                setActionError('');
                                try {
                                  await deleteCourseUnit(courseUnit.id);
                                  setSelectedCourseUnit(null);
                                  reload();
                                } catch (error) {
                                  setActionError(error.message);
                                }
                              }}
                            >
                              Eliminar
                            </button>
                          </td>
                        </tr>
                      ))}
                    </tbody>
                  </table>
                </div>
                <p className="small muted">
                  {semesterEcts} ECTS neste semestre · {yearEcts} /{' '}
                  {studyPlan.maxEctsPerCurricularYear} ECTS no ano curricular
                </p>
              </Card>
            );
          })}
        </div>

        {creating || selectedCourseUnit ? (
          <CourseUnitForm
            key={selectedCourseUnit?.id ?? 'new'}
            courseUnit={selectedCourseUnit}
            defaultCurricularYear={curricularYear}
            areas={areas}
            onSaved={handleSaved}
            onCancel={() => {
              setSelectedCourseUnit(null);
              setCreating(false);
            }}
          />
        ) : (
          <Card title="Editar unidade curricular">
            <p className="muted">
              Seleciona uma unidade curricular para a editar, ou cria uma nova.
            </p>
          </Card>
        )}
      </div>
    </div>
  );
}
