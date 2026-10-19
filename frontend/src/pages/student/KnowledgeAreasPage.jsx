import { useState } from 'react';
import { Link } from 'react-router-dom';
import { useAuth } from '../../auth/AuthContext';
import Alert from '../../components/Alert';
import Card from '../../components/Card';
import Field from '../../components/Field';
import Loading from '../../components/Loading';
import PageHeader from '../../components/PageHeader';
import useLoader from '../../hooks/useLoader';
import { getKnowledgeAreas } from '../../services/knowledgeAreaService';
import { getProfile } from '../../services/profileService';
import { formatGrade, SOURCE_LABELS } from '../../utils/format';

export default function KnowledgeAreasPage() {
  const { user } = useAuth();
  const { data, error, loading } = useLoader(
    () => Promise.all([getKnowledgeAreas(), getProfile(user.id)]),
    [user.id]
  );
  const [search, setSearch] = useState('');
  const [openAreaId, setOpenAreaId] = useState(null);

  if (loading) return <Loading />;
  if (error) return <Alert type="danger">{error}</Alert>;
  const [areas, profile] = data;
  const text = search.trim().toLowerCase();
  const visible = areas.filter(
    (area) =>
      area.name.toLowerCase().includes(text) || area.description.toLowerCase().includes(text)
  );

  return (
    <div className="page">
      <PageHeader
        title="Áreas de conhecimento"
        subtitle="Conhece as áreas do curso e as UC que contribuem para as tuas médias."
      />
      <div className="toolbar">
        <Field label="Pesquisar áreas">
          <input
            className="input"
            placeholder="Nome ou descrição…"
            value={search}
            onChange={(event) => setSearch(event.target.value)}
          />
        </Field>
        <span className="pill">{visible.length} áreas</span>
      </div>
      <div className="area-cards">
        {visible.map((area) => {
          const score = profile.areas.find((item) => item.areaId === area.id);
          return (
            <Card key={area.id} title={area.name}>
              <p className="small muted">{area.description}</p>
              <strong>{formatGrade(score?.value)} / 20</strong>
              <span className="small muted">
                {score?.source ? SOURCE_LABELS[score.source] : 'Sem dados'}
              </span>
              <button
                className="button button--link small"
                onClick={() => setOpenAreaId(openAreaId === area.id ? null : area.id)}
              >
                {area.courseUnits.length} unidades curriculares {openAreaId === area.id ? '↑' : '→'}
              </button>
              {openAreaId === area.id && (
                <ul className="small">
                  {area.courseUnits.map((unit) => (
                    <li key={unit.id}>
                      {unit.name} · {unit.curricularYear}.º ano · {unit.ects} ECTS
                    </li>
                  ))}
                </ul>
              )}
              <Link to={`/profile/areas/${area.id}`}>Ver detalhe da área →</Link>
            </Card>
          );
        })}
      </div>
      {visible.length === 0 && <p className="empty">Nenhuma área encontrada.</p>}
      <Card title="Uma área por unidade curricular">
        <p className="small muted">
          As notas das UC de cada área contribuem para o perfil. UC sem área, como o estágio, contam
          apenas para a média global.
        </p>
      </Card>
    </div>
  );
}
