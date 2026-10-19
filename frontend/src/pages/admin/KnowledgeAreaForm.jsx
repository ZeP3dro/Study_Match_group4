import { useState } from 'react';
import Alert from '../../components/Alert';
import Card from '../../components/Card';
import Field from '../../components/Field';
import { createKnowledgeArea, updateKnowledgeArea } from '../../services/knowledgeAreaService';

export default function KnowledgeAreaForm({ area, onSaved, onCancel }) {
  const isNew = !area;
  const [name, setName] = useState(area?.name ?? '');
  const [description, setDescription] = useState(area?.description ?? '');
  const [error, setError] = useState('');

  async function handleSubmit(event) {
    event.preventDefault();
    setError('');
    try {
      if (isNew) {
        await createKnowledgeArea({ name, description });
        setName('');
        setDescription('');
      } else {
        await updateKnowledgeArea(area.id, { name, description });
      }
      onSaved();
    } catch (saveError) {
      setError(saveError.message);
    }
  }

  return (
    <Card title={isNew ? 'Criar área de conhecimento' : `Editar · ${area.name}`}>
      <form className="form" onSubmit={handleSubmit}>
        <Field label="Nome">
          <input
            className={`input ${error ? 'input--invalid' : ''}`}
            value={name}
            onChange={(event) => setName(event.target.value)}
          />
        </Field>
        <Field label="Descrição">
          <input
            className="input"
            value={description}
            onChange={(event) => setDescription(event.target.value)}
          />
        </Field>
        {error && <Alert type="danger">{error}</Alert>}
        <div className="form-actions">
          <button type="submit" className="button button--primary" disabled={!name.trim()}>
            {isNew ? 'Criar área' : 'Guardar'}
          </button>
          {!isNew && (
            <button type="button" className="button" onClick={onCancel}>
              Cancelar
            </button>
          )}
        </div>
      </form>
    </Card>
  );
}
