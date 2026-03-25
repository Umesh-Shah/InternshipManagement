import apiClient from './client';
import type { Role } from '@/features/auth/useAuthStore';
import { API_PATHS } from '@/constants';

export interface LoginRequest {
  username: string;
  password: string;
}

export interface LoginResponse {
  role: Role;
  studentId: number | null;
  username: string;
}

export const login = (data: LoginRequest) =>
  apiClient.post<LoginResponse>(API_PATHS.AUTH_LOGIN, data).then((r) => r.data);

export const logout = () =>
  apiClient.post(API_PATHS.AUTH_LOGOUT).then(() => undefined);
