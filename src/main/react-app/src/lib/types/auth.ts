export interface AuthUser {
    id: string;
    email: string;
    firstName: string;
    lastName: string;
    role: UserRole;
}

export type UserRole = 'ADMIN' | 'STUDENT' | 'FACULTY' | 'STAFF';

export interface LoginCredentials {
    email: string;
    password: string;
}

export interface AuthResponse {
    user: AuthUser;
    token: string;
}

export interface AuthError {
    message: string;
    code?: string;
}

export interface AuthState {
    user: AuthUser | null;
    token: string | null;
    isLoading: boolean;
    error: AuthError | null;
} 