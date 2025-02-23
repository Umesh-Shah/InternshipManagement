"use client";

import { useState } from "react";
import { cn } from "@/lib/utils";
import { ChevronDown, ChevronUp, ChevronsUpDown } from "lucide-react";

interface Column<T> {
    key: keyof T;
    header: string;
    sortable?: boolean;
    render?: (value: T[keyof T], item: T) => React.ReactNode;
}

interface TableProps<T> {
    data: T[];
    columns: Column<T>[];
    onSort?: (key: keyof T, direction: "asc" | "desc") => void;
    className?: string;
    rowClassName?: string;
    emptyMessage?: string;
    isLoading?: boolean;
}

export function Table<T extends Record<string, any>>({
    data,
    columns,
    onSort,
    className,
    rowClassName,
    emptyMessage = "No data available",
    isLoading = false
}: TableProps<T>) {
    const [sortConfig, setSortConfig] = useState<{
        key: keyof T;
        direction: "asc" | "desc";
    } | null>(null);

    const handleSort = (key: keyof T) => {
        let direction: "asc" | "desc" = "asc";

        if (sortConfig?.key === key) {
            direction = sortConfig.direction === "asc" ? "desc" : "asc";
        }

        setSortConfig({ key, direction });
        onSort?.(key, direction);
    };

    const getSortIcon = (key: keyof T) => {
        if (!sortConfig || sortConfig.key !== key) {
            return <ChevronsUpDown className="h-4 w-4" />;
        }
        return sortConfig.direction === "asc" ? (
            <ChevronUp className="h-4 w-4" />
        ) : (
            <ChevronDown className="h-4 w-4" />
        );
    };

    return (
        <div className="relative overflow-x-auto">
            <table className={cn(
                "w-full text-left text-sm",
                "border-collapse border border-gray-200 dark:border-gray-700",
                className
            )}>
                <thead className="bg-gray-50 dark:bg-gray-800">
                    <tr>
                        {columns.map((column) => (
                            <th
                                key={column.key.toString()}
                                className={cn(
                                    "border-b border-gray-200 dark:border-gray-700",
                                    "px-4 py-3 font-medium text-gray-900 dark:text-white",
                                    column.sortable && "cursor-pointer hover:bg-gray-100 dark:hover:bg-gray-700"
                                )}
                                onClick={() => column.sortable && handleSort(column.key)}
                            >
                                <div className="flex items-center gap-1">
                                    {column.header}
                                    {column.sortable && getSortIcon(column.key)}
                                </div>
                            </th>
                        ))}
                    </tr>
                </thead>
                <tbody className="divide-y divide-gray-200 dark:divide-gray-700">
                    {isLoading ? (
                        <tr>
                            <td
                                colSpan={columns.length}
                                className="px-4 py-3 text-center text-gray-500 dark:text-gray-400"
                            >
                                Loading...
                            </td>
                        </tr>
                    ) : data.length === 0 ? (
                        <tr>
                            <td
                                colSpan={columns.length}
                                className="px-4 py-3 text-center text-gray-500 dark:text-gray-400"
                            >
                                {emptyMessage}
                            </td>
                        </tr>
                    ) : (
                        data.map((item, index) => (
                            <tr
                                key={index}
                                className={cn(
                                    "bg-white hover:bg-gray-50",
                                    "dark:bg-gray-900 dark:hover:bg-gray-800",
                                    rowClassName
                                )}
                            >
                                {columns.map((column) => (
                                    <td
                                        key={column.key.toString()}
                                        className="px-4 py-3 text-gray-900 dark:text-white"
                                    >
                                        {column.render
                                            ? column.render(item[column.key], item)
                                            : item[column.key]?.toString()}
                                    </td>
                                ))}
                            </tr>
                        ))
                    )}
                </tbody>
            </table>
        </div>
    );
} 