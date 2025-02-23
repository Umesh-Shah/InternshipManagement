export const DONATION_PURPOSES = [
    'Education',
    'Healthcare',
    'Food',
    'Shelter',
    'Emergency Relief',
    'Community Development',
    'Other',
    'Devka school',
] as const;

export const PAYMENT_MODES = [
    'Cash',
    'Check',
    'Bank Transfer',
    'Credit Card',
    'UPI',
    'Other',
] as const;

export type DonationPurpose = typeof DONATION_PURPOSES[number];
export type PaymentMode = typeof PAYMENT_MODES[number];

export interface DonationData {
    donor_name: string;
    amount: number;
    purpose: DonationPurpose;
    payment_mode: PaymentMode;
    date: string;
    notes?: string;
}

export interface DonorData {
    name: string;
    email: string;
    phone: string;
    address?: string;
    total_donations: number;
    last_donation_date?: string;
}

export interface CountryData {
    country_name: string;
    country_std_code: string;
}

export interface DonorFormData {
    donor_name: string;
    country: string;
    mobile_number: string;
    std_code: string;
    email: string;
    address: string;
    receipt_no: string;
    receipt_dt: string;
    donation_purpose: DonationPurpose;
    pay_mode: PaymentMode;
    donation_amount: number;
    remarks?: string;
}

export interface DonorListItem {
    donor_id: string;
    donor_name: string;
    country: string;
    mobile_number: string;
    std_code: string;
    email: string;
    address: string;
    receipt_no: string;
    receipt_dt: string;
    donation_purpose: DonationPurpose;
    pay_mode: PaymentMode;
    donation_amount: number;
    status: 'Y' | 'N';
} 