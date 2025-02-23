"use client";

import { Component, type ReactNode } from "react";
import { AlertTriangle } from "lucide-react";
import { Button } from "./Button";

interface Props {
    children: ReactNode;
    fallback?: ReactNode;
}

interface State {
    hasError: boolean;
    error: Error | null;
}

export class ErrorBoundary extends Component<Props, State> {
    constructor(props: Props) {
        super(props);
        this.state = {
            hasError: false,
            error: null
        };
    }

    static getDerivedStateFromError(error: Error): State {
        return {
            hasError: true,
            error
        };
    }

    componentDidCatch(error: Error, errorInfo: React.ErrorInfo) {
        // Log error to error reporting service
        console.error("Error caught by ErrorBoundary:", error, errorInfo);
    }

    handleRetry = () => {
        this.setState({
            hasError: false,
            error: null
        });
    };

    render() {
        const { hasError, error } = this.state;
        const { children, fallback } = this.props;

        if (hasError) {
            if (fallback) {
                return fallback;
            }

            return (
                <div className="flex min-h-[400px] flex-col items-center justify-center p-4 text-center">
                    <div className="mb-4 rounded-full bg-red-100 p-3 dark:bg-red-900">
                        <AlertTriangle className="h-6 w-6 text-red-600 dark:text-red-400" />
                    </div>
                    <h2 className="mb-2 text-lg font-semibold text-gray-900 dark:text-white">
                        Something went wrong
                    </h2>
                    <p className="mb-4 max-w-md text-sm text-gray-600 dark:text-gray-300">
                        {error?.message || "An unexpected error occurred"}
                    </p>
                    <div className="flex gap-3">
                        <Button
                            onClick={this.handleRetry}
                            variant="primary"
                        >
                            Try again
                        </Button>
                        <Button
                            onClick={() => window.location.reload()}
                            variant="outline"
                        >
                            Refresh page
                        </Button>
                    </div>
                </div>
            );
        }

        return children;
    }
} 