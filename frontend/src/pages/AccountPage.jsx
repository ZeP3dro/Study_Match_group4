import PasswordForm from '../components/PasswordForm';
import { useAuth } from '../auth/AuthContext';
import Card from '../components/Card';
import PageHeader from '../components/PageHeader';
import PhotoEditor from '../components/PhotoEditor';
import { ROLE_LABELS } from '../utils/format';
export default function AccountPage() {
  const { user } = useAuth();
  return (
    <div className="page">
      <PageHeader
        title="A minha conta"
        subtitle="Gere a tua fotografia e a palavra-passe da conta."
      />
      <Card title={ROLE_LABELS[user.role]}>
        <PhotoEditor account={user} />
        <span>{user.email}</span>
      </Card>
      <PasswordForm />
    </div>
  );
}
