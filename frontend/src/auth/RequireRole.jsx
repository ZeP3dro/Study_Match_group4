import { Navigate, useLocation } from 'react-router-dom';
import Loading from '../components/Loading';
import { homePathFor, useAuth } from './AuthContext';

export default function RequireRole({ role, children }) {
  const { user, loading } = useAuth();
  const location = useLocation();

  // Espera pela confirmação da sessão antes de decidir o acesso.
  if (loading) {
    return <Loading />;
  }
  // Sem sessão, abre o login e guarda o endereço pedido.
  if (!user) {
    return <Navigate to="/login" replace state={{ from: location.pathname }} />;
  }
  // Uma conta de outro tipo volta à sua página inicial.
  if (user.role !== role) {
    return <Navigate to={homePathFor(user)} replace />;
  }
  return children;
}
