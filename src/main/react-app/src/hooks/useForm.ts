"use client";

import { useState } from "react";
import { useToast } from "@/components/providers/ToastProvider";
import { z } from "zod";

interface UseFormOptions<T> {
    initialValues: T;
    validationSchema?: z.ZodSchema<T>;
    onSubmit: (values: T) => Promise<void>;
}

interface UseFormReturn<T> {
    values: T;
    errors: Record<string, string>;
    isSubmitting: boolean;
    handleChange: (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement | HTMLSelectElement>) => void;
    handleSubmit: (e: React.FormEvent) => Promise<void>;
    setFieldValue: (field: keyof T, value: any) => void;
}

export function useForm<T extends Record<string, any>>({
    initialValues,
    validationSchema,
    onSubmit
}: UseFormOptions<T>): UseFormReturn<T> {
    const { toast } = useToast();
    const [values, setValues] = useState<T>(initialValues);
    const [errors, setErrors] = useState<Record<string, string>>({});
    const [isSubmitting, setIsSubmitting] = useState(false);

    const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement | HTMLSelectElement>) => {
        const { name, value, type } = e.target;
        let parsedValue: any = value;

        // Handle different input types
        if (type === "number") {
            parsedValue = value === "" ? "" : Number(value);
        } else if (type === "checkbox") {
            parsedValue = (e.target as HTMLInputElement).checked;
        }

        setValues(prev => ({
            ...prev,
            [name]: parsedValue
        }));

        // Clear error when field is modified
        if (errors[name]) {
            setErrors(prev => {
                const newErrors = { ...prev };
                delete newErrors[name];
                return newErrors;
            });
        }
    };

    const setFieldValue = (field: keyof T, value: any) => {
        setValues(prev => ({
            ...prev,
            [field]: value
        }));
    };

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        setIsSubmitting(true);
        setErrors({});

        try {
            let validatedValues = values;

            if (validationSchema) {
                validatedValues = validationSchema.parse(values);
            }

            await onSubmit(validatedValues);
            toast('success', 'Form submitted successfully');
        } catch (error) {
            if (error instanceof z.ZodError) {
                const newErrors: Record<string, string> = {};
                error.errors.forEach((err) => {
                    if (err.path[0]) {
                        newErrors[err.path[0].toString()] = err.message;
                    }
                });
                setErrors(newErrors);
                toast('error', 'Please fix the form errors');
            } else {
                toast('error', 'An error occurred while submitting the form');
                console.error("Form submission error:", error);
            }
        } finally {
            setIsSubmitting(false);
        }
    };

    return {
        values,
        errors,
        isSubmitting,
        handleChange,
        handleSubmit,
        setFieldValue
    };
} 