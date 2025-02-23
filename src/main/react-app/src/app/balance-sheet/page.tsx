"use client"

import { useState } from 'react'
import { toast } from 'react-toastify'
import type { BalanceSheetData } from '@/lib/types'
import { Eye, EyeOff, Trash2, Download } from 'lucide-react'

// Mock data - replace with API call
const mockBalanceSheets: BalanceSheetData[] = [
    {
        sheet_id: '1',
        sheet_title: 'Annual Balance Sheet 2023',
        sheet_year: '2023-2024',
        sheet_data: 'path/to/file.pdf',
        status: 'N',
    },
    {
        sheet_id: '2',
        sheet_title: 'Annual Balance Sheet 2022',
        sheet_year: '2022-2023',
        sheet_data: 'path/to/file.pdf',
        status: 'H',
    },
]

export default function ManageBalanceSheet() {
    const [sheets, setSheets] = useState<BalanceSheetData[]>(mockBalanceSheets)

    const handleDelete = (sheetId: string) => {
        if (
            window.confirm(
                'This record will be deleted permanently. Do you want to delete?'
            )
        ) {
            // TODO: Implement API delete
            setSheets((prev) =>
                prev.map((sheet) =>
                    sheet.sheet_id === sheetId
                        ? { ...sheet, status: 'Deleted' }
                        : sheet
                )
            )
            toast.success('Balance sheet deleted successfully')
        }
    }

    const handleVisibilityToggle = (sheet: BalanceSheetData) => {
        if (
            window.confirm(
                `Are you sure you want to ${sheet.status === 'N' ? 'hide' : 'show'
                } this balance sheet?`
            )
        ) {
            // TODO: Implement API update
            setSheets((prev) =>
                prev.map((s) =>
                    s.sheet_id === sheet.sheet_id
                        ? { ...s, status: s.status === 'N' ? 'H' : 'N' }
                        : s
                )
            )
            toast.success(
                `Balance sheet ${sheet.status === 'N' ? 'hidden' : 'shown'
                } successfully`
            )
        }
    }

    const handleDownload = (sheet: BalanceSheetData) => {
        // TODO: Implement file download
        console.log('Downloading sheet:', sheet.sheet_data)
        toast.info('Downloading balance sheet...')
    }

    const visibleSheets = sheets.filter((sheet) => sheet.status !== 'Deleted')

    return (
        <div className="space-y-8">
            <h1 className="text-center text-3xl font-bold">
                Manage Balance Sheets
            </h1>

            <div className="rounded-lg border border-gray-200 bg-white dark:border-gray-700 dark:bg-gray-800">
                <div className="overflow-x-auto">
                    <table className="w-full divide-y divide-gray-200 dark:divide-gray-700">
                        <thead>
                            <tr className="bg-gray-50 dark:bg-gray-900">
                                <th className="px-6 py-3 text-left text-xs font-medium uppercase tracking-wider text-gray-500 dark:text-gray-400">
                                    Sr. No.
                                </th>
                                <th className="px-6 py-3 text-left text-xs font-medium uppercase tracking-wider text-gray-500 dark:text-gray-400">
                                    Title
                                </th>
                                <th className="px-6 py-3 text-left text-xs font-medium uppercase tracking-wider text-gray-500 dark:text-gray-400">
                                    Year
                                </th>
                                <th className="px-6 py-3 text-center text-xs font-medium uppercase tracking-wider text-gray-500 dark:text-gray-400">
                                    Actions
                                </th>
                            </tr>
                        </thead>
                        <tbody className="divide-y divide-gray-200 bg-white dark:divide-gray-700 dark:bg-gray-800">
                            {visibleSheets.map((sheet, index) => (
                                <tr
                                    key={sheet.sheet_id}
                                    className="hover:bg-gray-50 dark:hover:bg-gray-700"
                                >
                                    <td className="whitespace-nowrap px-6 py-4 text-sm">
                                        {index + 1}
                                    </td>
                                    <td className="whitespace-nowrap px-6 py-4 text-sm">
                                        {sheet.sheet_title}
                                    </td>
                                    <td className="whitespace-nowrap px-6 py-4 text-sm">
                                        {sheet.sheet_year}
                                    </td>
                                    <td className="whitespace-nowrap px-6 py-4 text-center">
                                        <div className="flex justify-center gap-4">
                                            <button
                                                onClick={() =>
                                                    handleDownload(sheet)
                                                }
                                                className="text-blue-600 hover:text-blue-800"
                                                title="Download"
                                            >
                                                <Download className="h-5 w-5" />
                                            </button>
                                            <button
                                                onClick={() =>
                                                    handleVisibilityToggle(sheet)
                                                }
                                                className={`${sheet.status === 'N'
                                                        ? 'text-green-600 hover:text-green-800'
                                                        : 'text-gray-600 hover:text-gray-800'
                                                    }`}
                                                title={
                                                    sheet.status === 'N'
                                                        ? 'Hide'
                                                        : 'Show'
                                                }
                                            >
                                                {sheet.status === 'N' ? (
                                                    <Eye className="h-5 w-5" />
                                                ) : (
                                                    <EyeOff className="h-5 w-5" />
                                                )}
                                            </button>
                                            <button
                                                onClick={() =>
                                                    handleDelete(sheet.sheet_id)
                                                }
                                                className="text-red-600 hover:text-red-800"
                                                title="Delete"
                                            >
                                                <Trash2 className="h-5 w-5" />
                                            </button>
                                        </div>
                                    </td>
                                </tr>
                            ))}
                            {visibleSheets.length === 0 && (
                                <tr>
                                    <td
                                        colSpan={4}
                                        className="px-6 py-4 text-center text-sm text-gray-500 dark:text-gray-400"
                                    >
                                        No balance sheets found
                                    </td>
                                </tr>
                            )}
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    )
} 