import { useAuth } from '../../auth/AuthContext';
import Alert from '../../components/Alert';
import Loading from '../../components/Loading';
import Card from '../../components/Card';
import PageHeader from '../../components/PageHeader';
import AcademicHistoryTable from '../../components/AcademicHistoryTable';
import useLoader from '../../hooks/useLoader';
import { getEnrolments } from '../../services/academicRecordService';
export default function AcademicPathPage() {
  const { user } = useAuth();
  const { data, loading, error } = useLoader(() => getEnrolments(user.id), [user.id]);
  if (loading) return <Loading />;
  if (error) return <Alert type="danger">{error}</Alert>;
  return (
    <div className="page">
      <PageHeader
        title="O teu percurso académico"
        subtitle="Unidades curriculares e resultados registados."
      />
      <Card title="Histórico académico">
        <AcademicHistoryTable enrolments={data} />
      </Card>
    </div>
  );
}
