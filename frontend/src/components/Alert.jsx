/**
 * Coloured message box. type: info | success | warning | danger.
 */
export default function Alert({ type = 'info', title, children }) {
  if (!title && !children) {
    return null;
  }
  return (
    <div className={`alert alert--${type}`} role={type === 'danger' ? 'alert' : undefined}>
      {title && <span className="alert__title">{title}</span>}
      {children && <span>{children}</span>}
    </div>
  );
}
