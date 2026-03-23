import axios from 'axios';
import useAuthStore from '../features/auth/useAuthStore';

const apiClient = axios.create({ baseURL: '/api', withCredentials: true });

// On 401 from any endpoint other than login, clear local auth state and redirect.
apiClient.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401 && !error.config?.url?.includes('/auth/login')) {
      useAuthStore.getState().clearAuth();
      window.location.href = '/login';
    }
    return Promise.reject(error);
  }
);

export default apiClient;
