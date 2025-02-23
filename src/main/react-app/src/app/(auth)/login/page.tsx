"use client"

import { useState } from 'react'
import { useAuth } from '@/contexts/AuthContext'
import { useRouter } from 'next/navigation'
import { toast } from 'react-toastify'
import { cn } from '@/lib/utils'
import { LogIn } from 'lucide-react'

export default function LoginPage() {
    const [formData, setFormData] = useState({
        email: '',
        password: ''
    })
    const [isSubmitting, setIsSubmitting] = useState(false)
    const { login } = useAuth()
    const router = useRouter()

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault()
        setIsSubmitting(true)

        try {
            await login(formData.email, formData.password)
            router.push('/student/dashboard')
        } catch (error) {
            // Error handling is done in the AuthContext
        } finally {
            setIsSubmitting(false)
        }
    }

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const { name, value } = e.target
        setFormData(prev => ({
            ...prev,
            [name]: value
        }))
    }

    return (
        <div className="min-h-screen flex items-center justify-center px-4">
            <div className="max-w-md w-full space-y-8">
                <div className="text-center">
                    <h2 className="text-3xl font-bold">Welcome Back</h2>
                    <p className="mt-2 text-gray-600 dark:text-gray-400">
                        Sign in to your account to continue
                    </p>
                </div>

                <form onSubmit={handleSubmit} className="mt-8 space-y-6">
                    <div className="space-y-4">
                        <div>
                            <label htmlFor="email" className="block text-sm font-medium">
                                Email Address
                            </label>
                            <input
                                id="email"
                                name="email"
                                type="email"
                                autoComplete="email"
                                required
                                value={formData.email}
                                onChange={handleChange}
                                className={cn(
                                    "mt-1 block w-full px-3 py-2 border rounded-md",
                                    "bg-white dark:bg-gray-800",
                                    "border-gray-300 dark:border-gray-700",
                                    "focus:outline-none focus:ring-2 focus:ring-blue-500"
                                )}
                            />
                        </div>

                        <div>
                            <label htmlFor="password" className="block text-sm font-medium">
                                Password
                            </label>
                            <input
                                id="password"
                                name="password"
                                type="password"
                                autoComplete="current-password"
                                required
                                value={formData.password}
                                onChange={handleChange}
                                className={cn(
                                    "mt-1 block w-full px-3 py-2 border rounded-md",
                                    "bg-white dark:bg-gray-800",
                                    "border-gray-300 dark:border-gray-700",
                                    "focus:outline-none focus:ring-2 focus:ring-blue-500"
                                )}
                            />
                        </div>
                    </div>

                    <div>
                        <button
                            type="submit"
                            disabled={isSubmitting}
                            className={cn(
                                "w-full flex justify-center items-center gap-2",
                                "px-4 py-2 rounded-md text-white",
                                "bg-blue-600 hover:bg-blue-700",
                                "focus:outline-none focus:ring-2 focus:ring-blue-500",
                                "disabled:opacity-50 disabled:cursor-not-allowed"
                            )}
                        >
                            {isSubmitting ? (
                                <div className="animate-spin rounded-full h-5 w-5 border-b-2 border-white"></div>
                            ) : (
                                <>
                                    <LogIn size={20} />
                                    Sign In
                                </>
                            )}
                        </button>
                    </div>
                </form>
            </div>
        </div>
    )
} 