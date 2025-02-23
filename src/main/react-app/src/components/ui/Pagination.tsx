"use client";

import { cn } from "@/lib/utils";
import { ChevronLeft, ChevronRight, ChevronsLeft, ChevronsRight } from "lucide-react";
import { Button } from "./Button";

interface PaginationProps {
    currentPage: number;
    totalPages: number;
    onPageChange: (page: number) => void;
    className?: string;
}

export const Pagination = ({
    currentPage,
    totalPages,
    onPageChange,
    className
}: PaginationProps) => {
    const getPageNumbers = () => {
        const pages: (number | string)[] = [];
        const maxVisiblePages = 5;

        if (totalPages <= maxVisiblePages) {
            return Array.from({ length: totalPages }, (_, i) => i + 1);
        }

        // Always show first page
        pages.push(1);

        // Calculate start and end of visible pages
        let start = Math.max(2, currentPage - 1);
        let end = Math.min(totalPages - 1, currentPage + 1);

        // Adjust if we're near the start
        if (currentPage <= 3) {
            end = Math.min(maxVisiblePages - 1, totalPages - 1);
        }

        // Adjust if we're near the end
        if (currentPage >= totalPages - 2) {
            start = Math.max(2, totalPages - (maxVisiblePages - 2));
        }

        // Add ellipsis if needed
        if (start > 2) {
            pages.push("...");
        }

        // Add visible page numbers
        for (let i = start; i <= end; i++) {
            pages.push(i);
        }

        // Add ellipsis if needed
        if (end < totalPages - 1) {
            pages.push("...");
        }

        // Always show last page
        if (totalPages > 1) {
            pages.push(totalPages);
        }

        return pages;
    };

    if (totalPages <= 1) return null;

    return (
        <div className={cn("flex items-center justify-center gap-2", className)}>
            <Button
                variant="outline"
                size="sm"
                onClick={() => onPageChange(1)}
                disabled={currentPage === 1}
            >
                <ChevronsLeft className="h-4 w-4" />
            </Button>
            <Button
                variant="outline"
                size="sm"
                onClick={() => onPageChange(currentPage - 1)}
                disabled={currentPage === 1}
            >
                <ChevronLeft className="h-4 w-4" />
            </Button>

            <div className="flex items-center gap-1">
                {getPageNumbers().map((page, index) => (
                    typeof page === "number" ? (
                        <Button
                            key={index}
                            variant={currentPage === page ? "primary" : "outline"}
                            size="sm"
                            onClick={() => onPageChange(page)}
                            className="min-w-[2.5rem]"
                        >
                            {page}
                        </Button>
                    ) : (
                        <span
                            key={index}
                            className="px-2 text-gray-500 dark:text-gray-400"
                        >
                            {page}
                        </span>
                    )
                ))}
            </div>

            <Button
                variant="outline"
                size="sm"
                onClick={() => onPageChange(currentPage + 1)}
                disabled={currentPage === totalPages}
            >
                <ChevronRight className="h-4 w-4" />
            </Button>
            <Button
                variant="outline"
                size="sm"
                onClick={() => onPageChange(totalPages)}
                disabled={currentPage === totalPages}
            >
                <ChevronsRight className="h-4 w-4" />
            </Button>
        </div>
    );
}; 