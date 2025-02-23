"use client";

import { useAuth } from '@/contexts/AuthContext';
import { cn } from '@/lib/utils';
import Link from 'next/link';
import { usePathname } from 'next/navigation';
import { LogOut, User, Briefcase, GraduationCap, Settings } from 'lucide-react';

export const Navigation = () => {
    const { user, logout } = useAuth();
    const pathname = usePathname();

    const studentMenuItems = [
        {
            label: 'Dashboard',
            href: '/student/dashboard',
            icon: User
        },
        {
            label: 'Jobs',
            href: '/student/jobs',
            icon: Briefcase
        },
        {
            label: 'Skills',
            href: '/student/skills',
            icon: GraduationCap
        },
        {
            label: 'Settings',
            href: '/student/settings',
            icon: Settings
        }
    ];

    const adminMenuItems = [
        {
            label: 'Dashboard',
            href: '/admin/dashboard',
            icon: User
        },
        {
            label: 'Students',
            href: '/admin/students',
            icon: GraduationCap
        },
        {
            label: 'Jobs',
            href: '/admin/jobs',
            icon: Briefcase
        },
        {
            label: 'Settings',
            href: '/admin/settings',
            icon: Settings
        }
    ];

    const menuItems = user?.role === 'admin' ? adminMenuItems : studentMenuItems;

    return (
        <nav className="h-full w-64 bg-white dark:bg-gray-800 border-r border-gray-200 dark:border-gray-700">
            <div className="p-4">
                <div className="space-y-4">
                    {menuItems.map((item) => {
                        const Icon = item.icon;
                        return (
                            <Link
                                key={item.href}
                                href={item.href}
                                className={cn(
                                    "flex items-center space-x-3 px-3 py-2 rounded-md transition-colors",
                                    pathname === item.href
                                        ? "bg-blue-50 text-blue-600 dark:bg-blue-900/50 dark:text-blue-400"
                                        : "text-gray-700 dark:text-gray-300 hover:bg-gray-100 dark:hover:bg-gray-700"
                                )}
                            >
                                <Icon size={20} />
                                <span>{item.label}</span>
                            </Link>
                        );
                    })}

                    <button
                        onClick={() => logout()}
                        className={cn(
                            "flex items-center space-x-3 px-3 py-2 rounded-md w-full transition-colors",
                            "text-gray-700 dark:text-gray-300 hover:bg-gray-100 dark:hover:bg-gray-700"
                        )}
                    >
                        <LogOut size={20} />
                        <span>Logout</span>
                    </button>
                </div>
            </div>
        </nav>
    );
}; 