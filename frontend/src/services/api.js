const API_BASE_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080/api';

export class ApiError extends Error {
  constructor(message, status, fieldErrors = {}) {
    super(message);
    this.status = status;
    this.fieldErrors = fieldErrors;
  }
}

async function request(path, { method = 'GET', body } = {}) {
  const options = {
    method,
    // Envia o cookie que identifica a sessão.
    credentials: 'include',
    headers: { Accept: 'application/json' }
  };
  if (!['GET', 'HEAD', 'OPTIONS'].includes(method)) {
    // Pede o token de segurança antes de enviar uma alteração.
    const csrf = await request('/auth/csrf');
    options.headers[csrf.headerName] = csrf.token;
  }
  if (body !== undefined) {
    options.headers['Content-Type'] = 'application/json';
    options.body = JSON.stringify(body);
  }

  let response;
  try {
    response = await fetch(`${API_BASE_URL}${path}`, options);
  } catch {
    throw new ApiError(
      'Não foi possível ligar ao servidor. Confirma se o backend está a correr.',
      0
    );
  }

  // Algumas operações, como o logout, não devolvem conteúdo.
  if (response.status === 204) {
    return null;
  }
  const data = await response.json().catch(() => null);
  // Mantém a mensagem e os erros dos campos para a página os mostrar.
  if (!response.ok) {
    throw new ApiError(
      data?.message || defaultMessageFor(response.status),
      response.status,
      data?.fieldErrors
    );
  }
  return data;
}

function defaultMessageFor(status) {
  if (status === 401) return 'A tua sessão terminou. Entra novamente.';
  if (status === 403) return 'Não tens permissão para ver ou alterar estes dados.';
  if (status === 404) return 'Não encontrámos o que procuras.';
  return 'Ocorreu um erro inesperado. Tenta novamente.';
}

export const api = {
  get: (path) => request(path),
  post: (path, body) => request(path, { method: 'POST', body })
};
