import { describe, it, expect, beforeEach } from 'vitest';
import { render, screen } from '@testing-library/react';
import { MemoryRouter, Routes, Route } from 'react-router-dom';
import useAuthStore from '@/features/auth/useAuthStore';
import { ROLE_ADMIN, ROLE_STUDENT, ROUTES } from '@/constants';
import ProtectedRoute from '@/router/ProtectedRoute';

beforeEach(() => {
  useAuthStore.getState().clearAuth();
});

function renderWithRoute(requiredRole?: string) {
  return render(
    <MemoryRouter initialEntries={['/protected']}>
      <Routes>
        <Route element={<ProtectedRoute requiredRole={requiredRole} />}>
          <Route path="/protected" element={<div>Protected content</div>} />
        </Route>
        <Route path={ROUTES.LOGIN} element={<div>Login page</div>} />
        <Route path={ROUTES.ADMIN} element={<div>Admin page</div>} />
        <Route path={ROUTES.STUDENT} element={<div>Student page</div>} />
      </Routes>
    </MemoryRouter>
  );
}

describe('ProtectedRoute', () => {
  it('redirects to login when unauthenticated', () => {
    renderWithRoute();
    expect(screen.getByText('Login page')).toBeInTheDocument();
  });

  it('renders children when authenticated with no role requirement', () => {
    useAuthStore.getState().setAuth({ username: 'admin', role: ROLE_ADMIN, studentId: null });
    renderWithRoute();
    expect(screen.getByText('Protected content')).toBeInTheDocument();
  });

  it('renders children when role matches', () => {
    useAuthStore.getState().setAuth({ username: 'admin', role: ROLE_ADMIN, studentId: null });
    renderWithRoute(ROLE_ADMIN);
    expect(screen.getByText('Protected content')).toBeInTheDocument();
  });

  it('redirects admin to /admin when student route is required', () => {
    useAuthStore.getState().setAuth({ username: 'admin', role: ROLE_ADMIN, studentId: null });
    renderWithRoute(ROLE_STUDENT);
    expect(screen.getByText('Admin page')).toBeInTheDocument();
  });

  it('redirects student to /student when admin route is required', () => {
    useAuthStore.getState().setAuth({ username: 'alice', role: ROLE_STUDENT, studentId: 1 });
    renderWithRoute(ROLE_ADMIN);
    expect(screen.getByText('Student page')).toBeInTheDocument();
  });
});
