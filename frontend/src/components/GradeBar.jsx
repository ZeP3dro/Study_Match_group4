/**
 * Horizontal bar for a value on the 0–20 scale. Cold-start estimates use a striped bar.
 */
export default function GradeBar({ value, estimate = false, max = 20 }) {
  const width =
    value === null || value === undefined ? 0 : Math.min(100, (Number(value) / max) * 100);
  return (
    <div className="bar">
      <div
        className={`bar__fill ${estimate ? 'bar__fill--estimate' : ''}`}
        style={{ width: `${width}%` }}
      />
    </div>
  );
}
