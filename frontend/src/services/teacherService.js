import { api, toQuery } from './api';
// Pedidos à API para pesquisar, registar e editar docentes.
export const searchTeachers = (number) => api.get(`/teachers${toQuery({ number })}`);
export const registerTeacher = (form) => api.post('/teachers', form);
export const updateTeacher = (id, form) => api.put(`/teachers/${id}`, form);
