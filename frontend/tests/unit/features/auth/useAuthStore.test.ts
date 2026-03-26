import { describe, it, expect, beforeEach } from 'vitest';
import useAuthStore from '@/features/auth/useAuthStore';
import { AUTH_STORAGE_KEY, ROLE_ADMIN, ROLE_STUDENT, SESSION_DURATION_MS } from '@/constants';

const ADMIN_USER = { username: 'admin', role: ROLE_ADMIN, studentId: null };

// Reset the store state between tests
beforeEach(() => {
  useAuthStore.getState().clearAuth();
});

describe('useAuthStore', () => {
  it('starts with no expiresAt and no user', () => {
    const { expiresAt, user } = useAuthStore.getState();
    expect(expiresAt).toBeNull();
    expect(user).toBeNull();
  });

  it('setAuth stores user and computes expiresAt', () => {
    const before = Date.now();
    useAuthStore.getState().setAuth(ADMIN_USER);
    const { expiresAt, user } = useAuthStore.getState();
    expect(user?.username).toBe('admin');
    expect(user?.role).toBe(ROLE_ADMIN);
    expect(user?.studentId).toBeNull();
    expect(expiresAt).toBeGreaterThanOrEqual(before + SESSION_DURATION_MS);
  });

  it('clearAuth resets to null', () => {
    useAuthStore.getState().setAuth(ADMIN_USER);
    useAuthStore.getState().clearAuth();
    expect(useAuthStore.getState().expiresAt).toBeNull();
    expect(useAuthStore.getState().user).toBeNull();
  });

  it('studentId is preserved', () => {
    useAuthStore.getState().setAuth(
      { username: 's1', role: ROLE_STUDENT, studentId: 7 },
    );
    expect(useAuthStore.getState().user?.studentId).toBe(7);
  });

  it('isExpired returns true when no expiresAt', () => {
    expect(useAuthStore.getState().isExpired()).toBe(true);
  });

  it('isExpired returns false after setAuth', () => {
    useAuthStore.getState().setAuth(ADMIN_USER);
    expect(useAuthStore.getState().isExpired()).toBe(false);
  });

  it('clearAuth persists nulled state to localStorage', () => {
    useAuthStore.getState().setAuth(ADMIN_USER);
    const before = JSON.parse(localStorage.getItem(AUTH_STORAGE_KEY)!);
    expect(before.state.expiresAt).toBeGreaterThan(0);

    useAuthStore.getState().clearAuth();

    const after = JSON.parse(localStorage.getItem(AUTH_STORAGE_KEY)!);
    expect(after.state.expiresAt).toBeNull();
    expect(after.state.user).toBeNull();
  });
});
