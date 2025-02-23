import { authService } from './auth.service';

const API_URL = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8080/api';

export class ApiService {
    private static instance: ApiService;

    private constructor() { }

    static getInstance(): ApiService {
        if (!ApiService.instance) {
            ApiService.instance = new ApiService();
        }
        return ApiService.instance;
    }

    private async handleResponse<T>(response: Response): Promise<T> {
        if (!response.ok) {
            if (response.status === 401) {
                authService.removeToken();
                window.location.href = '/login';
            }
            const error = await response.json();
            throw new Error(error.message || 'An error occurred');
        }
        return response.json();
    }

    async get<T>(endpoint: string): Promise<T> {
        const response = await fetch(`${API_URL}${endpoint}`, {
            headers: authService.getAuthHeaders(),
        });
        return this.handleResponse<T>(response);
    }

    async post<T>(endpoint: string, data?: any): Promise<T> {
        const response = await fetch(`${API_URL}${endpoint}`, {
            method: 'POST',
            headers: authService.getAuthHeaders(),
            body: data ? JSON.stringify(data) : undefined,
        });
        return this.handleResponse<T>(response);
    }

    async put<T>(endpoint: string, data: any): Promise<T> {
        const response = await fetch(`${API_URL}${endpoint}`, {
            method: 'PUT',
            headers: authService.getAuthHeaders(),
            body: JSON.stringify(data),
        });
        return this.handleResponse<T>(response);
    }

    async delete<T>(endpoint: string): Promise<T> {
        const response = await fetch(`${API_URL}${endpoint}`, {
            method: 'DELETE',
            headers: authService.getAuthHeaders(),
        });
        return this.handleResponse<T>(response);
    }
}

export const apiService = ApiService.getInstance(); 