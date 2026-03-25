import { create } from 'zustand';
import { persist } from 'zustand/middleware';
import { AUTH_STORAGE_KEY, SESSION_DURATION_MS } from '@/constants';

export type Role = 'ROLE_ADMIN' | 'ROLE_STUDENT';

interface AuthUser {
  username: string;
  role: Role;
  studentId: number | null;
}

interface AuthState {
  expiresAt: number | null;
  user: AuthUser | null;
  setAuth: (user: AuthUser) => void;
  clearAuth: () => void;
  isExpired: () => boolean;
}

const useAuthStore = create<AuthState>()(
  persist(
    (set, get) => ({
      expiresAt: null,
      user: null,
      setAuth: (user) => set({ user, expiresAt: Date.now() + SESSION_DURATION_MS }),
      clearAuth: () => set({ user: null, expiresAt: null }),
      isExpired: () => {
        const { expiresAt } = get();
        return !expiresAt || expiresAt < Date.now();
      },
    }),
    { name: AUTH_STORAGE_KEY }
  )
);

export default useAuthStore;
