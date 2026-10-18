import { NavLink, Outlet, useNavigate } from 'react-router-dom';
import { useState } from 'react';
import Alert from '../components/Alert';
import Avatar from '../components/Avatar';
import Logo from '../components/Logo';
import { useAuth } from '../auth/AuthContext';
import { ROLE_LABELS, currentAcademicYearLabel, currentSemesterLabel } from '../utils/format';

// Cada tipo de utilizador tem as suas opções de menu.
const NAVIGATION = {
  STUDENT: [
    { label: 'Áreas de conhecimento', to: '/knowledge-areas' },
    { label: 'A minha conta', to: '/student/account' }
  ],
  ACADEMIC_ADMINISTRATOR: [
    { label: 'Estudantes', to: '/admin/students' },
    { label: 'Docentes', to: '/admin/teachers' },
    { label: 'Plano de estudos', to: '/admin/study-plan' },
    { label: 'Áreas de conhecimento', to: '/admin/knowledge-areas' },
    { label: 'A minha conta', to: '/admin/account' }
  ],
  TEACHER: [{ label: 'A minha conta', to: '/account' }]
};

export default function AppLayout() {
  const { user, logout } = useAuth();
  const navigate = useNavigate();
  const [logoutError, setLogoutError] = useState('');

  async function handleLogout() {
    setLogoutError('');
    try {
      // Termina a sessão no servidor antes de abrir o login.
      await logout();
      navigate('/login');
    } catch (error) {
      setLogoutError(error.message);
    }
  }

  return (
    <div className="app">
      <aside className="sidebar">
        <Logo />
        <span className="sidebar__programme">Engenharia Informática</span>
        <span className="pill sidebar__role">{ROLE_LABELS[user.role]}</span>

        <nav className="sidebar__nav">
          {NAVIGATION[user.role].map((item) => (
            <NavLink key={item.to} to={item.to} className="sidebar__link">
              {item.label}
            </NavLink>
          ))}
          <button
            type="button"
            className="button button--link sidebar__logout-mobile"
            onClick={handleLogout}
          >
            Terminar sessão
          </button>
        </nav>

        <div className="sidebar__footer">
          <span>{currentAcademicYearLabel()}</span>
          <div className="sidebar__user">
            <Avatar
              name={user.name}
            />
            <div>
              <div className="sidebar__user-name">{user.name}</div>
              <div>N.º {user.number}</div>
            </div>
          </div>
          <button type="button" className="button button--link" onClick={handleLogout}>
            Terminar sessão
          </button>
        </div>
      </aside>

      <main className="main">
        <div className="topbar">
          <span>Workspace / {ROLE_LABELS[user.role]}</span>
          <div className="topbar__right">
            <span className="pill">{currentSemesterLabel()}</span>
            <Avatar
              name={user.name}
              size={32}
            />
          </div>
        </div>
        {logoutError && <Alert type="danger">{logoutError}</Alert>}
        <Outlet />
      </main>
    </div>
  );
}
