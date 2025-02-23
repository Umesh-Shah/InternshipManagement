"use client";

import { useAuthContext } from '@/contexts/AuthContext';

export default function StudentDashboard() {
    const { user } = useAuthContext();

    return (
        <div className="space-y-6">
            <div className="bg-white dark:bg-gray-800 shadow rounded-lg p-6">
                <h1 className="text-2xl font-bold text-gray-900 dark:text-white">
                    Welcome back, {user?.firstName}!
                </h1>
                <p className="mt-2 text-gray-600 dark:text-gray-400">
                    This is your student dashboard. Here you can manage your internship applications and track your progress.
                </p>
            </div>

            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
                <div className="bg-white dark:bg-gray-800 shadow rounded-lg p-6">
                    <h2 className="text-xl font-semibold text-gray-900 dark:text-white">Applications</h2>
                    <p className="mt-2 text-gray-600 dark:text-gray-400">
                        Track your internship applications and their status.
                    </p>
                </div>

                <div className="bg-white dark:bg-gray-800 shadow rounded-lg p-6">
                    <h2 className="text-xl font-semibold text-gray-900 dark:text-white">Skills</h2>
                    <p className="mt-2 text-gray-600 dark:text-gray-400">
                        Update your skills and qualifications.
                    </p>
                </div>

                <div className="bg-white dark:bg-gray-800 shadow rounded-lg p-6">
                    <h2 className="text-xl font-semibold text-gray-900 dark:text-white">Profile</h2>
                    <p className="mt-2 text-gray-600 dark:text-gray-400">
                        Keep your profile information up to date.
                    </p>
                </div>
            </div>
        </div>
    );
} 