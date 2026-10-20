import { STATUS_LABELS } from '../utils/format';
import Card from './Card';
import PhotoEditor from './PhotoEditor';
import { formatGrade } from '../utils/format';
export default function StudentIdentityCard({ student, showContactNote }) {
  return (
    <Card>
      <PhotoEditor key={student.id} account={student} />
      <hr className="divider" />
      <div className="detail-list">
        <span>{student.email}</span>
        <span className="badge">
          {STATUS_LABELS[student.status]}
          {!student.active ? ' · conta inativa' : ''}
        </span>
        <span className="muted">
          Primeira inscrição {student.firstEnrolmentYear} · {student.curricularYear}.º ano
          curricular
        </span>
        <span>
          <strong>Nota de ingresso:</strong>{' '}
          {student.entryGrade === null
            ? 'não registada'
            : `${student.entryGrade} / 200 → ${formatGrade(student.entryGradeOn20)} / 20`}
        </span>
        {showContactNote && (
          <span className="small muted">Dados incorretos? Contacta os serviços académicos.</span>
        )}
      </div>
    </Card>
  );
}
