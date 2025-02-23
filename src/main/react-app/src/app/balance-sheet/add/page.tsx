"use client"

import { useState } from 'react'
import { toast } from 'react-toastify'
import { z } from 'zod'

const balanceSheetSchema = z.object({
    sheet_title: z.string().min(1, 'Title is required'),
    sheet_year: z.string().min(1, 'Year is required'),
    sheet_data: z.instanceof(File, { message: 'File is required' }),
})

export default function AddBalanceSheet() {
    const [formData, setFormData] = useState({
        sheet_title: '',
        sheet_year: '',
        sheet_data: null as File | null,
    })

    const [errors, setErrors] = useState<Record<string, string>>({})

    const handleChange = (
        e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>
    ) => {
        const { name, value } = e.target
        setFormData((prev) => ({ ...prev, [name]: value }))
        // Clear error when user starts typing
        if (errors[name]) {
            setErrors((prev) => ({ ...prev, [name]: '' }))
        }
    }

    const handleFileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const file = e.target.files?.[0]
        if (file) {
            // Check file size (5MB limit)
            if (file.size > 5 * 1024 * 1024) {
                setErrors((prev) => ({
                    ...prev,
                    sheet_data: 'File size must be less than 5MB',
                }))
                return
            }
            // Check file type (PDF only)
            if (file.type !== 'application/pdf') {
                setErrors((prev) => ({
                    ...prev,
                    sheet_data: 'Only PDF files are allowed',
                }))
                return
            }
            setFormData((prev) => ({ ...prev, sheet_data: file }))
            setErrors((prev) => ({ ...prev, sheet_data: '' }))
        }
    }

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault()

        try {
            const validatedData = balanceSheetSchema.parse(formData)
            // TODO: Submit to API
            console.log('Form data:', validatedData)
            toast.success('Balance sheet added successfully')
            setFormData({
                sheet_title: '',
                sheet_year: '',
                sheet_data: null,
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

    // Generate year options (last 10 years)
    const currentYear = new Date().getFullYear()
    const years = Array.from({ length: 10 }, (_, i) => {
        const year = currentYear - i
        return `${year}-${year + 1}`
    })

    return (
        <div className="mx-auto max-w-2xl">
            <h1 className="mb-8 text-center text-3xl font-bold">
                Add Balance Sheet
            </h1>

            <div className="rounded-lg border border-gray-200 bg-white p-6 dark:border-gray-700 dark:bg-gray-800">
                <form onSubmit={handleSubmit} className="space-y-6">
                    <div>
                        <label className="block text-sm font-medium">
                            Title
                            <span className="text-red-500">*</span>
                        </label>
                        <input
                            type="text"
                            name="sheet_title"
                            value={formData.sheet_title}
                            onChange={handleChange}
                            className="mt-1 block w-full rounded-md border border-gray-300 px-3 py-2 dark:border-gray-600 dark:bg-gray-700"
                        />
                        {errors.sheet_title && (
                            <p className="mt-1 text-sm text-red-500">
                                {errors.sheet_title}
                            </p>
                        )}
                    </div>

                    <div>
                        <label className="block text-sm font-medium">
                            Year
                            <span className="text-red-500">*</span>
                        </label>
                        <select
                            name="sheet_year"
                            value={formData.sheet_year}
                            onChange={handleChange}
                            className="mt-1 block w-full rounded-md border border-gray-300 px-3 py-2 dark:border-gray-600 dark:bg-gray-700"
                        >
                            <option value="">--Select--</option>
                            {years.map((year) => (
                                <option key={year} value={year}>
                                    {year}
                                </option>
                            ))}
                        </select>
                        {errors.sheet_year && (
                            <p className="mt-1 text-sm text-red-500">
                                {errors.sheet_year}
                            </p>
                        )}
                    </div>

                    <div>
                        <label className="block text-sm font-medium">
                            Upload Sheet
                            <span className="text-red-500">*</span>
                        </label>
                        <input
                            type="file"
                            accept=".pdf"
                            onChange={handleFileChange}
                            className="mt-1 block w-full rounded-md border border-gray-300 px-3 py-2 text-gray-700 dark:border-gray-600 dark:bg-gray-700 dark:text-gray-300"
                        />
                        {errors.sheet_data && (
                            <p className="mt-1 text-sm text-red-500">
                                {errors.sheet_data}
                            </p>
                        )}
                        <p className="mt-1 text-xs text-gray-500">
                            Only PDF files up to 5MB are allowed
                        </p>
                    </div>

                    <div className="flex justify-center gap-4">
                        <button
                            type="submit"
                            className="rounded-md bg-blue-600 px-4 py-2 text-white hover:bg-blue-700"
                        >
                            Submit
                        </button>
                        <button
                            type="reset"
                            onClick={() => {
                                setFormData({
                                    sheet_title: '',
                                    sheet_year: '',
                                    sheet_data: null,
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