import { STATUS_LABELS } from '../../utils/format';
import { useState } from 'react';
import { Link } from 'react-router-dom';
import Alert from '../../components/Alert';
import Avatar from '../../components/Avatar';
import Card from '../../components/Card';
import Field from '../../components/Field';
import Loading from '../../components/Loading';
import PageHeader from '../../components/PageHeader';
import useLoader from '../../hooks/useLoader';
import { searchStudents } from '../../services/studentService';
export default function StudentsPage() {
  const [number, setNumber] = useState('');
  const { data, loading, error } = useLoader(() => searchStudents(number), [number]);
  return (
    <div className="page">
      <PageHeader
        title="Estudantes"
        subtitle="Consulta e atualiza a informação académica."
        actions={
          <Link className="button button--primary" to="/admin/students/new">
            Registar estudante
          </Link>
        }
      />
      <Card>
        <Field label="Pesquisar por número de estudante">
          <input
            className="input"
            inputMode="numeric"
            placeholder="Ex.: 34567"
            value={number}
            onChange={(event) => setNumber(event.target.value.replace(/\D/g, ''))}
          />
        </Field>
        {error && <Alert type="danger">{error}</Alert>}
        {loading ? (
          <Loading />
        ) : data?.length ? (
          <div className="table-wrapper">
            <table className="table">
              <thead>
                <tr>
                  <th>Estudante</th>
                  <th>Número</th>
                  <th>Email</th>
                  <th>Estado</th>
                  <th></th>
                </tr>
              </thead>
              <tbody>
                {data.map((student) => (
                  <tr key={student.id}>
                    <td>
                      <div className="identity">
                        <Avatar
                          name={student.name}
                          accountId={student.id}
                          hasPhoto={student.hasPhoto}
                        />
                        {student.name}
                      </div>
                    </td>
                    <td>{student.number}</td>
                    <td>{student.email}</td>
                    <td>
                      {STATUS_LABELS[student.status]}
                      {!student.active ? ' · conta inativa' : ''}
                    </td>
                    <td>
                      <Link to={`/admin/students/${student.id}`}>Ver ficha →</Link>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        ) : (
          !error && <p className="empty">Nenhum estudante encontrado.</p>
        )}
      </Card>
    </div>
  );
}
