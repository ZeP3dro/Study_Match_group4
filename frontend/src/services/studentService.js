import { api, toQuery } from './api';
export const searchStudents = (number) => api.get(`/students${toQuery({ number })}`);
export const getStudent = (id) => api.get(`/students/${id}`);
export const registerStudent = (student) => api.post('/students', student);
export const updateStudent = (id, student) => api.put(`/students/${id}`, student);

export const changeStudentStatus = (id, status) => api.put(`/students/${id}/status`, { status });
