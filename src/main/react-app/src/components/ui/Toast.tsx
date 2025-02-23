"use client";

import { useEffect, useState } from "react";
import { createPortal } from "react-dom";
import { cn } from "@/lib/utils";
import { AlertCircle, CheckCircle, Info, X, XCircle } from "lucide-react";

type ToastType = "success" | "error" | "info" | "warning";

interface ToastProps {
    id: string;
    type: ToastType;
    message: string;
    duration?: number;
    onClose: (id: string) => void;
}

const toastIcons = {
    success: CheckCircle,
    error: XCircle,
    info: Info,
    warning: AlertCircle
};

const toastStyles: Record<ToastType, string> = {
    success: "bg-green-50 text-green-800 dark:bg-green-900 dark:text-green-100",
    error: "bg-red-50 text-red-800 dark:bg-red-900 dark:text-red-100",
    info: "bg-blue-50 text-blue-800 dark:bg-blue-900 dark:text-blue-100",
    warning: "bg-yellow-50 text-yellow-800 dark:bg-yellow-900 dark:text-yellow-100"
};

export const Toast = ({
    id,
    type,
    message,
    duration = 5000,
    onClose
}: ToastProps) => {
    const [isVisible, setIsVisible] = useState(true);
    const Icon = toastIcons[type];

    useEffect(() => {
        const timer = setTimeout(() => {
            setIsVisible(false);
            setTimeout(() => onClose(id), 300); // Wait for animation to complete
        }, duration);

        return () => clearTimeout(timer);
    }, [duration, id, onClose]);

    return (
        <div
            className={cn(
                "flex items-center gap-3 rounded-lg p-4 shadow-lg",
                "transition-all duration-300",
                toastStyles[type],
                isVisible
                    ? "translate-x-0 opacity-100"
                    : "translate-x-full opacity-0"
            )}
            role="alert"
        >
            <Icon className="h-5 w-5 flex-shrink-0" />
            <p className="text-sm">{message}</p>
            <button
                onClick={() => {
                    setIsVisible(false);
                    setTimeout(() => onClose(id), 300);
                }}
                className="ml-auto flex-shrink-0 rounded-lg p-1.5 hover:bg-black hover:bg-opacity-10"
            >
                <X className="h-4 w-4" />
            </button>
        </div>
    );
};

interface ToastContainerProps {
    position?: "top-right" | "top-left" | "bottom-right" | "bottom-left";
    className?: string;
}

const positionStyles = {
    "top-right": "top-0 right-0",
    "top-left": "top-0 left-0",
    "bottom-right": "bottom-0 right-0",
    "bottom-left": "bottom-0 left-0"
};

export const ToastContainer = ({
    position = "top-right",
    className
}: ToastContainerProps) => {
    return createPortal(
        <div
            className={cn(
                "fixed z-50 m-4 flex flex-col gap-2",
                positionStyles[position],
                className
            )}
            role="region"
            aria-label="Notifications"
        >
            {/* Toasts will be rendered here by the ToastProvider */}
        </div>,
        document.body
    );
}; 