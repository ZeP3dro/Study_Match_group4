import { useState } from 'react';
import Alert from '../../components/Alert';
import Card from '../../components/Card';
import Field from '../../components/Field';
import Loading from '../../components/Loading';
import PageHeader from '../../components/PageHeader';
import TeacherForm from './TeacherForm';
import useLoader from '../../hooks/useLoader';
import { searchTeachers } from '../../services/teacherService';

export default function TeachersPage() {
  const [number, setNumber] = useState('');
  const [editing, setEditing] = useState(null);
  const [creating, setCreating] = useState(false);
  const [registered, setRegistered] = useState(null);
  // Atualiza a lista quando o número de pesquisa muda.
  const { data, loading, error, reload } = useLoader(() => searchTeachers(number), [number]);

  function saved(account) {
    setEditing(null);
    setCreating(false);
    // Mostra os dados de acesso só quando a conta acaba de ser criada.
    setRegistered(account.initialPassword ? account : null);
    reload();
  }

  return (
    <div className="page">
      <PageHeader
        title="Docentes"
        subtitle="Cria contas e pesquisa os docentes pelo número."
        actions={
          <button
            className="button button--primary"
            onClick={() => {
              setCreating(true);
              setEditing(null);
              setRegistered(null);
            }}
          >
            Registar docente
          </button>
        }
      />
      {registered && (
        <Card title="Docente registado">
          <div className="detail-list">
            <strong>{registered.name}</strong>
            <span>N.º {registered.number}</span>
            <span>{registered.email}</span>
            <span>
              Palavra-passe inicial: <code>{registered.initialPassword}</code>
            </span>
          </div>
          <p className="small muted">
            A palavra-passe inicial só é apresentada agora. Entrega-a ao docente.
          </p>
          <div>
            <button className="button" onClick={() => setRegistered(null)}>
              Fechar
            </button>
          </div>
        </Card>
      )}
      {(creating || editing) && (
        <TeacherForm
          key={editing?.id || 'new'}
          teacher={editing}
          onSaved={saved}
          onCancel={() => {
            setCreating(false);
            setEditing(null);
          }}
        />
      )}
      <Card>
        <Field label="Pesquisar por número de docente">
          <input
            className="input"
            inputMode="numeric"
            placeholder="Ex.: 23456"
            value={number}
            onChange={(event) => setNumber(event.target.value.replace(/\D/g, ''))}
          />
        </Field>
      </Card>
      {error && <Alert type="danger">{error}</Alert>}
      {loading ? (
        <Loading />
      ) : (
        <div className="stack">
          {data?.map((teacher) => (
            <Card key={teacher.id}>
              <h3>{teacher.name}</h3>
              <span>N.º {teacher.number}</span>
              <span>{teacher.email}</span>
              <span className="small muted">{teacher.department}</span>
              <span className="badge">{teacher.active ? 'Conta ativa' : 'Conta inativa'}</span>
              <div className="form-actions">
                <button
                  className="button"
                  onClick={() => {
                    setEditing(teacher);
                    setCreating(false);
                  }}
                >
                  Editar dados
                </button>
              </div>
            </Card>
          ))}
          {!error && data?.length === 0 && <p className="empty">Nenhum docente encontrado.</p>}
        </div>
      )}
    </div>
  );
}
