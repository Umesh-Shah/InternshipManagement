"use client";

import { cn } from "@/lib/utils";

interface CardProps {
    children: React.ReactNode;
    title?: string;
    className?: string;
}

export const Card = ({ children, title, className }: CardProps) => {
    return (
        <div className={cn(
            "rounded-lg border border-gray-200 bg-white p-6",
            "dark:border-gray-700 dark:bg-gray-800",
            className
        )}>
            {title && (
                <h2 className="mb-4 text-xl font-semibold text-gray-900 dark:text-white">
                    {title}
                </h2>
            )}
            {children}
        </div>
    );
}; 