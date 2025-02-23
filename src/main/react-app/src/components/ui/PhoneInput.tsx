"use client";

import { cn } from "@/lib/utils";

interface PhoneInputProps {
    label: string;
    name: string;
    value: string;
    onChange: (e: React.ChangeEvent<HTMLInputElement>) => void;
    countryCode: string;
    error?: string;
    required?: boolean;
    placeholder?: string;
    className?: string;
    disabled?: boolean;
}

export const PhoneInput = ({
    label,
    name,
    value,
    onChange,
    countryCode,
    error,
    required = false,
    placeholder = "Enter phone number",
    className,
    disabled = false
}: PhoneInputProps) => {
    const inputStyles = cn(
        "mt-1 block w-full rounded-md border",
        "bg-white dark:bg-gray-800",
        "border-gray-300 dark:border-gray-700",
        "px-3 py-2",
        "focus:outline-none focus:ring-2 focus:ring-blue-500",
        "disabled:opacity-50 disabled:cursor-not-allowed",
        error && "border-red-500 focus:ring-red-500"
    );

    return (
        <div className="space-y-2">
            <label htmlFor={name} className="block text-sm font-medium">
                {label}
                {required && <span className="text-red-500">*</span>}
            </label>
            <div className="flex">
                <input
                    type="text"
                    value={countryCode}
                    readOnly
                    className={cn(
                        inputStyles,
                        "w-20 rounded-r-none border-r-0",
                        className
                    )}
                />
                <input
                    type="tel"
                    id={name}
                    name={name}
                    value={value}
                    onChange={onChange}
                    placeholder={placeholder}
                    disabled={disabled}
                    className={cn(
                        inputStyles,
                        "rounded-l-none",
                        className
                    )}
                    required={required}
                />
            </div>
            {error && (
                <p className="mt-1 text-sm text-red-500">
                    {error}
                </p>
            )}
        </div>
    );
}; 