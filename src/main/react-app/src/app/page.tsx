import Link from 'next/link'

export default function Home() {
    return (
        <div className="flex min-h-[calc(100vh-4rem)] flex-col items-center justify-center bg-gradient-to-b from-white to-gray-50 px-4 dark:from-gray-950 dark:to-gray-900">
            <div className="container mx-auto max-w-4xl text-center">
                <h1 className="mb-6 text-4xl font-bold tracking-tight text-gray-900 dark:text-white sm:text-5xl">
                    Welcome to the Internship Management System
                </h1>
                <p className="mx-auto mb-8 max-w-2xl text-lg text-gray-600 dark:text-gray-300">
                    A comprehensive platform for managing student internships at the University of Windsor.
                    Connect with employers, track applications, and manage your internship journey.
                </p>
                <div className="flex flex-col items-center justify-center gap-4 sm:flex-row">
                    <Link
                        href="/login"
                        className="w-full rounded-lg bg-primary-600 px-8 py-3 text-center text-sm font-medium text-white hover:bg-primary-700 sm:w-auto"
                    >
                        Student Login
                    </Link>
                    <Link
                        href="/register"
                        className="w-full rounded-lg border border-gray-300 bg-white px-8 py-3 text-center text-sm font-medium text-gray-900 hover:bg-gray-50 dark:border-gray-700 dark:bg-gray-900 dark:text-white dark:hover:bg-gray-800 sm:w-auto"
                    >
                        Register Account
                    </Link>
                </div>
            </div>
        </div>
    )
} 