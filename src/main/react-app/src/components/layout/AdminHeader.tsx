"use client"

import Image from 'next/image'

export interface AdminHeaderProps {
    title: string
    welcomeMessage?: string
}

export default function AdminHeader({ title, welcomeMessage = "Welcome, Admin" }: AdminHeaderProps) {
    return (
        <header className="border-b border-gray-200 bg-gradient-to-b from-[#CC887F] to-[#D9A49E] px-4 py-4 dark:border-gray-800">
            <div className="container mx-auto flex items-center justify-between">
                <div className="flex items-center gap-4">
                    <Image
                        src="/img/logoo.png"
                        alt="Logo"
                        width={123}
                        height={108}
                        className="h-[108px] w-[123px]"
                    />
                    <div className="text-center">
                        <h1 className="text-2xl font-bold text-white">
                            {title}
                        </h1>
                        <p className="mt-2 text-sm text-white">
                            {welcomeMessage}
                        </p>
                    </div>
                </div>
                <Image
                    src="/img/2.JPG"
                    alt="Trust Image"
                    width={100}
                    height={108}
                    className="h-[108px] w-[100px]"
                />
            </div>
        </header>
    )
} 