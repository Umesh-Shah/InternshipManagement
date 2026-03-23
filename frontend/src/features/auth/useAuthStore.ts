import { create } from 'zustand';
import { persist } from 'zustand/middleware';

export type Role = 'ROLE_ADMIN' | 'ROLE_STUDENT';

const SESSION_DURATION = 8 * 60 * 60 * 1000; // 8 hours, mirrors server-side token lifetime

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
      setAuth: (user) => set({ user, expiresAt: Date.now() + SESSION_DURATION }),
      clearAuth: () => set({ user: null, expiresAt: null }),
      isExpired: () => {
        const { expiresAt } = get();
        return !expiresAt || expiresAt < Date.now();
      },
    }),
    { name: 'ims-auth' }
  )
);

export default useAuthStore;
