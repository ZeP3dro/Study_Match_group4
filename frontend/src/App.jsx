import { Navigate, Route, Routes } from 'react-router-dom';
import RequireRole from './auth/RequireRole';
import { homePathFor, useAuth } from './auth/AuthContext';
import Loading from './components/Loading';
import AppLayout from './layouts/AppLayout';
import LoginPage from './pages/LoginPage';
import AccountPage from './pages/AccountPage';
// Cada Route associa um endereço à página que deve aparecer.
export default function App() {
  return (
    <Routes>
      <Route path="/login" element={<LoginPage />} />
      <Route element={<RequireRole role="STUDENT"><AppLayout /></RequireRole>}>
        <Route path="/student/account" element={<AccountPage />} />
      </Route>
      <Route element={<RequireRole role="ACADEMIC_ADMINISTRATOR"><AppLayout /></RequireRole>}>
        <Route path="/admin/account" element={<AccountPage />} />
      </Route>
      <Route element={<RequireRole role="TEACHER"><AppLayout /></RequireRole>}>
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
