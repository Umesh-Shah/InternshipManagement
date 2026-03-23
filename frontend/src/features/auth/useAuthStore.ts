import { create } from 'zustand';
import { persist } from 'zustand/middleware';

export type Role = 'ROLE_ADMIN' | 'ROLE_STUDENT';

interface AuthUser {
  username: string;
  role: Role;
  studentId: number | null;
}

interface AuthState {
  token: string | null;
  user: AuthUser | null;
  setAuth: (token: string, user: AuthUser) => void;
  clearAuth: () => void;
}

const useAuthStore = create<AuthState>()(
  persist(
    (set) => ({
      token: null,
      user: null,
      setAuth: (token, user) => set({ token, user }),
      clearAuth: () => set({ token: null, user: null }),
    }),
    { name: 'ims-auth' }
  )
);

export default useAuthStore;
