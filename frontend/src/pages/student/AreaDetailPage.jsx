import { Link, useParams } from 'react-router-dom';
import { useAuth } from '../../auth/AuthContext';
import useLoader from '../../hooks/useLoader';
import { getProfile } from '../../services/profileService';
import { formatGrade, SOURCE_LABELS } from '../../utils/format';
import Alert from '../../components/Alert';
import Loading from '../../components/Loading';
import Card from '../../components/Card';
import PageHeader from '../../components/PageHeader';
export default function AreaDetailPage() {
  const { user } = useAuth();
  const { studentId: routeStudentId, areaId } = useParams();
  const studentId = routeStudentId || user.id;
  const { data: profile, loading, error } = useLoader(() => getProfile(studentId), [studentId]);
  if (loading) return <Loading />;
  if (error) return <Alert type="danger">{error}</Alert>;
  const area = profile.areas.find((item) => item.areaId === Number(areaId));
  if (!area) return <Alert type="danger">Área de conhecimento não encontrada.</Alert>;
  const back = routeStudentId ? `/admin/students/${studentId}/profile` : '/profile';
  return <div className="page">
    <PageHeader title={area.name} actions={<Link to={back}>← Voltar ao perfil</Link>} />
    <Card title={formatGrade(area.value)}>
      <p>{area.description}</p>
      <p>{area.courseUnitsCount} UC · {area.totalEcts} ECTS</p>
      <p>{area.source ? SOURCE_LABELS[area.source] : 'Sem dados nesta área'}</p>
    </Card>
  </div>;
}
