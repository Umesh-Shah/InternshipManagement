"use client";

import { useState, useMemo } from "react";

interface UseTableOptions<T> {
    data: T[];
    pageSize?: number;
    initialSort?: {
        key: keyof T;
        direction: "asc" | "desc";
    };
    filterFn?: (item: T, search: string) => boolean;
}

interface UseTableReturn<T> {
    displayData: T[];
    currentPage: number;
    totalPages: number;
    pageSize: number;
    sortConfig: {
        key: keyof T;
        direction: "asc" | "desc";
    } | null;
    searchQuery: string;
    handlePageChange: (page: number) => void;
    handleSort: (key: keyof T, direction: "asc" | "desc") => void;
    handleSearch: (query: string) => void;
    handlePageSizeChange: (size: number) => void;
}

export function useTable<T extends Record<string, any>>({
    data,
    pageSize = 10,
    initialSort,
    filterFn = (item: T, search: string) => {
        return Object.values(item).some(value =>
            value?.toString().toLowerCase().includes(search.toLowerCase())
        );
    }
}: UseTableOptions<T>): UseTableReturn<T> {
    const [currentPage, setCurrentPage] = useState(1);
    const [itemsPerPage, setItemsPerPage] = useState(pageSize);
    const [sortConfig, setSortConfig] = useState<{
        key: keyof T;
        direction: "asc" | "desc";
    } | null>(initialSort || null);
    const [searchQuery, setSearchQuery] = useState("");

    // Filter data based on search query
    const filteredData = useMemo(() => {
        if (!searchQuery) return data;
        return data.filter(item => filterFn(item, searchQuery));
    }, [data, searchQuery, filterFn]);

    // Sort filtered data
    const sortedData = useMemo(() => {
        if (!sortConfig) return filteredData;

        return [...filteredData].sort((a, b) => {
            const aValue = a[sortConfig.key];
            const bValue = b[sortConfig.key];

            if (aValue === bValue) return 0;
            if (aValue === null || aValue === undefined) return 1;
            if (bValue === null || bValue === undefined) return -1;

            const comparison = aValue > bValue ? 1 : -1;
            return sortConfig.direction === "asc" ? comparison : -comparison;
        });
    }, [filteredData, sortConfig]);

    // Calculate pagination
    const totalPages = Math.ceil(sortedData.length / itemsPerPage);
    const startIndex = (currentPage - 1) * itemsPerPage;
    const displayData = sortedData.slice(startIndex, startIndex + itemsPerPage);

    // Ensure current page is valid after data changes
    if (currentPage > totalPages) {
        setCurrentPage(Math.max(1, totalPages));
    }

    const handlePageChange = (page: number) => {
        setCurrentPage(page);
    };

    const handleSort = (key: keyof T, direction: "asc" | "desc") => {
        setSortConfig({ key, direction });
    };

    const handleSearch = (query: string) => {
        setSearchQuery(query);
        setCurrentPage(1); // Reset to first page when searching
    };

    const handlePageSizeChange = (size: number) => {
        setItemsPerPage(size);
        setCurrentPage(1); // Reset to first page when changing page size
    };

    return {
        displayData,
        currentPage,
        totalPages,
        pageSize: itemsPerPage,
        sortConfig,
        searchQuery,
        handlePageChange,
        handleSort,
        handleSearch,
        handlePageSizeChange
    };
} 