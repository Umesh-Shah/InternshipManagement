// Admin types
export interface DonorFormData {
    donor_name: string
    country: string
    mobile_number: string
    std_code: string
    email: string
    address: string
    receipt_no: string
    receipt_dt: string
    donation_purpose: string
    pay_mode: string
    donation_amount: number
    remarks?: string
}

export interface DonorListItem extends DonorFormData {
    donor_id: string
    status: 'Y' | 'N' | 'deleted'
}

export interface BalanceSheetData {
    sheet_id: string
    sheet_title: string
    sheet_year: string
    sheet_data: string
    status: 'N' | 'H' | 'Deleted'
}

export interface AlbumData {
    album_id: string
    album_name: string
    album_title_img: string
}

export interface GalleryImage {
    gallery_id: string
    gallery_img: string
    album_id: string
}

export interface CountryData {
    country_name: string
    country_std_code: string
}

// Donation purpose options
export const DONATION_PURPOSES = [
    'Devka school',
    'Devka college',
    'Gau shalla',
    'Other',
] as const

export type DonationPurpose = typeof DONATION_PURPOSES[number]

// Payment mode options
export const PAYMENT_MODES = [
    'Cash',
    'Cheque',
    'Online Banking',
    'Credit Card',
    'Debit Card',
] as const

export type PaymentMode = typeof PAYMENT_MODES[number] 