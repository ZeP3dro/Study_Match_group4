import { api } from './api';

export const signIn = (email, password) => api.post('/auth/login', { email, password });

export const signOut = () => api.post('/auth/logout');

export const getCurrentUser = () => api.get('/auth/me');
