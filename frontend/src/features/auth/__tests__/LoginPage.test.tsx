import { describe, it, expect, vi, beforeEach } from 'vitest';
import { render, screen, waitFor } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { MemoryRouter } from 'react-router-dom';
import LoginPage from '../LoginPage';
import * as authApi from '@/api/auth.api';
import useAuthStore from '../useAuthStore';

// Mock the login API call
vi.mock('@/api/auth.api');
const mockLogin = vi.mocked(authApi.login);

// Mock react-router-dom navigate
const mockNavigate = vi.fn();
vi.mock('react-router-dom', async (importOriginal) => {
  const actual = await importOriginal<typeof import('react-router-dom')>();
  return { ...actual, useNavigate: () => mockNavigate };
});

function renderLoginPage() {
  return render(
    <MemoryRouter>
      <LoginPage />
    </MemoryRouter>
  );
}

beforeEach(() => {
  vi.clearAllMocks();
  useAuthStore.getState().clearAuth();
});

describe('LoginPage', () => {
  it('renders username and password fields', () => {
    renderLoginPage();
    expect(screen.getByLabelText(/username/i)).toBeInTheDocument();
    expect(screen.getByLabelText(/password/i)).toBeInTheDocument();
    expect(screen.getByRole('button', { name: /sign in/i })).toBeInTheDocument();
  });

  it('shows validation error when username is empty', async () => {
    renderLoginPage();
    await userEvent.click(screen.getByRole('button', { name: /sign in/i }));
    expect(await screen.findByText(/username is required/i)).toBeInTheDocument();
  });

  it('shows validation error when password is empty', async () => {
    renderLoginPage();
    await userEvent.type(screen.getByLabelText(/username/i), 'admin');
    await userEvent.click(screen.getByRole('button', { name: /sign in/i }));
    expect(await screen.findByText(/password is required/i)).toBeInTheDocument();
  });

  it('navigates to /admin on successful admin login', async () => {
    mockLogin.mockResolvedValueOnce({
      token: 'tok',
      role: 'ROLE_ADMIN',
      studentId: null,
      username: 'admin',
    });

    renderLoginPage();
    await userEvent.type(screen.getByLabelText(/username/i), 'admin');
    await userEvent.type(screen.getByLabelText(/password/i), 'secret');
    await userEvent.click(screen.getByRole('button', { name: /sign in/i }));

    await waitFor(() => {
      expect(mockNavigate).toHaveBeenCalledWith('/admin', { replace: true });
    });
    expect(useAuthStore.getState().token).toBe('tok');
  });

  it('navigates to /student on successful student login', async () => {
    mockLogin.mockResolvedValueOnce({
      token: 'stok',
      role: 'ROLE_STUDENT',
      studentId: 3,
      username: 'student1',
    });

    renderLoginPage();
    await userEvent.type(screen.getByLabelText(/username/i), 'student1');
    await userEvent.type(screen.getByLabelText(/password/i), 'pass');
    await userEvent.click(screen.getByRole('button', { name: /sign in/i }));

    await waitFor(() => {
      expect(mockNavigate).toHaveBeenCalledWith('/student', { replace: true });
    });
  });

  it('shows error message on bad credentials', async () => {
    mockLogin.mockRejectedValueOnce(new Error('401'));

    renderLoginPage();
    await userEvent.type(screen.getByLabelText(/username/i), 'admin');
    await userEvent.type(screen.getByLabelText(/password/i), 'wrong');
    await userEvent.click(screen.getByRole('button', { name: /sign in/i }));

    expect(await screen.findByText(/invalid username or password/i)).toBeInTheDocument();
    expect(mockNavigate).not.toHaveBeenCalled();
  });
});
