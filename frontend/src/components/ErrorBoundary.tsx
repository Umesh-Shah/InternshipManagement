import { Component } from 'react';
import type { ErrorInfo, ReactNode } from 'react';
import { ROUTES } from '@/constants';

interface Props {
  children: ReactNode;
}

interface State {
  hasError: boolean;
  message: string;
}

export class ErrorBoundary extends Component<Props, State> {
  state: State = { hasError: false, message: '' };

  static getDerivedStateFromError(error: unknown): State {
    const message = error instanceof Error ? error.message : String(error);
    return { hasError: true, message };
  }

  componentDidCatch(error: unknown, info: ErrorInfo) {
    console.error('[ErrorBoundary]', error, info.componentStack);
  }

  render() {
    if (this.state.hasError) {
      return (
        <div className="flex flex-col items-center justify-center min-h-screen gap-4 p-8 text-center">
          <h1 className="text-2xl font-semibold text-destructive">Something went wrong</h1>
          <p className="text-muted-foreground text-sm max-w-md">{this.state.message}</p>
          <button
            className="px-4 py-2 rounded bg-primary text-primary-foreground text-sm"
            onClick={() => window.location.assign(ROUTES.LOGIN)}
          >
            Back to login
          </button>
        </div>
      );
    }
    return this.props.children;
  }
}
