import { api } from './api';
export const changeAccountState = (id, active) => api.put(`/users/${id}/active`, { active });
