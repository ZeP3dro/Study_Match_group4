import { Link } from 'react-router-dom';
import GradeBar from './GradeBar';
import { SOURCE_LABELS, formatGrade } from '../utils/format';
export default function AreaScoreRow({ area, detailPath }) {
  const estimate = area.source === 'UNIVERSITY_AVERAGE' || area.source === 'ENTRY_GRADE';
  return (
    <div className="area-row">
      <div className="area-row__top">
        <span>{area.name}</span>
        <span>{formatGrade(area.value)}</span>
      </div>
      <GradeBar value={area.value} estimate={estimate} />
      <div className="area-row__meta">
        {area.source === 'AREA_AVERAGE' && (
          <span>
            {area.courseUnitsCount} UC · {area.totalEcts} ECTS
          </span>
        )}
        {estimate && <span className="badge badge--warning">{SOURCE_LABELS[area.source]}</span>}
        {!area.source && <span>Sem dados neste momento</span>}
      </div>
      <p className="small muted">{area.description}</p>
      <Link className="small" to={detailPath}>
        Ver detalhe da área →
      </Link>
    </div>
  );
}
