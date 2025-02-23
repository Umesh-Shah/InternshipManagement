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
                body: JSON.stringify({
                    username: credentials.email,
                    password: credentials.password
                }),
            });

            if (!response.ok) {
                const error = await response.json();
                throw new Error(error.message || 'Login failed');
            }

            const { token } = await response.json();

            // Get user info with the token
            const userResponse = await fetch(`${API_URL}/auth/me`, {
                headers: {
                    'Authorization': `Bearer ${token}`,
                    'Content-Type': 'application/json',
                },
            });

            if (!userResponse.ok) {
                throw new Error('Failed to get user info');
            }

            const user = await userResponse.json();
            const authResponse: AuthResponse = { user, token };

            this.setToken(token);
            return authResponse;
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