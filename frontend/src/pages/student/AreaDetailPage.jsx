import { Link, useParams } from 'react-router-dom';
import Alert from '../../components/Alert';
import Card from '../../components/Card';
import Loading from '../../components/Loading';
import PageHeader from '../../components/PageHeader';
import { useAuth } from '../../auth/AuthContext';
import useLoader from '../../hooks/useLoader';
import { getProfile } from '../../services/profileService';
import { SOURCE_LABELS, formatGrade, formatNumber } from '../../utils/format';

export default function AreaDetailPage() {
  const { user } = useAuth();
  const { studentId: routeStudentId, areaId } = useParams();
  const studentId = routeStudentId || user.id;
  const profilePath = routeStudentId ? `/admin/students/${studentId}/profile` : '/profile';
  const { data: profile, error, loading } = useLoader(() => getProfile(studentId), [studentId]);

  if (loading) return <Loading />;
  if (error) return <Alert type="danger">{error}</Alert>;
  const area = profile.areas.find((item) => item.areaId === Number(areaId));
  if (!area) return <Alert type="danger">Área de conhecimento não encontrada.</Alert>;
  const results = area.results;
  const formula = results
    .map((item) => `${formatNumber(item.grade)} × ${item.courseUnit.ects}`)
    .join(' + ');

  return (
    <div className="page">
      <PageHeader
        title={`${area.name} · ${formatGrade(area.value)}`}
        subtitle="Como cada unidade curricular contribui para esta competência."
        actions={<Link to={profilePath}>← Voltar ao perfil</Link>}
      />
      <Card title="Contribuição por unidade curricular">
        {results.length ? (
          <>
            <p className="formula">
              Peso = ECTS da UC ÷ ECTS da área com nota ({area.totalEcts}). Contribuição = melhor
              nota × peso.
            </p>
            <div className="table-wrapper">
              <table className="table">
                <thead>
                  <tr>
                    <th>Unidade curricular</th>
                    <th>Ano letivo</th>
                    <th>ECTS</th>
                    <th>Melhor nota</th>
                    <th>Peso (ECTS)</th>
                    <th>Contribuição</th>
                  </tr>
                </thead>
                <tbody>
                  {results.map((item) => {
                    const weight = item.courseUnit.ects / area.totalEcts;
                    return (
                      <tr key={item.courseUnit.id}>
                        <td>{item.courseUnit.name}</td>
                        <td>{item.academicYear}</td>
                        <td>{item.courseUnit.ects}</td>
                        <td>{formatNumber(item.grade)}</td>
                        <td>
                          <div className="identity small">
                            <div className="bar bar--thin">
                              <div className="bar__fill" style={{ width: `${weight * 100}%` }} />
                            </div>
                            {formatNumber(weight * 100)}%
                          </div>
                        </td>
                        <td>{formatGrade(item.grade * weight, 2)}</td>
                      </tr>
                    );
                  })}
                </tbody>
              </table>
            </div>
            <p className="formula">
              Média ponderada da área{' '}
              <strong>
                ({formula}) ÷ {area.totalEcts} = {formatGrade(area.value, 2)}
              </strong>
            </p>
          </>
        ) : (
          <Alert
            type="warning"
            title={area.source ? SOURCE_LABELS[area.source] : 'Sem dados nesta área'}
          >
            {area.source === 'UNIVERSITY_AVERAGE' &&
              `Não existem notas nesta área. O valor ${formatGrade(area.value)} vem da média global, não de notas desta área.`}
            {area.source === 'ENTRY_GRADE' &&
              `Não existem notas universitárias. O valor ${formatGrade(area.value)} vem da nota de ingresso dividida por 10.`}
            {!area.source && 'Ainda não existem notas nem nota de ingresso para calcular um valor.'}
          </Alert>
        )}
      </Card>
    </div>
  );
}
