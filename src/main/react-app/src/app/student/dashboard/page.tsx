"use client";

import { useEffect, useState } from 'react';
import { useAuth } from '@/contexts/AuthContext';
import { Student } from '@/lib/types';
import { StudentProfileForm } from '@/components/student/StudentProfileForm';
import { StudentSkills } from '@/components/student/StudentSkills';
import { toast } from 'react-toastify';
import { cn } from '@/lib/utils';

export default function StudentDashboard() {
    const { user } = useAuth();
    const [student, setStudent] = useState<Student | null>(null);
    const [activeTab, setActiveTab] = useState<'profile' | 'skills'>('profile');
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const fetchStudentData = async () => {
            try {
                const response = await fetch('/api/student/profile', {
                    headers: {
                        'Authorization': `Bearer ${localStorage.getItem('token')}`
                    }
                });

                if (response.ok) {
                    const data = await response.json();
                    setStudent(data.student);
                } else {
                    toast.error('Failed to load student profile');
                }
            } catch (error) {
                toast.error('Error loading student profile');
            } finally {
                setLoading(false);
            }
        };

        if (user) {
            fetchStudentData();
        }
    }, [user]);

    const handleProfileUpdate = async (data: Partial<Student>) => {
        try {
            const response = await fetch('/api/student/profile', {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${localStorage.getItem('token')}`
                },
                body: JSON.stringify(data)
            });

            if (response.ok) {
                const updatedData = await response.json();
                setStudent(updatedData.student);
                return Promise.resolve();
            } else {
                return Promise.reject();
            }
        } catch (error) {
            return Promise.reject(error);
        }
    };

    const handleSkillsUpdate = async (skills: Student['skills']) => {
        try {
            const response = await fetch('/api/student/skills', {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${localStorage.getItem('token')}`
                },
                body: JSON.stringify({ skills })
            });

            if (response.ok) {
                const updatedData = await response.json();
                setStudent(prev => prev ? { ...prev, skills: updatedData.skills } : null);
                return Promise.resolve();
            } else {
                return Promise.reject();
            }
        } catch (error) {
            return Promise.reject(error);
        }
    };

    if (loading) {
        return (
            <div className="flex items-center justify-center min-h-screen">
                <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-blue-600"></div>
            </div>
        );
    }

    return (
        <div className="max-w-4xl mx-auto py-8 px-4">
            <h1 className="text-3xl font-bold mb-8">Student Dashboard</h1>

            <div className="mb-6">
                <div className="border-b border-gray-200 dark:border-gray-700">
                    <nav className="flex space-x-8">
                        <button
                            onClick={() => setActiveTab('profile')}
                            className={cn(
                                "py-4 px-1 border-b-2 font-medium text-sm",
                                activeTab === 'profile'
                                    ? "border-blue-500 text-blue-600"
                                    : "border-transparent text-gray-500 hover:text-gray-700 hover:border-gray-300"
                            )}
                        >
                            Profile
                        </button>
                        <button
                            onClick={() => setActiveTab('skills')}
                            className={cn(
                                "py-4 px-1 border-b-2 font-medium text-sm",
                                activeTab === 'skills'
                                    ? "border-blue-500 text-blue-600"
                                    : "border-transparent text-gray-500 hover:text-gray-700 hover:border-gray-300"
                            )}
                        >
                            Skills
                        </button>
                    </nav>
                </div>
            </div>

            <div className="bg-white dark:bg-gray-800 rounded-lg shadow p-6">
                {activeTab === 'profile' && (
                    <StudentProfileForm
                        student={student || undefined}
                        onSubmit={handleProfileUpdate}
                    />
                )}
                {activeTab === 'skills' && student && (
                    <StudentSkills
                        skills={student.skills}
                        onUpdate={handleSkillsUpdate}
                    />
                )}
            </div>
        </div>
    );
} 