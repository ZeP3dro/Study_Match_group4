import { api } from './api';
export const getEnrolments = (id) => api.get(`/students/${id}/enrolments`);
export const enrol = (id, courseUnitId, academicYearId) =>
  api.post(`/students/${id}/enrolments`, { courseUnitId, academicYearId });
export const recordGrade = (id, enrolmentId, grade) =>
  api.put(`/students/${id}/enrolments/${enrolmentId}/grade`, { grade });
