import { initialsOf } from '../utils/format';
export default function Avatar({ name, size = 36 }) {
  return <span className="avatar" style={{ width: size, height: size, fontSize: Math.max(11, size / 3) }}>{initialsOf(name)}</span>;
}
