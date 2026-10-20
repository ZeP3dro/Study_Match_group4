import { photoUrl } from '../services/api';
import { initialsOf } from '../utils/format';
export default function Avatar({ name, accountId, hasPhoto = false, photoVersion = 0, size = 36 }) {
  return (
    <span
      className="avatar"
      style={{ width: size, height: size, fontSize: Math.max(11, size / 3) }}
    >
      {hasPhoto && accountId ? (
        <img src={photoUrl(accountId, photoVersion)} alt={`Foto de ${name}`} />
      ) : (
        initialsOf(name)
      )}
    </span>
  );
}
