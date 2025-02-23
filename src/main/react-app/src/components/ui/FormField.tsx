"use client";

import { cn } from "@/lib/utils";

interface FormFieldProps {
    label: string;
    name: string;
    type?: string;
    value: string | number;
    onChange: (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => void;
    error?: string;
    required?: boolean;
    placeholder?: string;
    className?: string;
    min?: number;
    max?: number;
    step?: number;
    rows?: number;
    disabled?: boolean;
}

export const FormField = ({
    label,
    name,
    type = "text",
    value,
    onChange,
    error,
    required = false,
    placeholder,
    className,
    min,
    max,
    step,
    rows,
    disabled = false
}: FormFieldProps) => {
    const isTextarea = type === "textarea";
    const InputComponent = isTextarea ? "textarea" as const : "input" as const;

    return (
        <div className="space-y-2">
            <label htmlFor={name} className="block text-sm font-medium">
                {label}
                {required && <span className="text-red-500">*</span>}
            </label>
            <InputComponent
                id={name}
                name={name}
                type={type}
                value={value}
                onChange={onChange}
                placeholder={placeholder}
                rows={rows}
                min={min}
                max={max}
                step={step}
                disabled={disabled}
                className={cn(
                    "w-full px-3 py-2 border rounded-md",
                    "bg-white dark:bg-gray-800",
                    "border-gray-300 dark:border-gray-700",
                    "focus:outline-none focus:ring-2 focus:ring-blue-500",
                    "disabled:opacity-50 disabled:cursor-not-allowed",
                    error && "border-red-500 focus:ring-red-500",
                    className
                )}
                required={required}
            />
            {error && (
                <p className="mt-1 text-sm text-red-500">
                    {error}
                </p>
            )}
        </div>
    );
}; 