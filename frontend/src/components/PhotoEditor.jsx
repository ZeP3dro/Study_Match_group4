import { useRef, useState } from 'react';
import Avatar from './Avatar';
import Alert from './Alert';
import { useAuth } from '../auth/AuthContext';
import { uploadPhoto } from '../services/photoService';

/** A mesma edição de fotografia para alunos, docentes e administrador. */
export default function PhotoEditor({ account }) {
  const input = useRef(null);
  const { user, refreshUser } = useAuth();
  const canEdit = user.id === account.id;
  const [hasPhoto, setHasPhoto] = useState(account.hasPhoto);
  const [version, setVersion] = useState(Date.now());
  const [saving, setSaving] = useState(false);
  const [error, setError] = useState('');

  async function changePhoto(event) {
    const file = event.target.files[0];
    event.target.value = '';
    if (!file) return;
    setSaving(true);
    setError('');
    try {
      await uploadPhoto(account.id, file);
      setHasPhoto(true);
      setVersion(Date.now());
      if (user.id === account.id) await refreshUser();
    } catch (error) {
      setError(error.message);
    } finally {
      setSaving(false);
    }
  }

  return (
    <div className="stack">
      <div className="identity">
        <Avatar
          name={account.name}
          accountId={account.id}
          hasPhoto={hasPhoto}
          photoVersion={version}
          size={64}
        />
        <div>
          <h3>{account.name}</h3>
          <p className="small muted">N.º {account.number}</p>
          {canEdit && (
            <>
              <button
                type="button"
                className="button button--link small"
                disabled={saving}
                onClick={() => input.current.click()}
              >
                {saving
                  ? 'A enviar…'
                  : hasPhoto
                    ? 'Alterar foto de perfil'
                    : 'Adicionar foto de perfil'}
              </button>
              <input
                ref={input}
                type="file"
                accept="image/png,image/jpeg"
                hidden
                onChange={changePhoto}
              />
            </>
          )}
        </div>
      </div>
      {canEdit && <span className="small muted">JPEG ou PNG · máximo 2 MB</span>}
      {error && <Alert type="danger">{error}</Alert>}
    </div>
  );
}
