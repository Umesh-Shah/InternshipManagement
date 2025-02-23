import Link from 'next/link'

export default function HomePage() {
    return (
        <div className="flex min-h-screen flex-col items-center justify-center bg-gray-50 px-4 py-12 dark:bg-gray-900 sm:px-6 lg:px-8">
            <div className="text-center">
                <h1 className="text-4xl font-bold tracking-tight text-gray-900 dark:text-white sm:text-5xl md:text-6xl">
                    <span className="block">Welcome to</span>
                    <span className="block text-indigo-600 dark:text-indigo-400">
                        Shree Vraj Bhagirathi Charitable Trust
                    </span>
                </h1>
                <p className="mx-auto mt-3 max-w-md text-base text-gray-500 dark:text-gray-400 sm:text-lg md:mt-5 md:max-w-3xl md:text-xl">
                    A charitable trust dedicated to helping those in need
                </p>
                <div className="mx-auto mt-5 max-w-md sm:flex sm:justify-center md:mt-8">
                    <div className="rounded-md shadow">
                        <Link
                            href="/login"
                            className="flex w-full items-center justify-center rounded-md border border-transparent bg-indigo-600 px-8 py-3 text-base font-medium text-white hover:bg-indigo-700 md:px-10 md:py-4 md:text-lg"
                        >
                            Sign in
                        </Link>
                    </div>
                </div>
            </div>
        </div>
    )
} 