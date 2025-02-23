import { useState, useCallback, useEffect } from 'react';
import { toast } from 'react-toastify';
import { AuthUser, LoginCredentials, AuthState, AuthError } from '@/lib/types/auth';
import { authService } from '@/lib/services/auth.service';

export const useAuth = () => {
    const [state, setState] = useState<AuthState>({
        user: null,
        token: null,
        isLoading: true,
        error: null,
    });

    useEffect(() => {
        const token = authService.getToken();
        if (token) {
            setState(prev => ({ ...prev, token, isLoading: false }));
        } else {
            setState(prev => ({ ...prev, isLoading: false }));
        }
    }, []);

    const login = useCallback(async (credentials: LoginCredentials) => {
        setState(prev => ({ ...prev, isLoading: true, error: null }));
        try {
            const response = await authService.login(credentials);
            setState(prev => ({
                ...prev,
                user: response.user,
                token: response.token,
                isLoading: false,
            }));
            toast.success('Successfully logged in!');
        } catch (error) {
            const authError: AuthError = {
                message: error instanceof Error ? error.message : 'An error occurred during login',
            };
            setState(prev => ({ ...prev, error: authError, isLoading: false }));
            toast.error(authError.message);
        }
    }, []);

    const logout = useCallback(async () => {
        setState(prev => ({ ...prev, isLoading: true }));
        try {
            await authService.logout();
            setState({
                user: null,
                token: null,
                isLoading: false,
                error: null,
            });
            toast.success('Successfully logged out!');
        } catch (error) {
            toast.error('Error during logout');
            setState(prev => ({ ...prev, isLoading: false }));
        }
    }, []);

    const updateUser = useCallback((user: AuthUser) => {
        setState(prev => ({ ...prev, user }));
    }, []);

    return {
        user: state.user,
        token: state.token,
        isLoading: state.isLoading,
        error: state.error,
        isAuthenticated: authService.isAuthenticated(),
        login,
        logout,
        updateUser,
    };
}; 