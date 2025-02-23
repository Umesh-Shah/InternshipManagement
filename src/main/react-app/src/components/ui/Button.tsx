"use client";

import { cn } from "@/lib/utils";

interface ButtonProps extends React.ButtonHTMLAttributes<HTMLButtonElement> {
    variant?: "primary" | "secondary" | "outline" | "danger";
    size?: "sm" | "md" | "lg";
    isLoading?: boolean;
}

export const Button = ({
    children,
    className,
    variant = "primary",
    size = "md",
    isLoading = false,
    disabled,
    ...props
}: ButtonProps) => {
    const variants = {
        primary: "bg-blue-600 hover:bg-blue-700 text-white",
        secondary: "bg-gray-600 hover:bg-gray-700 text-white",
        outline: "border border-gray-300 dark:border-gray-700 hover:bg-gray-100 dark:hover:bg-gray-800",
        danger: "bg-red-600 hover:bg-red-700 text-white"
    };

    const sizes = {
        sm: "px-3 py-1.5 text-sm",
        md: "px-4 py-2",
        lg: "px-6 py-3 text-lg"
    };

    return (
        <button
            className={cn(
                "rounded-md font-medium transition-colors duration-200",
                "focus:outline-none focus:ring-2 focus:ring-offset-2",
                variants[variant],
                sizes[size],
                "disabled:opacity-50 disabled:cursor-not-allowed",
                className
            )}
            disabled={disabled || isLoading}
            {...props}
        >
            {isLoading ? (
                <div className="flex items-center justify-center">
                    <svg
                        className="animate-spin -ml-1 mr-2 h-4 w-4"
                        xmlns="http://www.w3.org/2000/svg"
                        fill="none"
                        viewBox="0 0 24 24"
                    >
                        <circle
                            className="opacity-25"
                            cx="12"
                            cy="12"
                            r="10"
                            stroke="currentColor"
                            strokeWidth="4"
                        />
                        <path
                            className="opacity-75"
                            fill="currentColor"
                            d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"
                        />
                    </svg>
                    Loading...
                </div>
            ) : (
                children
            )}
        </button>
    );
}; 