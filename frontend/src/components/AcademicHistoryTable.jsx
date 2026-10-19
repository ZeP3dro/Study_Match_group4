import { formatGrade } from '../utils/format';
export default function AcademicHistoryTable({ enrolments, children }) {
  if (!enrolments.length)
    return <p className="empty">Ainda não existem unidades curriculares no histórico.</p>;
  return (
    <div className="table-wrapper">
      <table className="table">
        <thead>
          <tr>
            <th>Unidade curricular</th>
            <th>Ano letivo</th>
            <th>Semestre</th>
            <th>ECTS</th>
            <th>Nota / 20</th>
          </tr>
        </thead>
        <tbody>
          {enrolments.map((item) => (
            <tr key={item.id}>
              <td>
                {item.courseUnit.name}
                <div className="small muted">{item.courseUnit.knowledgeAreaName || 'Sem área'}</div>
              </td>
              <td>{item.academicYear}</td>
              <td>{item.courseUnit.semester}</td>
              <td>{item.courseUnit.ects}</td>
              <td>{children ? children(item) : formatGrade(item.grade)}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}
