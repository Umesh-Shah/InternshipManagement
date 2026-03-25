import axios from 'axios';
import useAuthStore from '../features/auth/useAuthStore';
import { API_PATHS, ROUTES } from '@/constants';

const apiClient = axios.create({ baseURL: API_PATHS.BASE, withCredentials: true });

// On 401 from any endpoint other than login, clear local auth state and redirect.
apiClient.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401 && !error.config?.url?.includes(API_PATHS.AUTH_LOGIN)) {
      useAuthStore.getState().clearAuth();
      window.location.href = ROUTES.LOGIN;
    }
    return Promise.reject(error);
  }
);

export default apiClient;
