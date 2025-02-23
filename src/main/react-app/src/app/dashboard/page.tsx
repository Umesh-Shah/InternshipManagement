'use client';

import { useAuthContext } from '@/contexts/AuthContext';
import { useRouter } from 'next/navigation';
import { Card } from '@/components/ui/Card';
import { Button } from '@/components/ui/Button';

export default function DashboardPage() {
    const { user, logout } = useAuthContext();
    const router = useRouter();

    const handleLogout = async () => {
        await logout();
        router.push('/login');
    };

    return (
        <div className="min-h-screen bg-gray-50 dark:bg-gray-900">
            <nav className="bg-white shadow dark:bg-gray-800">
                <div className="mx-auto max-w-7xl px-4 sm:px-6 lg:px-8">
                    <div className="flex h-16 justify-between">
                        <div className="flex items-center">
                            <h1 className="text-xl font-bold text-gray-900 dark:text-white">
                                Dashboard
                            </h1>
                        </div>
                        <div className="flex items-center">
                            <span className="mr-4 text-gray-700 dark:text-gray-300">
                                Welcome, {user?.firstName} {user?.lastName}
                            </span>
                            <Button
                                onClick={handleLogout}
                                variant="danger"
                                size="sm"
                            >
                                Logout
                            </Button>
                        </div>
                    </div>
                </div>
            </nav>

            <main className="mx-auto max-w-7xl py-6 sm:px-6 lg:px-8">
                <div className="px-4 py-6 sm:px-0">
                    <Card className="h-96">
                        <h2 className="mb-4 text-2xl font-bold text-gray-900 dark:text-white">
                            Welcome to your Dashboard
                        </h2>
                        <p className="text-gray-600 dark:text-gray-400">
                            This is a protected page that can only be accessed by authenticated users.
                        </p>
                    </Card>
                </div>
            </main>
        </div>
    );
} 