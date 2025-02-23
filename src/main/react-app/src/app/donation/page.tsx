"use client"

import { useState } from 'react'
import { toast } from 'react-toastify'
import { z } from 'zod'
import type {
    DonorFormData,
    CountryData,
    DonationPurpose,
    PaymentMode,
} from '@/lib/types'
import { DONATION_PURPOSES, PAYMENT_MODES } from '@/lib/types'

const donorFormSchema = z.object({
    donor_name: z.string().min(1, 'Name is required'),
    country: z.string().min(1, 'Country is required'),
    mobile_number: z.string().min(10, 'Invalid mobile number'),
    std_code: z.string(),
    email: z.string().email('Invalid email address'),
    address: z.string().min(1, 'Address is required'),
    receipt_no: z.string().min(1, 'Receipt number is required'),
    receipt_dt: z.string().min(1, 'Receipt date is required'),
    donation_purpose: z.enum(DONATION_PURPOSES),
    pay_mode: z.enum(PAYMENT_MODES),
    donation_amount: z.number().min(1, 'Amount is required'),
    remarks: z.string().optional(),
})

// Mock data - replace with API call
const countries: CountryData[] = [
    { country_name: 'India', country_std_code: '+91' },
    { country_name: 'USA', country_std_code: '+1' },
]

