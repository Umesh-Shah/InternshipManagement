"use client";

import { cn } from "@/lib/utils";

interface MoneyInputProps {
    label: string;
    name: string;
    value: number;
    onChange: (e: React.ChangeEvent<HTMLInputElement>) => void;
    error?: string;
    required?: boolean;
    currencySymbol?: string;
    min?: number;
    max?: number;
    step?: number;
    className?: string;
    disabled?: boolean;
}

export const MoneyInput = ({
    label,
    name,
    value,
    onChange,
    error,
    required = false,
    currencySymbol = "₹",
    min,
    max,
    step = 1,
    className,
    disabled = false
}: MoneyInputProps) => {
    return (
        <div className="space-y-2">
            <label htmlFor={name} className="block text-sm font-medium">
                {label}
                {required && <span className="text-red-500">*</span>}
            </label>
            <div className="relative mt-1">
                <span className="absolute inset-y-0 left-0 flex items-center pl-3 text-gray-500">
                    {currencySymbol}
                </span>
                <input
                    type="number"
                    id={name}
                    name={name}
                    value={value}
                    onChange={onChange}
                    min={min}
                    max={max}
                    step={step}
                    disabled={disabled}
                    className={cn(
                        "block w-full rounded-md border",
                        "pl-7 pr-3 py-2",
                        "bg-white dark:bg-gray-800",
                        "border-gray-300 dark:border-gray-700",
                        "focus:outline-none focus:ring-2 focus:ring-blue-500",
                        "disabled:opacity-50 disabled:cursor-not-allowed",
                        error && "border-red-500 focus:ring-red-500",
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