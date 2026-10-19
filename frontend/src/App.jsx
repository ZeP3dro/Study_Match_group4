import AreaDetailPage from './pages/student/AreaDetailPage';
import KnowledgeAreasPage from './pages/student/KnowledgeAreasPage';
import StudyPlanPage from './pages/admin/StudyPlanPage';
import KnowledgeAreasAdminPage from './pages/admin/KnowledgeAreasAdminPage';
import { Navigate, Route, Routes } from 'react-router-dom';
import RequireRole from './auth/RequireRole';
import { homePathFor, useAuth } from './auth/AuthContext';
import Loading from './components/Loading';
import AppLayout from './layouts/AppLayout';
import LoginPage from './pages/LoginPage';
import AccountPage from './pages/AccountPage';
import ProfilePage from './pages/student/ProfilePage';
import AcademicPathPage from './pages/student/AcademicPathPage';
import StudentsPage from './pages/admin/StudentsPage';
import RegisterStudentPage from './pages/admin/RegisterStudentPage';
import StudentRecordPage from './pages/admin/StudentRecordPage';
import TeachersPage from './pages/admin/TeachersPage';
// Cada Route associa um endereço à página que deve aparecer.
export default function App() {
  return (
    <Routes>
      <Route path="/login" element={<LoginPage />} />
      <Route
        element={
          <RequireRole role="STUDENT">
            <AppLayout />
          </RequireRole>
        }
      >
        <Route path="/profile" element={<ProfilePage />} />
        <Route path="/profile/areas/:areaId" element={<AreaDetailPage />} />
        <Route path="/knowledge-areas" element={<KnowledgeAreasPage />} />
        <Route path="/academic-path" element={<AcademicPathPage />} />
        <Route path="/student/account" element={<AccountPage />} />
      </Route>
      <Route
        element={
          <RequireRole role="ACADEMIC_ADMINISTRATOR">
            <AppLayout />
          </RequireRole>
        }
      >
        <Route path="/admin/students" element={<StudentsPage />} />
        <Route path="/admin/students/new" element={<RegisterStudentPage />} />
        <Route path="/admin/students/:studentId" element={<StudentRecordPage />} />
        <Route path="/admin/students/:studentId/profile" element={<ProfilePage />} />
        <Route
          path="/admin/students/:studentId/profile/areas/:areaId"
          element={<AreaDetailPage />}
        />
        <Route path="/admin/study-plan" element={<StudyPlanPage />} />
        <Route path="/admin/knowledge-areas" element={<KnowledgeAreasAdminPage />} />
        <Route path="/admin/teachers" element={<TeachersPage />} />
        <Route path="/admin/account" element={<AccountPage />} />
      </Route>
      <Route
        element={
          <RequireRole role="TEACHER">
            <AppLayout />
          </RequireRole>
        }
      >
        <Route path="/account" element={<AccountPage />} />
      </Route>
      <Route path="*" element={<HomeRedirect />} />
    </Routes>
  );
}
// Escolhe a página inicial de quem entrou ou envia para o login.
function HomeRedirect() {
  const { user, loading } = useAuth();
  if (loading) return <Loading />;
  return <Navigate to={user ? homePathFor(user) : '/login'} replace />;
}
