import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../auth/AuthContext';
import Alert from './Alert';
import Card from './Card';
import Field from './Field';
import { changePassword } from '../services/authService';

export default function PasswordForm() {
  const { clearUser } = useAuth();
  const navigate = useNavigate();
  const [form, setForm] = useState({ currentPassword: '', newPassword: '', confirmation: '' });
  const [saving, setSaving] = useState(false);
  const [error, setError] = useState('');

  function change(field, value) {
    setForm({ ...form, [field]: value });
  }

  async function save(event) {
    event.preventDefault();
    setError('');
    if (form.newPassword !== form.confirmation) {
      setError('A confirmação não coincide com a nova palavra-passe.');
      return;
    }
    setSaving(true);
    try {
      await changePassword(form);
      clearUser();
      navigate('/login', {
        replace: true,
        state: { message: 'Palavra-passe alterada. Entra com a nova palavra-passe.' }
      });
    } catch (error) {
      setError(error.message);
    } finally {
      setSaving(false);
    }
  }

  return (
    <Card
      title="Alterar palavra-passe"
      subtitle="A alteração termina as sessões abertas desta conta."
    >
      <form className="form" onSubmit={save}>
        <Field label="Palavra-passe atual">
          <input
            className="input"
            type="password"
            required
            autoComplete="current-password"
            value={form.currentPassword}
            onChange={(event) => change('currentPassword', event.target.value)}
          />
        </Field>
        <Field label="Nova palavra-passe">
          <input
            className="input"
            type="password"
            required
            minLength={8}
            maxLength={72}
            autoComplete="new-password"
            value={form.newPassword}
            onChange={(event) => change('newPassword', event.target.value)}
          />
        </Field>
        <Field label="Confirmar nova palavra-passe">
          <input
            className="input"
            type="password"
            required
            minLength={8}
            maxLength={72}
            autoComplete="new-password"
            value={form.confirmation}
            onChange={(event) => change('confirmation', event.target.value)}
          />
        </Field>
        <p className="small muted">Pelo menos 8 caracteres.</p>
        {error && <Alert type="danger">{error}</Alert>}
        <div>
          <button className="button button--primary" disabled={saving}>
            {saving ? 'A guardar…' : 'Alterar palavra-passe'}
          </button>
        </div>
      </form>
    </Card>
  );
}
