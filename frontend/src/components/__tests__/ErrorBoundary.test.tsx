import { describe, it, expect, vi, beforeEach } from 'vitest';
import { render, screen } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { ErrorBoundary } from '../ErrorBoundary';
import { ROUTES } from '@/constants';

// Suppress React's error boundary console output in test logs
beforeEach(() => {
  vi.spyOn(console, 'error').mockImplementation(() => {});
});

function Boom({ message }: { message: string }) {
  throw new Error(message);
}

describe('ErrorBoundary', () => {
  it('renders children when there is no error', () => {
    render(
      <ErrorBoundary>
        <p>All good</p>
      </ErrorBoundary>
    );
    expect(screen.getByText('All good')).toBeInTheDocument();
  });

  it('renders fallback UI when a child throws', () => {
    render(
      <ErrorBoundary>
        <Boom message="test error" />
      </ErrorBoundary>
    );
    expect(screen.getByText('Something went wrong')).toBeInTheDocument();
    expect(screen.getByText('test error')).toBeInTheDocument();
  });

  it('shows the "Back to login" button in the fallback', () => {
    render(
      <ErrorBoundary>
        <Boom message="oops" />
      </ErrorBoundary>
    );
    expect(screen.getByRole('button', { name: /back to login/i })).toBeInTheDocument();
  });

  it('"Back to login" button navigates to the login route', async () => {
    // jsdom marks window.location.assign as non-configurable — replace the whole object
    const assignMock = vi.fn();
    vi.stubGlobal('location', { assign: assignMock });
    render(
      <ErrorBoundary>
        <Boom message="oops" />
      </ErrorBoundary>
    );
    await userEvent.click(screen.getByRole('button', { name: /back to login/i }));
    expect(assignMock).toHaveBeenCalledWith(ROUTES.LOGIN);
    vi.unstubAllGlobals();
  });
});
