import { api } from './api';
export const getStudyPlan = () => api.get('/study-plan');
export const getCourseUnits = () => api.get('/course-units');
export const getAcademicYears = () => api.get('/academic-years');
export const createCourseUnit = (form) => api.post('/course-units', form);
export const updateCourseUnit = (id, form) => api.put(`/course-units/${id}`, form);
export const deleteCourseUnit = (id) => api.delete(`/course-units/${id}`);
export const assignKnowledgeArea = (id, knowledgeAreaId) =>
  api.put(`/course-units/${id}/knowledge-area`, { knowledgeAreaId });
