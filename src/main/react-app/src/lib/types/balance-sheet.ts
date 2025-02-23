export interface BalanceSheetData {
    sheet_id: string;
    sheet_title: string;
    sheet_year: string;
    sheet_data: string;
    status: 'N' | 'H' | 'Deleted';
} 