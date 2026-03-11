import apiClient from './client';

export interface LoginRequest {
  username: string;
  password: string;
}

export interface LoginResponse {
  token: string;
  role: string;
  studentId: number | null;
  username: string;
}

export const login = (data: LoginRequest) =>
  apiClient.post<LoginResponse>('/auth/login', data).then((r) => r.data);
