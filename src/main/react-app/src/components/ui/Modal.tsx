"use client";

import { useEffect, useRef } from "react";
import { createPortal } from "react-dom";
import { X } from "lucide-react";
import { cn } from "@/lib/utils";
import { Button } from "./Button";

interface ModalProps {
    isOpen: boolean;
    onClose: () => void;
    title?: string;
    children: React.ReactNode;
    className?: string;
    showCloseButton?: boolean;
    closeOnOutsideClick?: boolean;
}

export const Modal = ({
    isOpen,
    onClose,
    title,
    children,
    className,
    showCloseButton = true,
    closeOnOutsideClick = true
}: ModalProps) => {
    const overlayRef = useRef<HTMLDivElement>(null);
    const modalRef = useRef<HTMLDivElement>(null);

    useEffect(() => {
        const handleEscape = (e: KeyboardEvent) => {
            if (e.key === "Escape") {
                onClose();
            }
        };

        if (isOpen) {
            document.addEventListener("keydown", handleEscape);
            document.body.style.overflow = "hidden";
        }

        return () => {
            document.removeEventListener("keydown", handleEscape);
            document.body.style.overflow = "unset";
        };
    }, [isOpen, onClose]);

    useEffect(() => {
        if (!isOpen) return;

        const overlay = overlayRef.current;
        const modal = modalRef.current;

        if (!overlay || !modal) return;

        const handleClick = (e: MouseEvent) => {
            if (closeOnOutsideClick && e.target === overlay) {
                onClose();
            }
        };

        overlay.addEventListener("click", handleClick);

        return () => {
            overlay.removeEventListener("click", handleClick);
        };
    }, [isOpen, onClose, closeOnOutsideClick]);

    if (!isOpen) return null;

    return createPortal(
        <div
            ref={overlayRef}
            className={cn(
                "fixed inset-0 z-50",
                "flex items-center justify-center",
                "bg-black bg-opacity-50",
                "animate-fade-in"
            )}
        >
            <div
                ref={modalRef}
                className={cn(
                    "relative w-full max-w-lg",
                    "bg-white dark:bg-gray-800",
                    "rounded-lg shadow-xl",
                    "animate-slide-up",
                    className
                )}
            >
                {(title || showCloseButton) && (
                    <div className="flex items-center justify-between border-b border-gray-200 dark:border-gray-700 px-6 py-4">
                        {title && (
                            <h2 className="text-lg font-semibold text-gray-900 dark:text-white">
                                {title}
                            </h2>
                        )}
                        {showCloseButton && (
                            <Button
                                variant="outline"
                                size="sm"
                                className="p-1"
                                onClick={onClose}
                            >
                                <X className="h-4 w-4" />
                            </Button>
                        )}
                    </div>
                )}

                <div className="p-6">{children}</div>
            </div>
        </div>,
        document.body
    );
}; 