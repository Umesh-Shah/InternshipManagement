"use client";

import { Table } from "./Table";
import { Pagination } from "./Pagination";
import { SearchInput } from "./SearchInput";
import { useTable } from "@/hooks/useTable";
import { cn } from "@/lib/utils";

export interface Column<T> {
    key: keyof T;
    header: string;
    sortable?: boolean;
    render?: (value: T[keyof T], item: T) => React.ReactNode;
}

interface DataTableProps<T> {
    data: T[];
    columns: Column<T>[];
    pageSize?: number;
    className?: string;
    rowClassName?: string;
    showSearch?: boolean;
    searchPlaceholder?: string;
    emptyMessage?: string;
    isLoading?: boolean;
    filterFn?: (item: T, search: string) => boolean;
}

export function DataTable<T extends Record<string, any>>({
    data,
    columns,
    pageSize = 10,
    className,
    rowClassName,
    showSearch = true,
    searchPlaceholder = "Search...",
    emptyMessage = "No data available",
    isLoading = false,
    filterFn
}: DataTableProps<T>) {
    const {
        displayData,
        currentPage,
        totalPages,
        sortConfig,
        searchQuery,
        handlePageChange,
        handleSort,
        handleSearch
    } = useTable({
        data,
        pageSize,
        filterFn
    });

    return (
        <div className="space-y-4">
            {showSearch && (
                <div className="flex justify-end">
                    <div className="w-full max-w-xs">
                        <SearchInput
                            value={searchQuery}
                            onChange={handleSearch}
                            placeholder={searchPlaceholder}
                        />
                    </div>
                </div>
            )}

            <div className={cn("rounded-lg border border-gray-200 dark:border-gray-700", className)}>
                <Table
                    data={displayData}
                    columns={columns}
                    onSort={handleSort}
                    className="rounded-t-lg"
                    rowClassName={rowClassName}
                    emptyMessage={emptyMessage}
                    isLoading={isLoading}
                />

                <div className="border-t border-gray-200 dark:border-gray-700 p-4">
                    <Pagination
                        currentPage={currentPage}
                        totalPages={totalPages}
                        onPageChange={handlePageChange}
                    />
                </div>
            </div>
        </div>
    );
} 