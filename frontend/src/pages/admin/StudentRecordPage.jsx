import StatusCard from './studentRecord/StatusCard';
import { Link, useParams } from 'react-router-dom';
import Alert from '../../components/Alert';
import Loading from '../../components/Loading';
import PageHeader from '../../components/PageHeader';
import StudentIdentityCard from '../../components/StudentIdentityCard';
import useLoader from '../../hooks/useLoader';
import { getStudent } from '../../services/studentService';
import { getEnrolments } from '../../services/academicRecordService';
import { getCourseUnits, getAcademicYears } from '../../services/studyPlanService';
import IdentityCard from './studentRecord/IdentityCard';
import EnrolmentsCard from './studentRecord/EnrolmentsCard';
export default function StudentRecordPage() {
  const { studentId } = useParams();
  const { data, loading, error, reload } = useLoader(
    () =>
      Promise.all([
        getStudent(studentId),
        getEnrolments(studentId),
        getCourseUnits(),
        getAcademicYears()
      ]),
    [studentId]
  );
  if (loading && !data) return <Loading />;
  if (error) return <Alert type="danger">{error}</Alert>;
  const [student, enrolments, units, years] = data;
  return (
    <div className="page">
      <PageHeader
        title={`Ficha de ${student.name}`}
        subtitle={`N.º ${student.number}`}
      />
      <div className="grid-2">
        <StudentIdentityCard key={studentId} student={student} />
        <IdentityCard key={studentId} student={student} onChange={reload} />
      </div>
      <StatusCard key={`${studentId}-${student.status}`} student={student} onChange={reload} />
      <EnrolmentsCard
        student={student}
        studentId={studentId}
        enrolments={enrolments}
        units={units}
        years={years}
        onChange={reload}
      />
      <Link to="/admin/students">← Voltar aos estudantes</Link>
    </div>
  );
}
