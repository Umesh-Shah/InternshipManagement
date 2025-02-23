"use client";

import { Inter } from "next/font/google";
import { AuthProvider } from "@/contexts/AuthContext";
import { MainLayout } from "@/components/layout/MainLayout";
import { ToastProvider } from "@/components/providers/ToastProvider";
import { ErrorBoundary } from "@/components/ui/ErrorBoundary";

import "./globals.css";

const inter = Inter({ subsets: ["latin"] });

export default function RootLayout({
    children,
}: {
    children: React.ReactNode;
}) {
    return (
        <html lang="en" suppressHydrationWarning>
            <body className={inter.className}>
                <ErrorBoundary>
                    <AuthProvider>
                        <ToastProvider>
                            <MainLayout>
                                {children}
                            </MainLayout>
                        </ToastProvider>
                    </AuthProvider>
                </ErrorBoundary>
            </body>
        </html>
    );
} 