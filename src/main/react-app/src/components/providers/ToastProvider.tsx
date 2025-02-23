"use client";

import {
    createContext,
    useCallback,
    useContext,
    useState,
    type ReactNode
} from "react";
import { Toast, ToastContainer } from "@/components/ui/Toast";

interface Toast {
    id: string;
    type: "success" | "error" | "info" | "warning";
    message: string;
}

interface ToastContextValue {
    toast: (type: Toast["type"], message: string) => void;
}

const ToastContext = createContext<ToastContextValue | null>(null);

export const useToast = () => {
    const context = useContext(ToastContext);
    if (!context) {
        throw new Error("useToast must be used within a ToastProvider");
    }
    return context;
};

interface ToastProviderProps {
    children: ReactNode;
    position?: "top-right" | "top-left" | "bottom-right" | "bottom-left";
}

export const ToastProvider = ({
    children,
    position = "top-right"
}: ToastProviderProps) => {
    const [toasts, setToasts] = useState<Toast[]>([]);

    const removeToast = useCallback((id: string) => {
        setToasts((prev) => prev.filter((toast) => toast.id !== id));
    }, []);

    const addToast = useCallback((type: Toast["type"], message: string) => {
        const id = Math.random().toString(36).substring(2, 9);
        setToasts((prev) => [...prev, { id, type, message }]);
    }, []);

    return (
        <ToastContext.Provider value={{ toast: addToast }}>
            {children}
            <ToastContainer position={position}>
                {toasts.map((toast) => (
                    <Toast
                        key={toast.id}
                        id={toast.id}
                        type={toast.type}
                        message={toast.message}
                        onClose={removeToast}
                    />
                ))}
            </ToastContainer>
        </ToastContext.Provider>
    );
}; 