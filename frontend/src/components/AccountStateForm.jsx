import { useState } from 'react';
import Field from './Field';
import Alert from './Alert';
import { changeAccountState } from '../services/accountService';

export default function AccountStateForm({ account, onChange }) {
  const [active, setActive] = useState(String(account.active));
  const [saving, setSaving] = useState(false);
  const [error, setError] = useState('');

  async function save(event) {
    event.preventDefault();
    setSaving(true);
    setError('');
    try {
      await changeAccountState(account.id, active === 'true');
      onChange();
    } catch (error) {
      setError(error.message);
    } finally {
      setSaving(false);
    }
  }

  return (
    <form className="form" onSubmit={save}>
      <Field label="Acesso à conta">
        <select
          className="select"
          value={active}
          onChange={(event) => setActive(event.target.value)}
        >
          <option value="true">Ativa — pode iniciar sessão</option>
          <option value="false">Inativa — acesso bloqueado</option>
        </select>
      </Field>
      <p className="small muted">
        Desativar a conta preserva os dados e termina o acesso das sessões abertas.
      </p>
      {error && <Alert type="danger">{error}</Alert>}
      <div>
        <button className="button" disabled={saving || active === String(account.active)}>
          {saving ? 'A guardar…' : 'Guardar acesso'}
        </button>
      </div>
    </form>
  );
}
