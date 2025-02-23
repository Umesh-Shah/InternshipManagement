"use client";

import { useState } from "react";
import { cn } from "@/lib/utils";

interface Tab {
    id: string;
    label: string;
    content: React.ReactNode;
    disabled?: boolean;
}

interface TabsProps {
    tabs: Tab[];
    defaultTabId?: string;
    onChange?: (tabId: string) => void;
    className?: string;
    variant?: "default" | "pills" | "underline";
}

export const Tabs = ({
    tabs,
    defaultTabId,
    onChange,
    className,
    variant = "default"
}: TabsProps) => {
    const [activeTab, setActiveTab] = useState(defaultTabId || tabs[0]?.id);

    const handleTabClick = (tabId: string) => {
        setActiveTab(tabId);
        onChange?.(tabId);
    };

    const getTabStyles = (isActive: boolean, isDisabled: boolean | undefined) => {
        const baseStyles = "px-4 py-2 text-sm font-medium transition-colors duration-200";
        const disabledStyles = "opacity-50 cursor-not-allowed";

        switch (variant) {
            case "pills":
                return cn(
                    baseStyles,
                    "rounded-md",
                    isActive
                        ? "bg-primary-100 text-primary-800 dark:bg-primary-800 dark:text-primary-100"
                        : "text-gray-600 hover:bg-gray-100 dark:text-gray-300 dark:hover:bg-gray-800",
                    isDisabled && disabledStyles
                );
            case "underline":
                return cn(
                    baseStyles,
                    "border-b-2",
                    isActive
                        ? "border-primary-500 text-primary-600 dark:border-primary-400 dark:text-primary-400"
                        : "border-transparent text-gray-600 hover:border-gray-300 hover:text-gray-700 dark:text-gray-300 dark:hover:border-gray-700 dark:hover:text-gray-200",
                    isDisabled && disabledStyles
                );
            default:
                return cn(
                    baseStyles,
                    "rounded-t-lg border-b-2",
                    isActive
                        ? "border-primary-500 bg-white text-primary-600 dark:border-primary-400 dark:bg-gray-800 dark:text-primary-400"
                        : "border-transparent text-gray-600 hover:border-gray-300 hover:bg-gray-50 hover:text-gray-700 dark:text-gray-300 dark:hover:border-gray-700 dark:hover:bg-gray-800 dark:hover:text-gray-200",
                    isDisabled && disabledStyles
                );
        }
    };

    return (
        <div className={className}>
            <div className={cn(
                "flex",
                variant === "default" && "border-b border-gray-200 dark:border-gray-700",
                variant === "pills" && "gap-2",
                variant === "underline" && "border-b border-gray-200 dark:border-gray-700 gap-4"
            )}>
                {tabs.map((tab) => (
                    <button
                        key={tab.id}
                        onClick={() => !tab.disabled && handleTabClick(tab.id)}
                        className={getTabStyles(activeTab === tab.id, tab.disabled)}
                        disabled={tab.disabled}
                        type="button"
                    >
                        {tab.label}
                    </button>
                ))}
            </div>
            <div className="mt-4">
                {tabs.find((tab) => tab.id === activeTab)?.content}
            </div>
        </div>
    );
}; 