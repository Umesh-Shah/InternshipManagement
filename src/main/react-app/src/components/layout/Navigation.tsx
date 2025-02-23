"use client";

import Link from 'next/link';
import { usePathname } from 'next/navigation';
import { useAuthContext } from '@/contexts/AuthContext';
import { cn } from '@/lib/utils';
import {
    Home,
    Users,
    FileText,
    Settings,
    LogOut,
    CreditCard,
    Image,
    Album,
} from 'lucide-react';

const navigationItems = [
    { name: 'Dashboard', href: '/dashboard', icon: Home },
    { name: 'Donors', href: '/donors', icon: Users },
    { name: 'Donations', href: '/donation', icon: CreditCard },
    { name: 'Balance Sheet', href: '/balance-sheet', icon: FileText },
    { name: 'Images', href: '/images', icon: Image },
    { name: 'Album', href: '/album', icon: Album },
    { name: 'Settings', href: '/settings', icon: Settings },
];

export const Navigation = () => {
    const pathname = usePathname();
    const { user, logout } = useAuthContext();

    return (
        <nav className="w-64 bg-white dark:bg-gray-800 border-r border-gray-200 dark:border-gray-700">
            <div className="h-16 flex items-center px-4 border-b border-gray-200 dark:border-gray-700">
                <span className="text-lg font-semibold text-gray-900 dark:text-white">
                    {user?.firstName} {user?.lastName}
                </span>
            </div>
            <div className="p-4">
                <ul className="space-y-2">
                    {navigationItems.map((item) => {
                        const Icon = item.icon;
                        return (
                            <li key={item.name}>
                                <Link
                                    href={item.href}
                                    className={cn(
                                        'flex items-center px-4 py-2 text-sm rounded-md',
                                        'text-gray-700 dark:text-gray-300 hover:bg-gray-100 dark:hover:bg-gray-700',
                                        pathname === item.href && 'bg-blue-100 dark:bg-blue-900 text-blue-600 dark:text-blue-300'
                                    )}
                                >
                                    <Icon className="w-5 h-5 mr-3" />
                                    {item.name}
                                </Link>
                            </li>
                        );
                    })}
                    <li>
                        <button
                            onClick={logout}
                            className="flex items-center w-full px-4 py-2 text-sm text-red-600 dark:text-red-400 hover:bg-red-50 dark:hover:bg-red-900/20 rounded-md"
                        >
                            <LogOut className="w-5 h-5 mr-3" />
                            Logout
                        </button>
                    </li>
                </ul>
            </div>
        </nav>
    );
}; 