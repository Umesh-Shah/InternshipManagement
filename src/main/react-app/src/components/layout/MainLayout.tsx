"use client";

import { useTheme } from "@/hooks/useTheme";
import { useAuthContext } from '@/contexts/AuthContext';
import { cn } from "@/lib/utils";
import { Sun, Moon } from "lucide-react";
import { Navigation } from "./Navigation";

export const MainLayout: React.FC<{ children: React.ReactNode }> = ({ children }) => {
    const { theme, toggleTheme } = useTheme();
    const { isAuthenticated } = useAuthContext();

    return (
        <div className={cn(
            "min-h-screen w-full",
            "bg-white text-gray-900 dark:bg-gray-900 dark:text-gray-100",
            "transition-colors duration-200"
        )}>
            {/* Header */}
            <header className="border-b border-gray-200 dark:border-gray-800">
                <div className="container mx-auto px-4 py-4">
                    <div className="flex items-center justify-between">
                        <h1 className="text-2xl font-bold">Internship Management System</h1>
                        <button
                            onClick={toggleTheme}
                            className="p-2 rounded-full hover:bg-gray-100 dark:hover:bg-gray-800"
                        >
                            {theme === 'dark' ? <Sun size={20} /> : <Moon size={20} />}
                        </button>
                    </div>
                </div>
            </header>

            {/* Main Content */}
            <div className="flex h-[calc(100vh-4rem)]">
                {isAuthenticated && <Navigation />}
                <main className={cn(
                    "flex-1 overflow-auto",
                    isAuthenticated ? "p-6" : "container mx-auto px-4 py-8"
                )}>
                    {children}
                </main>
            </div>

            {/* Footer */}
            <footer className="border-t border-gray-200 dark:border-gray-800">
                <div className="container mx-auto px-4 py-6 text-center text-sm text-gray-600 dark:text-gray-400">
                    © {new Date().getFullYear()} Internship Management System. All rights reserved.
                </div>
            </footer>
        </div>
    );
}; 