"use client";

import { cn } from "@/lib/utils";

interface LoadingProps {
    variant?: "spinner" | "dots" | "pulse";
    size?: "sm" | "md" | "lg";
    className?: string;
    fullScreen?: boolean;
}

export const Loading = ({
    variant = "spinner",
    size = "md",
    className,
    fullScreen = false
}: LoadingProps) => {
    const sizeClasses = {
        sm: "h-4 w-4",
        md: "h-8 w-8",
        lg: "h-12 w-12"
    };

    const Spinner = () => (
        <div
            className={cn(
                "animate-spin rounded-full border-2",
                "border-gray-300 border-t-primary-600",
                "dark:border-gray-600 dark:border-t-primary-400",
                sizeClasses[size],
                className
            )}
        />
    );

    const Dots = () => (
        <div className={cn("flex items-center gap-1", className)}>
            {[...Array(3)].map((_, i) => (
                <div
                    key={i}
                    className={cn(
                        "animate-pulse rounded-full bg-primary-600 dark:bg-primary-400",
                        size === "sm" && "h-1.5 w-1.5",
                        size === "md" && "h-2 w-2",
                        size === "lg" && "h-3 w-3"
                    )}
                    style={{
                        animationDelay: `${i * 150}ms`
                    }}
                />
            ))}
        </div>
    );

    const Pulse = () => (
        <div
            className={cn(
                "animate-pulse rounded-full bg-primary-600 dark:bg-primary-400",
                sizeClasses[size],
                className
            )}
        />
    );

    const LoadingComponent = {
        spinner: Spinner,
        dots: Dots,
        pulse: Pulse
    }[variant];

    if (fullScreen) {
        return (
            <div className="fixed inset-0 z-50 flex items-center justify-center bg-white/80 dark:bg-gray-900/80">
                <LoadingComponent />
            </div>
        );
    }

    return <LoadingComponent />;
}; 