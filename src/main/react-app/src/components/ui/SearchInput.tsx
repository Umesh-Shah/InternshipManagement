"use client";

import { useEffect, useState, useCallback } from "react";
import { cn } from "@/lib/utils";
import { Search } from "lucide-react";

interface SearchInputProps {
    value: string;
    onChange: (value: string) => void;
    placeholder?: string;
    className?: string;
    debounceMs?: number;
}

export const SearchInput = ({
    value: externalValue,
    onChange,
    placeholder = "Search...",
    className,
    debounceMs = 300
}: SearchInputProps) => {
    const [value, setValue] = useState(externalValue);

    useEffect(() => {
        setValue(externalValue);
    }, [externalValue]);

    const debouncedOnChange = useCallback(
        (() => {
            let timeout: NodeJS.Timeout;
            return (value: string) => {
                clearTimeout(timeout);
                timeout = setTimeout(() => {
                    onChange(value);
                }, debounceMs);
            };
        })(),
        [onChange, debounceMs]
    );

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const newValue = e.target.value;
        setValue(newValue);
        debouncedOnChange(newValue);
    };

    return (
        <div className="relative">
            <div className="pointer-events-none absolute inset-y-0 left-0 flex items-center pl-3">
                <Search className="h-5 w-5 text-gray-400" />
            </div>
            <input
                type="text"
                value={value}
                onChange={handleChange}
                placeholder={placeholder}
                className={cn(
                    "block w-full rounded-md border",
                    "pl-10 pr-3 py-2",
                    "bg-white dark:bg-gray-800",
                    "border-gray-300 dark:border-gray-700",
                    "text-gray-900 dark:text-white",
                    "placeholder-gray-500 dark:placeholder-gray-400",
                    "focus:outline-none focus:ring-2 focus:ring-blue-500",
                    className
                )}
            />
        </div>
    );
}; 