import { api } from './api';

export const getKnowledgeAreas = () => api.get('/knowledge-areas');

export const createKnowledgeArea = (area) => api.post('/knowledge-areas', area);

export const updateKnowledgeArea = (id, area) => api.put(`/knowledge-areas/${id}`, area);

export const deleteKnowledgeArea = (id) => api.delete(`/knowledge-areas/${id}`);
