import { useEffect, useState } from 'react';
import { Link, useParams } from 'react-router-dom';
import Alert from '../../components/Alert';
import AreaScoreRow from '../../components/AreaScoreRow';
import Card from '../../components/Card';
import GradeBar from '../../components/GradeBar';
import Loading from '../../components/Loading';
import PageHeader from '../../components/PageHeader';
import StudentIdentityCard from '../../components/StudentIdentityCard';
import { useAuth } from '../../auth/AuthContext';
import useLoader from '../../hooks/useLoader';
import { getProfile } from '../../services/profileService';
import { getStudent } from '../../services/studentService';
import { formatGrade } from '../../utils/format';

/**
 * UC14 view student profile. Used by the student (own profile) and by the administrator
 * (route /admin/students/:studentId/profile).
 */
export default function ProfilePage() {
  const { user } = useAuth();
  const { studentId: studentIdFromRoute } = useParams();
  const isAdministratorView = Boolean(studentIdFromRoute);
  const studentId = studentIdFromRoute || user.id;
  const basePath = isAdministratorView ? `/admin/students/${studentId}/profile` : '/profile';

  const { data, error, loading } = useLoader(
    () => Promise.all([getStudent(studentId), getProfile(studentId)]),
    [studentId]
  );
  const [student, setStudent] = useState(null);

  useEffect(() => {
    if (data) setStudent(data[0]);
  }, [data]);

  if (loading || (!error && !student)) return <Loading />;
  if (error) return <Alert type="danger">{error}</Alert>;

  const profile = data[1];

  return (
    <div className="page">
      <PageHeader
        title={
          isAdministratorView ? `Perfil académico · ${student.name}` : 'O teu perfil académico'
        }
        subtitle="Uma visão das competências, construída a partir do percurso académico."
        actions={
          isAdministratorView && <Link to={`/admin/students/${studentId}`}>← Voltar à ficha</Link>
        }
      />

      <div className="grid-2">
        <Card
          title="Competências por área"
          subtitle="Médias ponderadas por ECTS · escala de 0 a 20"
        >
          {!profile.hasAcademicData && (
            <Alert type="warning" title="Ainda não há dados académicos">
              Sem notas e sem nota de ingresso, não é possível estimar o perfil. Assim que houver
              uma nota de ingresso ou o primeiro resultado, o perfil é calculado automaticamente.
            </Alert>
          )}
          <div className="area-list">
            {profile.areas.map((area) => (
              <AreaScoreRow
                key={area.areaId}
                area={area}
                detailPath={`${basePath}/areas/${area.areaId}`}
              />
            ))}
          </div>
          <hr className="divider" />
          <div className="area-row">
            <div className="area-row__top">
              <span>Média universitária global</span>
              <span>{formatGrade(profile.universityAverage)} / 20</span>
            </div>
            <GradeBar value={profile.universityAverage} />
            <div className="area-row__meta">
              {profile.courseUnitsCount > 0
                ? `${profile.courseUnitsCount} UC · ${profile.totalEcts} ECTS`
                : 'Ainda sem unidades curriculares com nota'}
            </div>
          </div>
          <p className="small muted">
            Cada UC conta uma vez, com a melhor nota registada, incluindo reprovações.
          </p>
        </Card>

        <div className="stack">
          <StudentIdentityCard student={student} showContactNote={!isAdministratorView} />
        </div>
      </div>
    </div>
  );
}
