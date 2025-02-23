import {
    Users,
    GraduationCap,
    Building,
    FileText,
    ArrowUpRight,
    ArrowDownRight,
    Settings,
} from 'lucide-react'
import Link from 'next/link'

const stats = [
    {
        name: 'Total Students',
        value: '2,451',
        change: '+12.5%',
        increasing: true,
        href: '/dashboard/students',
        icon: Users,
    },
    {
        name: 'Active Internships',
        value: '185',
        change: '+8.2%',
        increasing: true,
        href: '/dashboard/internships',
        icon: GraduationCap,
    },
    {
        name: 'Partner Companies',
        value: '42',
        change: '+2.4%',
        increasing: true,
        href: '/dashboard/companies',
        icon: Building,
    },
    {
        name: 'Pending Reports',
        value: '12',
        change: '-4.5%',
        increasing: false,
        href: '/dashboard/reports',
        icon: FileText,
    },
]

const recentActivity = [
    {
        id: 1,
        type: 'student',
        name: 'John Smith',
        action: 'submitted an internship report',
        date: '2 hours ago',
    },
    {
        id: 2,
        type: 'company',
        name: 'Tech Corp',
        action: 'posted a new internship opportunity',
        date: '4 hours ago',
    },
    {
        id: 3,
        type: 'student',
        name: 'Sarah Johnson',
        action: 'applied for an internship',
        date: '5 hours ago',
    },
    {
        id: 4,
        type: 'admin',
        name: 'Admin',
        action: 'approved 3 internship applications',
        date: '1 day ago',
    },
]

export default function DashboardHome() {
    return (
        <div className="space-y-8">
            <div>
                <h2 className="text-2xl font-bold text-gray-900 dark:text-white">
                    Dashboard Overview
                </h2>
                <p className="mt-1 text-sm text-gray-500 dark:text-gray-400">
                    A summary of your internship management system
                </p>
            </div>

            {/* Stats */}
            <div className="grid grid-cols-1 gap-4 sm:grid-cols-2 lg:grid-cols-4">
                {stats.map((stat) => (
                    <Link
                        key={stat.name}
                        href={stat.href}
                        className="group relative overflow-hidden rounded-lg bg-white p-6 shadow transition hover:shadow-md dark:bg-gray-800"
                    >
                        <div className="flex items-center">
                            <div className="flex h-12 w-12 items-center justify-center rounded-lg bg-primary-50 dark:bg-primary-900">
                                <stat.icon className="h-6 w-6 text-primary-600 dark:text-primary-400" />
                            </div>
                            <div className="ml-4">
                                <p className="text-sm font-medium text-gray-500 dark:text-gray-400">
                                    {stat.name}
                                </p>
                                <p className="text-2xl font-semibold text-gray-900 dark:text-white">
                                    {stat.value}
                                </p>
                                <p
                                    className={`inline-flex items-baseline text-sm font-medium ${stat.increasing
                                        ? 'text-green-600 dark:text-green-400'
                                        : 'text-red-600 dark:text-red-400'
                                        }`}
                                >
                                    {stat.increasing ? (
                                        <ArrowUpRight className="mr-1 h-4 w-4" />
                                    ) : (
                                        <ArrowDownRight className="mr-1 h-4 w-4" />
                                    )}
                                    {stat.change}
                                </p>
                            </div>
                        </div>
                        <div className="absolute -right-12 -top-12 h-32 w-32 rounded-full bg-primary-50 opacity-20 transition-transform group-hover:scale-110 dark:bg-primary-900" />
                    </Link>
                ))}
            </div>

            {/* Recent Activity */}
            <div className="rounded-lg bg-white p-6 shadow dark:bg-gray-800">
                <h3 className="text-lg font-medium text-gray-900 dark:text-white">
                    Recent Activity
                </h3>
                <div className="mt-6 flow-root">
                    <ul className="-mb-8">
                        {recentActivity.map((activity, activityIdx) => (
                            <li key={activity.id}>
                                <div className="relative pb-8">
                                    {activityIdx !== recentActivity.length - 1 ? (
                                        <span
                                            className="absolute left-5 top-5 -ml-px h-full w-0.5 bg-gray-200 dark:bg-gray-700"
                                            aria-hidden="true"
                                        />
                                    ) : null}
                                    <div className="relative flex items-start space-x-3">
                                        <div
                                            className={`relative h-10 w-10 rounded-full bg-${activity.type === 'student'
                                                ? 'blue'
                                                : activity.type === 'company'
                                                    ? 'green'
                                                    : 'purple'
                                                }-50 dark:bg-${activity.type === 'student'
                                                    ? 'blue'
                                                    : activity.type === 'company'
                                                        ? 'green'
                                                        : 'purple'
                                                }-900`}
                                        >
                                            {activity.type === 'student' ? (
                                                <Users className="absolute left-1/2 top-1/2 h-5 w-5 -translate-x-1/2 -translate-y-1/2 text-blue-600 dark:text-blue-400" />
                                            ) : activity.type === 'company' ? (
                                                <Building className="absolute left-1/2 top-1/2 h-5 w-5 -translate-x-1/2 -translate-y-1/2 text-green-600 dark:text-green-400" />
                                            ) : (
                                                <Settings className="absolute left-1/2 top-1/2 h-5 w-5 -translate-x-1/2 -translate-y-1/2 text-purple-600 dark:text-purple-400" />
                                            )}
                                        </div>
                                        <div className="min-w-0 flex-1">
                                            <div>
                                                <div className="text-sm font-medium text-gray-900 dark:text-white">
                                                    {activity.name}
                                                </div>
                                                <p className="mt-0.5 text-sm text-gray-500 dark:text-gray-400">
                                                    {activity.action}
                                                </p>
                                            </div>
                                            <div className="mt-2 text-sm text-gray-500 dark:text-gray-400">
                                                {activity.date}
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </li>
                        ))}
                    </ul>
                </div>
            </div>
        </div>
    )
} 