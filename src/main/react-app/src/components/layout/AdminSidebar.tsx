"use client"

import { useState } from 'react'
import Link from 'next/link'
import { usePathname } from 'next/navigation'
import {
    FileText,
    ChevronLeft,
    ChevronRight,
    Users,
    ImageIcon,
    FileSpreadsheet,
    Key,
    LucideIcon
} from 'lucide-react'

export interface NavigationItem {
    name: string
    href: string
    icon: LucideIcon
}

const defaultNavigation: NavigationItem[] = [
    {
        name: 'Donation Form',
        href: '/admin/donation',
        icon: FileText,
    },
    {
        name: 'Manage Donors',
        href: '/admin/donors',
        icon: Users,
    },
    {
        name: 'Add Balance Sheet',
        href: '/admin/balance-sheet/add',
        icon: FileSpreadsheet,
    },
    {
        name: 'Manage Balance Sheet',
        href: '/admin/balance-sheet',
        icon: FileSpreadsheet,
    },
    {
        name: 'Add Album',
        href: '/admin/album/add',
        icon: ImageIcon,
    },
    {
        name: 'Add Images',
        href: '/admin/images/add',
        icon: ImageIcon,
    },
    {
        name: 'Change Password',
        href: '/admin/change-password',
        icon: Key,
    },
]

export interface AdminSidebarProps {
    navigation?: NavigationItem[]
}

export default function AdminSidebar({ navigation = defaultNavigation }: AdminSidebarProps) {
    const [isCollapsed, setIsCollapsed] = useState(false)
    const pathname = usePathname()

    return (
        <aside
            className={`relative flex flex-col border-r border-gray-200 bg-white transition-all duration-300 dark:border-gray-800 dark:bg-gray-900 ${isCollapsed ? 'w-16' : 'w-64'
                }`}
        >
            <div className="flex flex-1 flex-col overflow-y-auto pt-5">
                <nav className="flex-1 space-y-1 px-2">
                    {navigation.map((item) => {
                        const isActive = pathname === item.href
                        return (
                            <Link
                                key={item.name}
                                href={item.href}
                                className={`group flex items-center rounded-md px-2 py-2 text-sm font-medium ${isActive
                                        ? 'bg-gray-100 text-gray-900 dark:bg-gray-800 dark:text-white'
                                        : 'text-gray-600 hover:bg-gray-50 hover:text-gray-900 dark:text-gray-300 dark:hover:bg-gray-800 dark:hover:text-white'
                                    }`}
                            >
                                <item.icon
                                    className={`mr-3 h-5 w-5 flex-shrink-0 ${isActive
                                            ? 'text-gray-500 dark:text-gray-300'
                                            : 'text-gray-400 group-hover:text-gray-500 dark:text-gray-400 dark:group-hover:text-gray-300'
                                        }`}
                                    aria-hidden="true"
                                />
                                <span
                                    className={`truncate ${isCollapsed ? 'hidden' : 'block'}`}
                                >
                                    {item.name}
                                </span>
                            </Link>
                        )
                    })}
                </nav>
            </div>
            <button
                onClick={() => setIsCollapsed(!isCollapsed)}
                className="absolute -right-4 top-4 flex h-8 w-8 items-center justify-center rounded-full border border-gray-200 bg-white text-gray-500 hover:bg-gray-50 dark:border-gray-700 dark:bg-gray-800 dark:text-gray-400 dark:hover:bg-gray-700"
            >
                {isCollapsed ? (
                    <ChevronRight className="h-4 w-4" />
                ) : (
                    <ChevronLeft className="h-4 w-4" />
                )}
            </button>
        </aside>
    )
} 