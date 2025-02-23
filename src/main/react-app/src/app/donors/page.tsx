"use client"

import { useState } from 'react'
import type { DonorListItem, CountryData } from '@/lib/types'
import { DONATION_PURPOSES } from '@/lib/types'
import { Eye, EyeOff, Trash2 } from 'lucide-react'
import { Card } from '@/components/ui/Card'
import { FormField } from '@/components/ui/FormField'
import { Select } from '@/components/ui/Select'
import { Button } from '@/components/ui/Button'
import { DataTable, type Column } from '@/components/ui/DataTable'
import { useToast } from '@/components/providers/ToastProvider'

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
    const { toast } = useToast()
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
        return filtered
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
            toast('success', 'Donor deleted successfully')
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
            toast('success', `Donor ${donor.status === 'Y' ? 'hidden' : 'shown'} successfully`)
        }
    }

    const columns: Column<DonorListItem>[] = [
        {
            key: 'donor_name',
            header: 'Name',
            sortable: true
        },
        {
            key: 'country',
            header: 'Country',
            sortable: true
        },
        {
            key: 'donation_purpose',
            header: 'Donation Purpose',
            sortable: true
        },
        {
            key: 'donation_amount',
            header: 'Amount (₹)',
            sortable: true,
            render: (value, _) => (value as number).toLocaleString('en-IN')
        },
        {
            key: 'status',
            header: 'Actions',
            render: (_, donor) => (
                <div className="flex justify-center gap-2">
                    <Button
                        variant="outline"
                        size="sm"
                        onClick={() => handleVisibilityToggle(donor)}
                    >
                        {donor.status === 'Y' ? (
                            <Eye className="h-4 w-4" />
                        ) : (
                            <EyeOff className="h-4 w-4" />
                        )}
                    </Button>
                    <Button
                        variant="danger"
                        size="sm"
                        onClick={() => handleDelete(donor.donor_id)}
                    >
                        <Trash2 className="h-4 w-4" />
                    </Button>
                </div>
            )
        }
    ]

    return (
        <div className="space-y-8">
            <h1 className="text-center text-3xl font-bold">Search Donors</h1>

            <Card title="Search Filters">
                <div className="grid grid-cols-1 gap-6 md:grid-cols-2">
                    <FormField
                        label="Full Name"
                        name="name"
                        value={filters.name}
                        onChange={(e) =>
                            setFilters((prev) => ({
                                ...prev,
                                name: e.target.value,
                            }))
                        }
                    />

                    <Select
                        label="Purpose of Donation"
                        name="purpose"
                        value={filters.purpose}
                        onChange={(e) =>
                            setFilters((prev) => ({
                                ...prev,
                                purpose: e.target.value,
                            }))
                        }
                        options={DONATION_PURPOSES.map(purpose => ({
                            label: purpose,
                            value: purpose
                        }))}
                    />

                    <Select
                        label="Country"
                        name="country"
                        value={filters.country}
                        onChange={(e) =>
                            setFilters((prev) => ({
                                ...prev,
                                country: e.target.value,
                            }))
                        }
                        options={countries.map(country => ({
                            label: country.country_name,
                            value: country.country_name
                        }))}
                    />

                    <Select
                        label="Donation Year"
                        name="year"
                        value={filters.year}
                        onChange={(e) =>
                            setFilters((prev) => ({
                                ...prev,
                                year: e.target.value,
                            }))
                        }
                        options={Array.from(
                            { length: 10 },
                            (_, i) => {
                                const year = new Date().getFullYear() - i
                                return {
                                    label: year.toString(),
                                    value: year.toString()
                                }
                            }
                        )}
                    />
                </div>

                <div className="mt-6 flex justify-center gap-4">
                    <Button onClick={handleSearch}>
                        Search
                    </Button>
                    <Button
                        variant="secondary"
                        onClick={() => {
                            setFilters({
                                name: '',
                                purpose: '',
                                country: '',
                                year: '',
                            })
                            setDonors(mockDonors)
                        }}
                    >
                        Reset
                    </Button>
                </div>
            </Card>

            <DataTable
                data={handleSearch()}
                columns={columns}
                showSearch={false}
            />

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