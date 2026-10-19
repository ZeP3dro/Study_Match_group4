import { createContext, useContext, useEffect, useState } from 'react';
import { getCurrentUser, signIn, signOut } from '../services/authService';

// Partilha a conta autenticada entre as páginas.
const AuthContext = createContext(null);

export function AuthProvider({ children }) {
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);

  // Ao abrir a aplicação, verifica se já existe uma sessão.
  useEffect(() => {
    getCurrentUser()
      .then(setUser)
      .catch(() => setUser(null))
      .finally(() => setLoading(false));
  }, []);

  async function login(email, password) {
    // Guarda os dados devolvidos pelo login.
    const signedInUser = await signIn(email, password);
    setUser(signedInUser);
    return signedInUser;
  }

  async function logout() {
    try {
      await signOut();
    } catch (error) {
      // Uma sessão já terminada também permite sair da conta.
      if (error.status !== 401) throw error;
    }
    setUser(null);
  }

  return (
    <AuthContext.Provider
      value={{
        user,
        loading,
        login,
        logout
      }}
    >
      {children}
    </AuthContext.Provider>
  );
}

export function useAuth() {
  return useContext(AuthContext);
}

// Escolhe o destino do login conforme o tipo de utilizador.
export function homePathFor(user) {
  if (user.role === 'STUDENT') return '/profile';
  if (user.role === 'ACADEMIC_ADMINISTRATOR') return '/admin/students';
  return '/account';
}
