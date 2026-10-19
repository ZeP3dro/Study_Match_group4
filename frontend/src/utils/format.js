/**
 * Formatting helpers shared by the pages. Numbers use the Portuguese format (16,8).
 */
export function formatGrade(value, decimals = 1) {
  if (value === null || value === undefined) {
    return '—';
  }
  return Number(value).toLocaleString('pt-PT', {
    minimumFractionDigits: decimals,
    maximumFractionDigits: decimals
  });
}

/** Shows 17 instead of 17,0, but keeps 16,5. */
export function formatNumber(value) {
  if (value === null || value === undefined) {
    return '—';
  }
  return Number(value).toLocaleString('pt-PT', { maximumFractionDigits: 2 });
}

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

export const SOURCE_LABELS = {
  AREA_AVERAGE: 'Média da área',
  UNIVERSITY_AVERAGE: 'Estimativa · média global',
  ENTRY_GRADE: 'Estimativa · nota de ingresso'
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

export const STATUS_LABELS = {
  ACTIVE: 'Ativo',
  SUSPENDED: 'Suspenso',
  GRADUATED: 'Diplomado',
  DROPPED_OUT: 'Abandonou'
};
