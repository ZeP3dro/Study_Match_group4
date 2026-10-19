import { api } from './api';
export const getProfile = (id) => api.get(`/students/${id}/profile`);
