"use client"

import { useState } from 'react'
import { toast } from 'react-toastify'
import type { DonorListItem, CountryData } from '@/lib/types'
import { DONATION_PURPOSES } from '@/lib/types'
import { Eye, EyeOff, Trash2 } from 'lucide-react'

// Mock data - replace with API call
const countries: CountryData[] = [
    { country_name: 'India', country_std_code: '+91' },
    { country_name: 'USA', country_std_code: '+1' },
]

// Mock donor data - replace with API call
const mockDonors: DonorListItem[] = [
    {
        donor_id: '1',
        donor_name: 'John Doe',
        country: 'India',
        mobile_number: '9876543210',
        std_code: '+91',
        email: 'john@example.com',
        address: '123 Main St, City',
        receipt_no: 'REC001',
        receipt_dt: '2024-03-01',
        donation_purpose: 'Devka school',
        pay_mode: 'Cash',
        donation_amount: 1000,
        status: 'Y',
    },
    // Add more mock data as needed
]

interface SearchFilters {
    name: string
    purpose: string
    country: string
    year: string
}

export default function DonorList() {
    const [donors, setDonors] = useState<DonorListItem[]>(mockDonors)
    const [filters, setFilters] = useState<SearchFilters>({
        name: '',
        purpose: '',
        country: '',
        year: '',
    })
    const [selectedDonor, setSelectedDonor] = useState<DonorListItem | null>(null)

    const handleSearch = () => {
        // TODO: Implement API search
        // For now, just filter the mock data
        const filtered = mockDonors.filter((donor) => {
            const nameMatch = donor.donor_name
                .toLowerCase()
                .includes(filters.name.toLowerCase())
            const purposeMatch =
                !filters.purpose || donor.donation_purpose === filters.purpose
            const countryMatch =
                !filters.country || donor.country === filters.country
            const yearMatch =
                !filters.year ||
                new Date(donor.receipt_dt).getFullYear().toString() ===
                filters.year
            return nameMatch && purposeMatch && countryMatch && yearMatch
        })
        setDonors(filtered)
    }

    const handleDelete = (donorId: string) => {
        if (
            window.confirm(
                'This record will be deleted permanently. Do you want to delete?'
            )
        ) {
            // TODO: Implement API delete
            setDonors((prev) =>
                prev.filter((donor) => donor.donor_id !== donorId)
            )
            toast.success('Donor deleted successfully')
        }
    }

    const handleVisibilityToggle = (donor: DonorListItem) => {
        if (
            window.confirm(
                `Are you sure you want to ${donor.status === 'Y' ? 'hide' : 'show'
                } this donor?`
            )
        ) {
            // TODO: Implement API update
            setDonors((prev) =>
                prev.map((d) =>
                    d.donor_id === donor.donor_id
                        ? { ...d, status: d.status === 'Y' ? 'N' : 'Y' }
                        : d
                )
            )
            toast.success(
                `Donor ${donor.status === 'Y' ? 'hidden' : 'shown'} successfully`
            )
        }
    }

    return (
        <div className="space-y-8">
            <h1 className="text-center text-3xl font-bold">Search Donors</h1>

            {/* Search Filters */}
            <div className="rounded-lg border border-gray-200 bg-white p-6 dark:border-gray-700 dark:bg-gray-800">
                <div className="grid grid-cols-1 gap-6 md:grid-cols-2">
                    <div>
                        <label className="block text-sm font-medium">
                            Full Name
                        </label>
                        <input
                            type="text"
                            value={filters.name}
                            onChange={(e) =>
                                setFilters((prev) => ({
                                    ...prev,
                                    name: e.target.value,
                                }))
                            }
                            className="mt-1 block w-full rounded-md border border-gray-300 px-3 py-2 dark:border-gray-600 dark:bg-gray-700"
                        />
                    </div>

                    <div>
                        <label className="block text-sm font-medium">
                            Purpose of Donation
                        </label>
                        <select
                            value={filters.purpose}
                            onChange={(e) =>
                                setFilters((prev) => ({
                                    ...prev,
                                    purpose: e.target.value,
                                }))
                            }
                            className="mt-1 block w-full rounded-md border border-gray-300 px-3 py-2 dark:border-gray-600 dark:bg-gray-700"
                        >
                            <option value="">--Select--</option>
                            {DONATION_PURPOSES.map((purpose) => (
                                <option key={purpose} value={purpose}>
                                    {purpose}
                                </option>
                            ))}
                        </select>
                    </div>

                    <div>
                        <label className="block text-sm font-medium">
                            Country
                        </label>
                        <select
                            value={filters.country}
                            onChange={(e) =>
                                setFilters((prev) => ({
                                    ...prev,
                                    country: e.target.value,
                                }))
                            }
                            className="mt-1 block w-full rounded-md border border-gray-300 px-3 py-2 dark:border-gray-600 dark:bg-gray-700"
                        >
                            <option value="">--Select--</option>
                            {countries.map((country) => (
                                <option
                                    key={country.country_std_code}
                                    value={country.country_name}
                                >
                                    {country.country_name}
                                </option>
                            ))}
                        </select>
                    </div>

                    <div>
                        <label className="block text-sm font-medium">
                            Donation Year
                        </label>
                        <select
                            value={filters.year}
                            onChange={(e) =>
                                setFilters((prev) => ({
                                    ...prev,
                                    year: e.target.value,
                                }))
                            }
                            className="mt-1 block w-full rounded-md border border-gray-300 px-3 py-2 dark:border-gray-600 dark:bg-gray-700"
                        >
                            <option value="">--Select--</option>
                            {Array.from(
                                { length: 10 },
                                (_, i) =>
                                    new Date().getFullYear() - i
                            ).map((year) => (
                                <option key={year} value={year}>
                                    {year}
                                </option>
                            ))}
                        </select>
                    </div>
                </div>

                <div className="mt-6 flex justify-center gap-4">
                    <button
                        onClick={handleSearch}
                        className="rounded-md bg-blue-600 px-4 py-2 text-white hover:bg-blue-700"
                    >
                        Search
                    </button>
                    <button
                        onClick={() => {
                            setFilters({
                                name: '',
                                purpose: '',
                                country: '',
                                year: '',
                            })
                            setDonors(mockDonors)
                        }}
                        className="rounded-md bg-gray-600 px-4 py-2 text-white hover:bg-gray-700"
                    >
                        Reset
                    </button>
                </div>
            </div>

            {/* Donor List */}
            <div className="rounded-lg border border-gray-200 bg-white dark:border-gray-700 dark:bg-gray-800">
                <div className="overflow-x-auto">
                    <table className="w-full divide-y divide-gray-200 dark:divide-gray-700">
                        <thead>
                            <tr className="bg-gray-50 dark:bg-gray-900">
                                <th className="px-6 py-3 text-left text-xs font-medium uppercase tracking-wider text-gray-500 dark:text-gray-400">
                                    Name
                                </th>
                                <th className="px-6 py-3 text-left text-xs font-medium uppercase tracking-wider text-gray-500 dark:text-gray-400">
                                    Country
                                </th>
                                <th className="px-6 py-3 text-left text-xs font-medium uppercase tracking-wider text-gray-500 dark:text-gray-400">
                                    Donation Purpose
                                </th>
                                <th className="px-6 py-3 text-left text-xs font-medium uppercase tracking-wider text-gray-500 dark:text-gray-400">
                                    Amount (₹)
                                </th>
                                <th className="px-6 py-3 text-center text-xs font-medium uppercase tracking-wider text-gray-500 dark:text-gray-400">
                                    Actions
                                </th>
                            </tr>
                        </thead>
                        <tbody className="divide-y divide-gray-200 bg-white dark:divide-gray-700 dark:bg-gray-800">
                            {donors.map((donor) => (
                                <tr
                                    key={donor.donor_id}
                                    className="hover:bg-gray-50 dark:hover:bg-gray-700"
                                >
                                    <td className="whitespace-nowrap px-6 py-4">
                                        <button
                                            onClick={() =>
                                                setSelectedDonor(donor)
                                            }
                                            className="text-blue-600 hover:text-blue-800 dark:text-blue-400 dark:hover:text-blue-300"
                                        >
                                            {donor.donor_name}
                                        </button>
                                    </td>
                                    <td className="whitespace-nowrap px-6 py-4">
                                        {donor.country}
                                    </td>
                                    <td className="whitespace-nowrap px-6 py-4">
                                        {donor.donation_purpose}
                                    </td>
                                    <td className="whitespace-nowrap px-6 py-4">
                                        {donor.donation_amount.toLocaleString()}
                                    </td>
                                    <td className="whitespace-nowrap px-6 py-4 text-center">
                                        <div className="flex justify-center gap-4">
                                            <button
                                                onClick={() =>
                                                    handleDelete(donor.donor_id)
                                                }
                                                className="text-red-600 hover:text-red-800"
                                                title="Delete"
                                            >
                                                <Trash2 className="h-5 w-5" />
                                            </button>
                                            <button
                                                onClick={() =>
                                                    handleVisibilityToggle(donor)
                                                }
                                                className={`${donor.status === 'Y'
                                                        ? 'text-green-600 hover:text-green-800'
                                                        : 'text-gray-600 hover:text-gray-800'
                                                    }`}
                                                title={
                                                    donor.status === 'Y'
                                                        ? 'Hide'
                                                        : 'Show'
                                                }
                                            >
                                                {donor.status === 'Y' ? (
                                                    <Eye className="h-5 w-5" />
                                                ) : (
                                                    <EyeOff className="h-5 w-5" />
                                                )}
                                            </button>
                                        </div>
                                    </td>
                                </tr>
                            ))}
                        </tbody>
                    </table>
                </div>
            </div>

            {/* Donor Details Modal */}
            {selectedDonor && (
                <div className="fixed inset-0 z-50 flex items-center justify-center bg-black bg-opacity-50">
                    <div className="w-full max-w-3xl rounded-lg bg-white p-6 dark:bg-gray-800">
                        <div className="flex justify-between">
                            <h2 className="text-xl font-bold">Donor Details</h2>
                            <button
                                onClick={() => setSelectedDonor(null)}
                                className="text-gray-500 hover:text-gray-700"
                            >
                                ×
                            </button>
                        </div>
                        <div className="mt-4 grid grid-cols-2 gap-4">
                            <div>
                                <p className="font-medium">Name:</p>
                                <p>{selectedDonor.donor_name}</p>
                            </div>
                            <div>
                                <p className="font-medium">Country:</p>
                                <p>{selectedDonor.country}</p>
                            </div>
                            <div>
                                <p className="font-medium">Mobile:</p>
                                <p>
                                    {selectedDonor.std_code}{' '}
                                    {selectedDonor.mobile_number}
                                </p>
                            </div>
                            <div>
                                <p className="font-medium">Email:</p>
                                <p>{selectedDonor.email}</p>
                            </div>
                            <div className="col-span-2">
                                <p className="font-medium">Address:</p>
                                <p>{selectedDonor.address}</p>
                            </div>
                            <div>
                                <p className="font-medium">Receipt Number:</p>
                                <p>{selectedDonor.receipt_no}</p>
                            </div>
                            <div>
                                <p className="font-medium">Receipt Date:</p>
                                <p>{selectedDonor.receipt_dt}</p>
                            </div>
                            <div>
                                <p className="font-medium">Purpose:</p>
                                <p>{selectedDonor.donation_purpose}</p>
                            </div>
                            <div>
                                <p className="font-medium">Payment Mode:</p>
                                <p>{selectedDonor.pay_mode}</p>
                            </div>
                            <div>
                                <p className="font-medium">Amount:</p>
                                <p>₹{selectedDonor.donation_amount}</p>
                            </div>
                        </div>
                    </div>
                </div>
            )}
        </div>
    )
} 