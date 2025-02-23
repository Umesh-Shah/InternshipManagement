"use client"

export default function AdminFooter() {
    return (
        <footer className="border-t border-gray-200 bg-gradient-to-b from-white to-[#F0C2AB] px-4 py-4 dark:border-gray-800 dark:from-gray-900 dark:to-gray-800">
            <div className="container mx-auto flex items-center justify-between text-sm text-gray-600 dark:text-gray-400">
                <div className="flex items-center gap-4">
                    <span>Terms & Conditions</span>
                    <span>|</span>
                    <span>Privacy Policy</span>
                </div>
                <div>© Copyright {new Date().getFullYear()}</div>
            </div>
        </footer>
    )
} 