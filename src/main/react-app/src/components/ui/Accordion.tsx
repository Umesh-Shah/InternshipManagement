"use client";

import { useState } from "react";
import { cn } from "@/lib/utils";
import { ChevronDown } from "lucide-react";

interface AccordionItem {
    id: string;
    title: string;
    content: React.ReactNode;
    disabled?: boolean;
}

interface AccordionProps {
    items: AccordionItem[];
    defaultExpandedIds?: string[];
    allowMultiple?: boolean;
    className?: string;
    onChange?: (expandedIds: string[]) => void;
}

export const Accordion = ({
    items,
    defaultExpandedIds = [],
    allowMultiple = false,
    className,
    onChange
}: AccordionProps) => {
    const [expandedIds, setExpandedIds] = useState<string[]>(defaultExpandedIds);

    const handleItemClick = (itemId: string) => {
        let newExpandedIds: string[];

        if (allowMultiple) {
            newExpandedIds = expandedIds.includes(itemId)
                ? expandedIds.filter(id => id !== itemId)
                : [...expandedIds, itemId];
        } else {
            newExpandedIds = expandedIds.includes(itemId)
                ? []
                : [itemId];
        }

        setExpandedIds(newExpandedIds);
        onChange?.(newExpandedIds);
    };

    return (
        <div className={cn("divide-y divide-gray-200 dark:divide-gray-700", className)}>
            {items.map((item) => {
                const isExpanded = expandedIds.includes(item.id);

                return (
                    <div key={item.id} className="py-1">
                        <button
                            onClick={() => !item.disabled && handleItemClick(item.id)}
                            className={cn(
                                "flex w-full items-center justify-between px-4 py-3",
                                "text-left text-sm font-medium",
                                "transition-colors duration-200",
                                item.disabled
                                    ? "cursor-not-allowed opacity-50"
                                    : "hover:bg-gray-50 dark:hover:bg-gray-800"
                            )}
                            disabled={item.disabled}
                            type="button"
                            aria-expanded={isExpanded}
                        >
                            <span>{item.title}</span>
                            <ChevronDown
                                className={cn(
                                    "h-5 w-5 text-gray-500 transition-transform duration-200",
                                    isExpanded && "rotate-180"
                                )}
                            />
                        </button>
                        <div
                            className={cn(
                                "overflow-hidden transition-all duration-200",
                                isExpanded ? "max-h-96" : "max-h-0"
                            )}
                        >
                            <div className="px-4 pb-3 pt-1">
                                {item.content}
                            </div>
                        </div>
                    </div>
                );
            })}
        </div>
    );
}; 