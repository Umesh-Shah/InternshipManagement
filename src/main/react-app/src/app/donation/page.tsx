"use client"

import { z } from 'zod'
import type {
    DonorFormData,
    CountryData,
    DonationPurpose,
    PaymentMode,
} from '@/lib/types'
import { DONATION_PURPOSES, PAYMENT_MODES } from '@/lib/types'
import { FormField } from '@/components/ui/FormField'
import { Button } from '@/components/ui/Button'
import { Card } from '@/components/ui/Card'
import { Select } from '@/components/ui/Select'
import { MoneyInput } from '@/components/ui/MoneyInput'
import { PhoneInput } from '@/components/ui/PhoneInput'
import { useForm } from '@/hooks/useForm'

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
    const {
        values: formData,
        errors,
        isSubmitting,
        handleChange,
        handleSubmit,
        setFieldValue
    } = useForm<DonorFormData>({
        initialValues: {
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
        },
        validationSchema: donorFormSchema,
        onSubmit: async (values) => {
            // TODO: Submit to API
            console.log('Form data:', values)
        }
    })

    const countryOptions = countries.map(country => ({
        label: `${country.country_name} (${country.country_std_code})`,
        value: country.country_name
    }))

    const donationPurposeOptions = DONATION_PURPOSES.map(purpose => ({
        label: purpose,
        value: purpose
    }))

    const paymentModeOptions = PAYMENT_MODES.map(mode => ({
        label: mode,
        value: mode
    }))

    const handleCountryChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
        const country = countries.find((c) => c.country_name === e.target.value)
        setFieldValue('country', e.target.value)
        setFieldValue('std_code', country?.country_std_code || '')
    }

    return (
        <div className="mx-auto max-w-4xl">
            <h1 className="mb-8 text-center text-3xl font-bold">
                Donation Form
            </h1>

            <form onSubmit={handleSubmit} className="space-y-8">
                <Card title="Personal Details">
                    <div className="grid grid-cols-1 gap-6 md:grid-cols-2">
                        <FormField
                            label="Full Name"
                            name="donor_name"
                            value={formData.donor_name}
                            onChange={handleChange}
                            error={errors.donor_name}
                            required
                        />

                        <Select
                            label="Country"
                            name="country"
                            value={formData.country}
                            onChange={handleCountryChange}
                            options={countryOptions}
                            error={errors.country}
                            required
                        />

                        <PhoneInput
                            label="Mobile Number"
                            name="mobile_number"
                            value={formData.mobile_number}
                            onChange={handleChange}
                            countryCode={formData.std_code}
                            error={errors.mobile_number}
                            required
                        />

                        <FormField
                            label="Email"
                            name="email"
                            type="email"
                            value={formData.email}
                            onChange={handleChange}
                            error={errors.email}
                            placeholder="example@xyz.xyz"
                            required
                        />

                        <FormField
                            label="Address"
                            name="address"
                            type="textarea"
                            value={formData.address}
                            onChange={handleChange}
                            error={errors.address}
                            placeholder="Colony, City, State"
                            className="md:col-span-2"
                            rows={3}
                            required
                        />
                    </div>
                </Card>

                <Card title="Donation Details">
                    <div className="grid grid-cols-1 gap-6 md:grid-cols-2">
                        <FormField
                            label="Receipt Number"
                            name="receipt_no"
                            value={formData.receipt_no}
                            onChange={handleChange}
                            error={errors.receipt_no}
                            required
                        />

                        <FormField
                            label="Receipt Date"
                            name="receipt_dt"
                            type="date"
                            value={formData.receipt_dt}
                            onChange={handleChange}
                            error={errors.receipt_dt}
                            required
                        />

                        <Select
                            label="Purpose of Donation"
                            name="donation_purpose"
                            value={formData.donation_purpose}
                            onChange={handleChange}
                            options={donationPurposeOptions}
                            error={errors.donation_purpose}
                            required
                        />

                        <Select
                            label="Payment Mode"
                            name="pay_mode"
                            value={formData.pay_mode}
                            onChange={handleChange}
                            options={paymentModeOptions}
                            error={errors.pay_mode}
                            required
                        />

                        <MoneyInput
                            label="Donation Amount"
                            name="donation_amount"
                            value={formData.donation_amount}
                            onChange={handleChange}
                            error={errors.donation_amount}
                            required
                            min={1}
                        />

                        <FormField
                            label="Remarks"
                            name="remarks"
                            type="textarea"
                            value={formData.remarks}
                            onChange={handleChange}
                            placeholder="Remarks if any"
                            rows={3}
                        />
                    </div>
                </Card>

                <div className="flex justify-center gap-4">
                    <Button
                        type="submit"
                        isLoading={isSubmitting}
                    >
                        Submit
                    </Button>
                    <Button
                        type="reset"
                        variant="secondary"
                    >
                        Reset
                    </Button>
                </div>
            </form>
        </div>
    )
} 