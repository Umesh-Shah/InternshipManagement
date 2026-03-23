import { create } from 'zustand';
import { persist } from 'zustand/middleware';

export type Role = 'ROLE_ADMIN' | 'ROLE_STUDENT';

interface AuthUser {
  username: string;
  role: Role;
  studentId: number | null;
}

interface AuthState {
  expiresAt: number | null;
  user: AuthUser | null;
  setAuth: (user: AuthUser, expiresAt: number) => void;
  clearAuth: () => void;
}

const useAuthStore = create<AuthState>()(
  persist(
    (set) => ({
      expiresAt: null,
      user: null,
      setAuth: (user, expiresAt) => set({ user, expiresAt }),
      clearAuth: () => set({ user: null, expiresAt: null }),
    }),
    { name: 'ims-auth' }
  )
);

export default useAuthStore;
