import { api } from './api';
export function uploadPhoto(accountId, file) {
  const data = new FormData();
  data.append('photo', file);
  return api.upload(`/users/${accountId}/photo`, data);
}
