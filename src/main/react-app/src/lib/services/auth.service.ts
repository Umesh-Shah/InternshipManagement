import { AuthResponse, LoginCredentials } from '../types/auth';

const API_URL = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8080/api';

export class AuthService {
    private static instance: AuthService;
    private tokenKey = 'auth_token';

    private constructor() { }

    static getInstance(): AuthService {
        if (!AuthService.instance) {
            AuthService.instance = new AuthService();
        }
        return AuthService.instance;
    }

    async login(credentials: LoginCredentials): Promise<AuthResponse> {
        try {
            const response = await fetch(`${API_URL}/auth/login`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(credentials),
            });

            if (!response.ok) {
                const error = await response.json();
                throw new Error(error.message || 'Login failed');
            }

            const data: AuthResponse = await response.json();
            this.setToken(data.token);
            return data;
        } catch (error) {
            throw error;
        }
    }

    async logout(): Promise<void> {
        try {
            await fetch(`${API_URL}/auth/logout`, {
                method: 'POST',
                headers: this.getAuthHeaders(),
            });
        } finally {
            this.removeToken();
        }
    }

    getToken(): string | null {
        if (typeof window !== 'undefined') {
            return localStorage.getItem(this.tokenKey);
        }
        return null;
    }

    setToken(token: string): void {
        if (typeof window !== 'undefined') {
            localStorage.setItem(this.tokenKey, token);
        }
    }

    removeToken(): void {
        if (typeof window !== 'undefined') {
            localStorage.removeItem(this.tokenKey);
        }
    }

    getAuthHeaders(): HeadersInit {
        const token = this.getToken();
        return {
            'Content-Type': 'application/json',
            'Authorization': token ? `Bearer ${token}` : '',
        };
    }

    isAuthenticated(): boolean {
        return !!this.getToken();
    }
}

export const authService = AuthService.getInstance(); 