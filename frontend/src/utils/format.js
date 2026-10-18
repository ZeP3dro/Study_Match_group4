export function initialsOf(name = '') {
  const parts = name.trim().split(/\s+/).filter(Boolean);
  if (parts.length === 0) return '?';
  const first = parts[0][0];
  const last = parts.length > 1 ? parts[parts.length - 1][0] : '';
  return (first + last).toUpperCase();
}

export const ROLE_LABELS = {
  STUDENT: 'Estudante',
  TEACHER: 'Docente',
  ACADEMIC_ADMINISTRATOR: 'Administração académica'
};

/** The 1st semester runs from September to January. */
export function currentSemesterLabel(date = new Date()) {
  const month = date.getMonth() + 1;
  return month >= 9 || month === 1 ? '1.º semestre' : '2.º semestre';
}

export function currentAcademicYearLabel(date = new Date()) {
  const startYear = date.getMonth() + 1 >= 9 ? date.getFullYear() : date.getFullYear() - 1;
  return `${startYear} / ${startYear + 1}`;
}
