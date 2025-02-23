"use client"

import { useState } from 'react'
import { toast } from 'react-toastify'
import { z } from 'zod'

const passwordSchema = z
    .object({
        oldPassword: z.string().min(1, 'Old password is required'),
        newPassword: z
            .string()
            .min(8, 'Password must be at least 8 characters')
            .max(15, 'Password must not exceed 15 characters')
            .regex(
                /^(?=.*\d)(?=.*[^a-zA-Z0-9])(?!.*\s).{8,15}$/,
                'Password must contain at least one number and one special character'
            ),
        confirmPassword: z.string().min(1, 'Please confirm your password'),
    })
    .refine((data) => data.newPassword === data.confirmPassword, {
        message: "Passwords don't match",
        path: ['confirmPassword'],
    })

export default function ChangePassword() {
    const [formData, setFormData] = useState({
        oldPassword: '',
        newPassword: '',
        confirmPassword: '',
    })

    const [errors, setErrors] = useState<Record<string, string>>({})

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const { name, value } = e.target
        setFormData((prev) => ({ ...prev, [name]: value }))
        // Clear error when user starts typing
        if (errors[name]) {
            setErrors((prev) => ({ ...prev, [name]: '' }))
        }
    }

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault()

        try {
            const validatedData = passwordSchema.parse(formData)
            // TODO: Submit to API
            console.log('Form data:', validatedData)
            toast.success('Password changed successfully')
            setFormData({
                oldPassword: '',
                newPassword: '',
                confirmPassword: '',
            })
        } catch (error) {
            if (error instanceof z.ZodError) {
                const newErrors: Record<string, string> = {}
                error.errors.forEach((err) => {
                    if (err.path[0]) {
                        newErrors[err.path[0].toString()] = err.message
                    }
                })
                setErrors(newErrors)
            }
        }
    }

    return (
        <div className="mx-auto max-w-md">
            <h1 className="mb-8 text-center text-3xl font-bold">
                Change Password
            </h1>

            <div className="rounded-lg border border-gray-200 bg-white p-6 dark:border-gray-700 dark:bg-gray-800">
                <form onSubmit={handleSubmit} className="space-y-6">
                    <div>
                        <label className="block text-sm font-medium">
                            Old Password
                            <span className="text-red-500">*</span>
                        </label>
                        <input
                            type="password"
                            name="oldPassword"
                            value={formData.oldPassword}
                            onChange={handleChange}
                            className="mt-1 block w-full rounded-md border border-gray-300 px-3 py-2 dark:border-gray-600 dark:bg-gray-700"
                        />
                        {errors.oldPassword && (
                            <p className="mt-1 text-sm text-red-500">
                                {errors.oldPassword}
                            </p>
                        )}
                    </div>

                    <div>
                        <label className="block text-sm font-medium">
                            New Password
                            <span className="text-red-500">*</span>
                        </label>
                        <input
                            type="password"
                            name="newPassword"
                            value={formData.newPassword}
                            onChange={handleChange}
                            className="mt-1 block w-full rounded-md border border-gray-300 px-3 py-2 dark:border-gray-600 dark:bg-gray-700"
                        />
                        {errors.newPassword && (
                            <p className="mt-1 text-sm text-red-500">
                                {errors.newPassword}
                            </p>
                        )}
                        <p className="mt-1 text-xs text-gray-500">
                            Password must be 8-15 characters with at least one
                            number and one special character
                        </p>
                    </div>

                    <div>
                        <label className="block text-sm font-medium">
                            Confirm New Password
                            <span className="text-red-500">*</span>
                        </label>
                        <input
                            type="password"
                            name="confirmPassword"
                            value={formData.confirmPassword}
                            onChange={handleChange}
                            className="mt-1 block w-full rounded-md border border-gray-300 px-3 py-2 dark:border-gray-600 dark:bg-gray-700"
                        />
                        {errors.confirmPassword && (
                            <p className="mt-1 text-sm text-red-500">
                                {errors.confirmPassword}
                            </p>
                        )}
                    </div>

                    <div className="flex justify-center gap-4">
                        <button
                            type="submit"
                            className="rounded-md bg-blue-600 px-4 py-2 text-white hover:bg-blue-700"
                        >
                            Change Password
                        </button>
                        <button
                            type="reset"
                            onClick={() => {
                                setFormData({
                                    oldPassword: '',
                                    newPassword: '',
                                    confirmPassword: '',
                                })
                                setErrors({})
                            }}
                            className="rounded-md bg-gray-600 px-4 py-2 text-white hover:bg-gray-700"
                        >
                            Reset
                        </button>
                    </div>
                </form>
            </div>
        </div>
    )
} 