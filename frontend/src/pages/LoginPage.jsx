import { useState } from 'react';
import { Navigate, useLocation, useNavigate } from 'react-router-dom';
import Alert from '../components/Alert';
import Field from '../components/Field';
import Logo from '../components/Logo';
import { homePathFor, useAuth } from '../auth/AuthContext';
import './LoginPage.css';

export default function LoginPage() {
  const { user, login } = useAuth();
  const navigate = useNavigate();
  const location = useLocation();
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const [fieldErrors, setFieldErrors] = useState({});
  const [submitting, setSubmitting] = useState(false);

  if (user) {
    return <Navigate to={homePathFor(user)} replace />;
  }

  async function handleSubmit(event) {
    // Envia o formulário sem recarregar a página.
    event.preventDefault();
    setError('');
    setFieldErrors({});
    setSubmitting(true);
    try {
      const signedInUser = await login(email, password);
      // Volta ao endereço pedido ou abre a página inicial da conta.
      navigate(location.state?.from || homePathFor(signedInUser), { replace: true });
    } catch (loginError) {
      setError(loginError.message);
      setFieldErrors(loginError.fieldErrors || {});
    } finally {
      setSubmitting(false);
    }
  }

  return (
    <div className="login">
      <div className="login__panel">
        <section className="login__welcome">
          <Logo size={52} />
          <h1 className="login__headline">O conhecimento cresce quando é partilhado.</h1>
          <p className="muted">
            Um espaço para acompanhares o teu percurso e encontrares o teu lugar na próxima equipa.
          </p>
        </section>

        <section className="login__form">
          {location.state?.message && <Alert type="success">{location.state.message}</Alert>}
          <div>
            <h2 className="login__title">Bem-vindo de volta.</h2>
            <p className="muted">Entra com a tua conta institucional.</p>
          </div>

          <form className="form" onSubmit={handleSubmit} noValidate>
            <Field label="Número de identificação" error={fieldErrors.email}>
              <input
                className="input"
                type="text"
                autoComplete="username"
                placeholder="Ex.: 12345"
                value={email}
                onChange={(event) => setEmail(event.target.value)}
              />
            </Field>
            <Field label="Palavra-passe" error={fieldErrors.password}>
              <input
                className="input"
                type="password"
                autoComplete="current-password"
                value={password}
                onChange={(event) => setPassword(event.target.value)}
              />
            </Field>

            {error && <Alert type="danger">{error}</Alert>}

            <button type="submit" className="button button--primary" disabled={submitting}>
              {submitting ? 'A entrar…' : 'Entrar →'}
            </button>
          </form>

          <hr className="divider" />
          <p className="small muted">
            Conta inativa ou dados incorretos?
            <br />
            Contacta os serviços académicos.
          </p>
        </section>
      </div>
    </div>
  );
}
