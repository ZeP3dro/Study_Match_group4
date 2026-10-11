const API_BASE_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080/api';

/**
 * Service to interact with Knowledge Areas endpoint
 */
export async function getKnowledgeAreas() {
  const response = await fetch(`${API_BASE_URL}/knowledge-areas`, {
    headers: {
      'Accept': 'application/json'
    }
  });

  if (!response.ok) {
    throw new Error(`Failed to fetch knowledge areas: ${response.status} ${response.statusText}`);
  }

  return response.json();
}
