import apiClient from './client';
import type { Role } from '@/features/auth/useAuthStore';

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
  apiClient.post<LoginResponse>('/auth/login', data).then((r) => r.data);

export const logout = () =>
  apiClient.post('/auth/logout').then(() => undefined);
