import { describe, it, expect, beforeEach } from 'vitest';
import useAuthStore from '../useAuthStore';

// Reset the store state between tests
beforeEach(() => {
  useAuthStore.getState().clearAuth();
});

describe('useAuthStore', () => {
  it('starts with no token and no user', () => {
    const { token, user } = useAuthStore.getState();
    expect(token).toBeNull();
    expect(user).toBeNull();
  });

  it('setAuth stores token and user', () => {
    useAuthStore.getState().setAuth('jwt-abc', {
      username: 'admin',
      role: 'ROLE_ADMIN',
      studentId: null,
    });
    const { token, user } = useAuthStore.getState();
    expect(token).toBe('jwt-abc');
    expect(user?.username).toBe('admin');
    expect(user?.role).toBe('ROLE_ADMIN');
    expect(user?.studentId).toBeNull();
  });

  it('clearAuth resets to null', () => {
    useAuthStore.getState().setAuth('tok', { username: 'u', role: 'ROLE_STUDENT', studentId: 42 });
    useAuthStore.getState().clearAuth();
    expect(useAuthStore.getState().token).toBeNull();
    expect(useAuthStore.getState().user).toBeNull();
  });

  it('studentId is preserved', () => {
    useAuthStore.getState().setAuth('t', { username: 's1', role: 'ROLE_STUDENT', studentId: 7 });
    expect(useAuthStore.getState().user?.studentId).toBe(7);
  });
});
