"use client"

import AdminHeader from '@/components/layout/AdminHeader'
import AdminSidebar from '@/components/layout/AdminSidebar'
import AdminFooter from '@/components/layout/AdminFooter'

export default function AdminLayout({
    children,
}: {
    children: React.ReactNode
}) {
    return (
        <div className="flex min-h-screen flex-col">
            <AdminHeader title="Shree Vraj Bhagirathi Charitable Trust" />
            
            <div className="flex flex-1">
                <AdminSidebar />
                <main className="flex-1 overflow-y-auto bg-gray-50 p-8 dark:bg-gray-900">
                    {children}
                </main>
            </div>

            <AdminFooter />
        </div>
    )
} 