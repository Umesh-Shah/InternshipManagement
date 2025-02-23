"use client";

import { cn } from "@/lib/utils";

interface SelectOption {
    label: string;
    value: string;
}

interface SelectProps {
    label: string;
    name: string;
    value: string;
    onChange: (e: React.ChangeEvent<HTMLSelectElement>) => void;
    options: SelectOption[];
    error?: string;
    required?: boolean;
    placeholder?: string;
    className?: string;
    disabled?: boolean;
}

export const Select = ({
    label,
    name,
    value,
    onChange,
    options,
    error,
    required = false,
    placeholder = "--Select--",
    className,
    disabled = false
}: SelectProps) => {
    return (
        <div className="space-y-2">
            <label htmlFor={name} className="block text-sm font-medium">
                {label}
                {required && <span className="text-red-500">*</span>}
            </label>
            <select
                id={name}
                name={name}
                value={value}
                onChange={onChange}
                disabled={disabled}
                className={cn(
                    "mt-1 block w-full rounded-md border",
                    "bg-white dark:bg-gray-800",
                    "border-gray-300 dark:border-gray-700",
                    "px-3 py-2",
                    "focus:outline-none focus:ring-2 focus:ring-blue-500",
                    "disabled:opacity-50 disabled:cursor-not-allowed",
                    error && "border-red-500 focus:ring-red-500",
                    className
                )}
                required={required}
            >
                <option value="">{placeholder}</option>
                {options.map((option) => (
                    <option key={option.value} value={option.value}>
                        {option.label}
                    </option>
                ))}
            </select>
            {error && (
                <p className="mt-1 text-sm text-red-500">
                    {error}
                </p>
            )}
        </div>
    );
}; 