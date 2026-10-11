import { useEffect, useState } from 'react';
import { getKnowledgeAreas } from './services/api';

export default function App() {
  const [areas, setAreas] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    getKnowledgeAreas()
      .then(setAreas)
      .catch(() => setError('Não foi possível carregar as áreas. Confirma se o backend e o MySQL estão a correr.'))
      .finally(() => setLoading(false));
  }, []);

  return (
    <main>
      <h1>StudyMatch</h1>
      <h2>Áreas de conhecimento</h2>
      {loading ? (
        <p>A carregar...</p>
      ) : error ? (
        <p role="alert">{error}</p>
      ) : areas.length === 0 ? (
        <p>Não existem áreas de conhecimento.</p>
      ) : (
        <ul>
          {areas.map(area => (
            <li key={area.name}>
              <strong>{area.name}</strong>
              <p>{area.description}</p>
            </li>
          ))}
        </ul>
      )}
    </main>
  );
}