export default function DonationForm() {
    const [formData, setFormData] = useState<DonorFormData>({
        donor_name: '',
        country: '',
        mobile_number: '',
        std_code: '',
        email: '',
        address: '',
        receipt_no: '',
        receipt_dt: '',
        donation_purpose: 'Devka school',
        pay_mode: 'Cash',
        donation_amount: 0,
        remarks: '',
    })

    const [errors, setErrors] = useState<Record<string, string>>({})

    const handleChange = (
        e: React.ChangeEvent<
            HTMLInputElement | HTMLSelectElement | HTMLTextAreaElement
        >
    ) => {
        const { name, value } = e.target
        setFormData((prev) => ({
            ...prev,
            [name]: name === 'donation_amount' ? Number(value) : value,
        }))
        // Clear error when user starts typing
        if (errors[name]) {
            setErrors((prev) => ({ ...prev, [name]: '' }))
        }
    }

    const handleCountryChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
        const country = countries.find((c) => c.country_name === e.target.value)
        setFormData((prev) => ({
            ...prev,
            country: e.target.value,
            std_code: country?.country_std_code || '',
        }))
    }

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault()

        try {
            const validatedData = donorFormSchema.parse(formData)
            // TODO: Submit to API
            console.log('Form data:', validatedData)
            toast.success('Donation form submitted successfully')
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
        <div className="mx-auto max-w-4xl">
            <h1 className="mb-8 text-center text-3xl font-bold">
                Donation Form
            </h1>

            <form onSubmit={handleSubmit} className="space-y-8">
                {/* Personal Details Section */}
                <div className="rounded-lg border border-gray-200 bg-white p-6 dark:border-gray-700 dark:bg-gray-800">
                    <h2 className="mb-4 text-xl font-semibold text-gray-900 dark:text-white">
                        Personal Details
                    </h2>
                    <div className="grid grid-cols-1 gap-6 md:grid-cols-2">
                        <div>
                            <label className="block text-sm font-medium">
                                Full Name
                                <span className="text-red-500">*</span>
                            </label>
                            <input
                                type="text"
                                name="donor_name"
                                value={formData.donor_name}
                                onChange={handleChange}
                                className="mt-1 block w-full rounded-md border border-gray-300 px-3 py-2 dark:border-gray-600 dark:bg-gray-700"
                            />
                            {errors.donor_name && (
                                <p className="mt-1 text-sm text-red-500">
                                    {errors.donor_name}
                                </p>
                            )}
                        </div>

                        <div>
                            <label className="block text-sm font-medium">
                                Country
                                <span className="text-red-500">*</span>
                            </label>
                            <select
                                name="country"
                                value={formData.country}
                                onChange={handleCountryChange}
                                className="mt-1 block w-full rounded-md border border-gray-300 px-3 py-2 dark:border-gray-600 dark:bg-gray-700"
                            >
                                <option value="">--Select--</option>
                                {countries.map((country) => (
                                    <option
                                        key={country.country_std_code}
                                        value={country.country_name}
                                    >
                                        {country.country_name} (
                                        {country.country_std_code})
                                    </option>
                                ))}
                            </select>
                            {errors.country && (
                                <p className="mt-1 text-sm text-red-500">
                                    {errors.country}
                                </p>
                            )}
                        </div>

                        <div>
                            <label className="block text-sm font-medium">
                                Mobile Number
                                <span className="text-red-500">*</span>
                            </label>
                            <div className="flex">
                                <input
                                    type="text"
                                    name="std_code"
                                    value={formData.std_code}
                                    readOnly
                                    className="mt-1 w-20 rounded-l-md border border-gray-300 px-3 py-2 dark:border-gray-600 dark:bg-gray-700"
                                />
                                <input
                                    type="text"
                                    name="mobile_number"
                                    value={formData.mobile_number}
                                    onChange={handleChange}
                                    className="mt-1 block w-full rounded-r-md border border-gray-300 px-3 py-2 dark:border-gray-600 dark:bg-gray-700"
                                />
                            </div>
                            {errors.mobile_number && (
                                <p className="mt-1 text-sm text-red-500">
                                    {errors.mobile_number}
                                </p>
                            )}
                        </div>

                        <div>
                            <label className="block text-sm font-medium">
                                Email
                                <span className="text-red-500">*</span>
                            </label>
                            <input
                                type="email"
                                name="email"
                                value={formData.email}
                                onChange={handleChange}
                                placeholder="example@xyz.xyz"
                                className="mt-1 block w-full rounded-md border border-gray-300 px-3 py-2 dark:border-gray-600 dark:bg-gray-700"
                            />
                            {errors.email && (
                                <p className="mt-1 text-sm text-red-500">
                                    {errors.email}
                                </p>
                            )}
                        </div>

                        <div className="md:col-span-2">
                            <label className="block text-sm font-medium">
                                Address
                                <span className="text-red-500">*</span>
                            </label>
                            <textarea
                                name="address"
                                value={formData.address}
                                onChange={handleChange}
                                placeholder="Colony, City, State"
                                rows={3}
                                className="mt-1 block w-full rounded-md border border-gray-300 px-3 py-2 dark:border-gray-600 dark:bg-gray-700"
                            />
                            {errors.address && (
                                <p className="mt-1 text-sm text-red-500">
                                    {errors.address}
                                </p>
                            )}
                        </div>
                    </div>
                </div>

                {/* Donation Details Section */}
                <div className="rounded-lg border border-gray-200 bg-white p-6 dark:border-gray-700 dark:bg-gray-800">
                    <h2 className="mb-4 text-xl font-semibold text-gray-900 dark:text-white">
                        Donation Details
                    </h2>
                    <div className="grid grid-cols-1 gap-6 md:grid-cols-2">
                        <div>
                            <label className="block text-sm font-medium">
                                Receipt Number
                                <span className="text-red-500">*</span>
                            </label>
                            <input
                                type="text"
                                name="receipt_no"
                                value={formData.receipt_no}
                                onChange={handleChange}
                                className="mt-1 block w-full rounded-md border border-gray-300 px-3 py-2 dark:border-gray-600 dark:bg-gray-700"
                            />
                            {errors.receipt_no && (
                                <p className="mt-1 text-sm text-red-500">
                                    {errors.receipt_no}
                                </p>
                            )}
                        </div>

                        <div>
                            <label className="block text-sm font-medium">
                                Receipt Date
                                <span className="text-red-500">*</span>
                            </label>
                            <input
                                type="date"
                                name="receipt_dt"
                                value={formData.receipt_dt}
                                onChange={handleChange}
                                className="mt-1 block w-full rounded-md border border-gray-300 px-3 py-2 dark:border-gray-600 dark:bg-gray-700"
                            />
                            {errors.receipt_dt && (
                                <p className="mt-1 text-sm text-red-500">
                                    {errors.receipt_dt}
                                </p>
                            )}
                        </div>

                        <div>
                            <label className="block text-sm font-medium">
                                Purpose of Donation
                                <span className="text-red-500">*</span>
                            </label>
                            <select
                                name="donation_purpose"
                                value={formData.donation_purpose}
                                onChange={handleChange}
                                className="mt-1 block w-full rounded-md border border-gray-300 px-3 py-2 dark:border-gray-600 dark:bg-gray-700"
                            >
                                <option value="">--Select--</option>
                                {DONATION_PURPOSES.map((purpose) => (
                                    <option key={purpose} value={purpose}>
                                        {purpose}
                                    </option>
                                ))}
                            </select>
                            {errors.donation_purpose && (
                                <p className="mt-1 text-sm text-red-500">
                                    {errors.donation_purpose}
                                </p>
                            )}
                        </div>

                        <div>
                            <label className="block text-sm font-medium">
                                Payment Mode
                                <span className="text-red-500">*</span>
                            </label>
                            <select
                                name="pay_mode"
                                value={formData.pay_mode}
                                onChange={handleChange}
                                className="mt-1 block w-full rounded-md border border-gray-300 px-3 py-2 dark:border-gray-600 dark:bg-gray-700"
                            >
                                <option value="">--Select--</option>
                                {PAYMENT_MODES.map((mode) => (
                                    <option key={mode} value={mode}>
                                        {mode}
                                    </option>
                                ))}
                            </select>
                            {errors.pay_mode && (
                                <p className="mt-1 text-sm text-red-500">
                                    {errors.pay_mode}
                                </p>
                            )}
                        </div>

                        <div>
                            <label className="block text-sm font-medium">
                                Donation Amount
                                <span className="text-red-500">*</span>
                            </label>
                            <div className="relative mt-1">
                                <span className="absolute inset-y-0 left-0 flex items-center pl-3 text-gray-500">
                                    ₹
                                </span>
                                <input
                                    type="number"
                                    name="donation_amount"
                                    value={formData.donation_amount}
                                    onChange={handleChange}
                                    className="block w-full rounded-md border border-gray-300 pl-7 pr-3 py-2 dark:border-gray-600 dark:bg-gray-700"
                                />
                            </div>
                            {errors.donation_amount && (
                                <p className="mt-1 text-sm text-red-500">
                                    {errors.donation_amount}
                                </p>
                            )}
                        </div>

                        <div>
                            <label className="block text-sm font-medium">
                                Remarks
                            </label>
                            <textarea
                                name="remarks"
                                value={formData.remarks}
                                onChange={handleChange}
                                placeholder="Remarks if any"
                                rows={3}
                                className="mt-1 block w-full rounded-md border border-gray-300 px-3 py-2 dark:border-gray-600 dark:bg-gray-700"
                            />
                        </div>
                    </div>
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
                        className="rounded-md bg-gray-600 px-4 py-2 text-white hover:bg-gray-700"
                    >
                        Reset
                    </button>
                </div>
            </form>
        </div>
    )
} 