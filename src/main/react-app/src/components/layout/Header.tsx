"use client"

import { useTheme } from 'next-themes'
import Link from 'next/link'
import Image from 'next/image'
import { usePathname } from 'next/navigation'
import { Sun, Moon, Menu } from 'lucide-react'
import { useState } from 'react'

interface HeaderProps {
    isLoggedIn?: boolean
    userName?: string
}

export default function Header({ isLoggedIn, userName }: HeaderProps) {
    const { theme, setTheme } = useTheme()
    const [isMenuOpen, setIsMenuOpen] = useState(false)
    const pathname = usePathname()

    return (
        <header className="sticky top-0 z-50 w-full border-b border-gray-200 bg-white/75 backdrop-blur-lg dark:border-gray-800 dark:bg-gray-950/75">
            <div className="container mx-auto px-4">
                <div className="flex h-16 items-center justify-between">
                    <div className="flex items-center gap-4">
                        <Image
                            src="/img/u.png"
                            alt="University Logo"
                            width={48}
                            height={48}
                            className="h-12 w-auto"
                        />
                        <h1 className="hidden text-xl font-semibold text-gray-900 dark:text-white sm:block">
                            Internship Management System
                        </h1>
                    </div>

                    <div className="flex items-center gap-4">
                        <button
                            onClick={() => setTheme(theme === 'dark' ? 'light' : 'dark')}
                            className="rounded-full p-2 text-gray-500 hover:bg-gray-100 dark:text-gray-400 dark:hover:bg-gray-800"
                        >
                            {theme === 'dark' ? <Sun size={20} /> : <Moon size={20} />}
                        </button>

                        <div className="hidden sm:flex sm:items-center sm:gap-4">
                            {isLoggedIn ? (
                                <>
                                    <span className="text-sm text-gray-700 dark:text-gray-300">
                                        Welcome, {userName}
                                    </span>
                                    <Link
                                        href="/logout"
                                        className="rounded-md bg-primary-600 px-4 py-2 text-sm font-medium text-white hover:bg-primary-700"
                                    >
                                        Logout
                                    </Link>
                                </>
                            ) : (
                                <Link
                                    href="/login"
                                    className="rounded-md bg-primary-600 px-4 py-2 text-sm font-medium text-white hover:bg-primary-700"
                                >
                                    Login
                                </Link>
                            )}
                        </div>

                        <button
                            onClick={() => setIsMenuOpen(!isMenuOpen)}
                            className="rounded-md p-2 text-gray-500 hover:bg-gray-100 dark:text-gray-400 dark:hover:bg-gray-800 sm:hidden"
                        >
                            <Menu size={24} />
                        </button>
                    </div>
                </div>
            </div>

            {/* Mobile menu */}
            {isMenuOpen && (
                <div className="border-t border-gray-200 bg-white dark:border-gray-800 dark:bg-gray-950 sm:hidden">
                    <div className="container mx-auto space-y-1 px-4 py-3">
                        {isLoggedIn ? (
                            <>
                                <p className="px-3 py-2 text-sm text-gray-700 dark:text-gray-300">
                                    Welcome, {userName}
                                </p>
                                <Link
                                    href="/logout"
                                    className="block rounded-md px-3 py-2 text-base font-medium text-gray-900 hover:bg-gray-100 dark:text-white dark:hover:bg-gray-800"
                                >
                                    Logout
                                </Link>
                            </>
                        ) : (
                            <Link
                                href="/login"
                                className="block rounded-md px-3 py-2 text-base font-medium text-gray-900 hover:bg-gray-100 dark:text-white dark:hover:bg-gray-800"
                            >
                                Login
                            </Link>
                        )}
                    </div>
                </div>
            )}
        </header>
    )
} 