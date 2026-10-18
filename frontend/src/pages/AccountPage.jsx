import { useAuth } from '../auth/AuthContext';
import Card from '../components/Card';
import PageHeader from '../components/PageHeader';
import { ROLE_LABELS } from '../utils/format';
export default function AccountPage() {
  const { user } = useAuth();
  return (
    <div className="page">
      <PageHeader
        title="A minha conta"
        subtitle="Consulta os dados da tua conta."
      />
      <Card title={ROLE_LABELS[user.role]}>
        <h3>{user.name}</h3>
        <span>N.º {user.number}</span>
        <span>{user.email}</span>
      </Card>
    </div>
  );
}
