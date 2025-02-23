"use client";

import { useState } from 'react';
import { Student } from '@/lib/types';
import { toast } from 'react-toastify';
import { cn } from '@/lib/utils';

interface StudentProfileFormProps {
    student?: Student;
    onSubmit: (data: Partial<Student>) => Promise<void>;
}

export const StudentProfileForm = ({ student, onSubmit }: StudentProfileFormProps) => {
    const [formData, setFormData] = useState<Partial<Student>>(student || {
        firstName: '',
        lastName: '',
        email: '',
        phone: '',
        major: '',
        gpa: 0,
        graduationYear: new Date().getFullYear() + 4
    });

    const [isSubmitting, setIsSubmitting] = useState(false);

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        setIsSubmitting(true);

        try {
            await onSubmit(formData);
            toast.success('Profile updated successfully!');
        } catch (error) {
            toast.error('Failed to update profile. Please try again.');
        } finally {
            setIsSubmitting(false);
        }
    };

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const { name, value } = e.target;
        setFormData(prev => ({
            ...prev,
            [name]: name === 'gpa' || name === 'graduationYear' ? Number(value) : value
        }));
    };

    return (
        <form onSubmit={handleSubmit} className="space-y-6">
            <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                <div className="space-y-2">
                    <label htmlFor="firstName" className="block text-sm font-medium">
                        First Name
                    </label>
                    <input
                        type="text"
                        id="firstName"
                        name="firstName"
                        value={formData.firstName}
                        onChange={handleChange}
                        className={cn(
                            "w-full px-3 py-2 border rounded-md",
                            "bg-white dark:bg-gray-800",
                            "border-gray-300 dark:border-gray-700",
                            "focus:outline-none focus:ring-2 focus:ring-blue-500"
                        )}
                        required
                    />
                </div>

                <div className="space-y-2">
                    <label htmlFor="lastName" className="block text-sm font-medium">
                        Last Name
                    </label>
                    <input
                        type="text"
                        id="lastName"
                        name="lastName"
                        value={formData.lastName}
                        onChange={handleChange}
                        className={cn(
                            "w-full px-3 py-2 border rounded-md",
                            "bg-white dark:bg-gray-800",
                            "border-gray-300 dark:border-gray-700",
                            "focus:outline-none focus:ring-2 focus:ring-blue-500"
                        )}
                        required
                    />
                </div>

                <div className="space-y-2">
                    <label htmlFor="email" className="block text-sm font-medium">
                        Email
                    </label>
                    <input
                        type="email"
                        id="email"
                        name="email"
                        value={formData.email}
                        onChange={handleChange}
                        className={cn(
                            "w-full px-3 py-2 border rounded-md",
                            "bg-white dark:bg-gray-800",
                            "border-gray-300 dark:border-gray-700",
                            "focus:outline-none focus:ring-2 focus:ring-blue-500"
                        )}
                        required
                    />
                </div>

                <div className="space-y-2">
                    <label htmlFor="phone" className="block text-sm font-medium">
                        Phone
                    </label>
                    <input
                        type="tel"
                        id="phone"
                        name="phone"
                        value={formData.phone}
                        onChange={handleChange}
                        className={cn(
                            "w-full px-3 py-2 border rounded-md",
                            "bg-white dark:bg-gray-800",
                            "border-gray-300 dark:border-gray-700",
                            "focus:outline-none focus:ring-2 focus:ring-blue-500"
                        )}
                        required
                    />
                </div>

                <div className="space-y-2">
                    <label htmlFor="major" className="block text-sm font-medium">
                        Major
                    </label>
                    <input
                        type="text"
                        id="major"
                        name="major"
                        value={formData.major}
                        onChange={handleChange}
                        className={cn(
                            "w-full px-3 py-2 border rounded-md",
                            "bg-white dark:bg-gray-800",
                            "border-gray-300 dark:border-gray-700",
                            "focus:outline-none focus:ring-2 focus:ring-blue-500"
                        )}
                        required
                    />
                </div>

                <div className="space-y-2">
                    <label htmlFor="gpa" className="block text-sm font-medium">
                        GPA
                    </label>
                    <input
                        type="number"
                        id="gpa"
                        name="gpa"
                        min="0"
                        max="4"
                        step="0.01"
                        value={formData.gpa}
                        onChange={handleChange}
                        className={cn(
                            "w-full px-3 py-2 border rounded-md",
                            "bg-white dark:bg-gray-800",
                            "border-gray-300 dark:border-gray-700",
                            "focus:outline-none focus:ring-2 focus:ring-blue-500"
                        )}
                        required
                    />
                </div>

                <div className="space-y-2">
                    <label htmlFor="graduationYear" className="block text-sm font-medium">
                        Expected Graduation Year
                    </label>
                    <input
                        type="number"
                        id="graduationYear"
                        name="graduationYear"
                        min={new Date().getFullYear()}
                        value={formData.graduationYear}
                        onChange={handleChange}
                        className={cn(
                            "w-full px-3 py-2 border rounded-md",
                            "bg-white dark:bg-gray-800",
                            "border-gray-300 dark:border-gray-700",
                            "focus:outline-none focus:ring-2 focus:ring-blue-500"
                        )}
                        required
                    />
                </div>
            </div>

            <div className="flex justify-end">
                <button
                    type="submit"
                    disabled={isSubmitting}
                    className={cn(
                        "px-4 py-2 rounded-md text-white",
                        "bg-blue-600 hover:bg-blue-700",
                        "focus:outline-none focus:ring-2 focus:ring-blue-500",
                        "disabled:opacity-50 disabled:cursor-not-allowed"
                    )}
                >
                    {isSubmitting ? 'Saving...' : 'Save Profile'}
                </button>
            </div>
        </form>
    );
}; 