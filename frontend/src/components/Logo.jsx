export default function Logo({ size = 40 }) {
  return (
    <span className="sidebar__logo">
      <img src="/logo.png" width={size} height={size} alt="" aria-hidden="true" />
      StudyMatch
    </span>
  );
}
