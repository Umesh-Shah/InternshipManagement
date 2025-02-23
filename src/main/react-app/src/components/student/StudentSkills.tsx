"use client";

import { useState } from 'react';
import { Skill } from '@/lib/types';
import { toast } from 'react-toastify';
import { cn } from '@/lib/utils';
import { X } from 'lucide-react';

interface StudentSkillsProps {
    skills: Skill[];
    onUpdate: (skills: Skill[]) => Promise<void>;
}

export const StudentSkills = ({ skills: initialSkills, onUpdate }: StudentSkillsProps) => {
    const [skills, setSkills] = useState<Skill[]>(initialSkills);
    const [newSkill, setNewSkill] = useState({
        name: '',
        proficiency: 'beginner' as Skill['proficiency']
    });
    const [isSubmitting, setIsSubmitting] = useState(false);

    const handleAddSkill = async (e: React.FormEvent) => {
        e.preventDefault();
        if (!newSkill.name.trim()) {
            toast.error('Please enter a skill name');
            return;
        }

        setIsSubmitting(true);
        try {
            const updatedSkills = [...skills, { ...newSkill, id: Date.now().toString() }];
            await onUpdate(updatedSkills);
            setSkills(updatedSkills);
            setNewSkill({ name: '', proficiency: 'beginner' });
            toast.success('Skill added successfully!');
        } catch (error) {
            toast.error('Failed to add skill. Please try again.');
        } finally {
            setIsSubmitting(false);
        }
    };

    const handleRemoveSkill = async (skillId: string) => {
        setIsSubmitting(true);
        try {
            const updatedSkills = skills.filter(skill => skill.id !== skillId);
            await onUpdate(updatedSkills);
            setSkills(updatedSkills);
            toast.success('Skill removed successfully!');
        } catch (error) {
            toast.error('Failed to remove skill. Please try again.');
        } finally {
            setIsSubmitting(false);
        }
    };

    return (
        <div className="space-y-6">
            <div className="grid gap-4">
                {skills.map(skill => (
                    <div
                        key={skill.id}
                        className={cn(
                            "flex items-center justify-between p-3 rounded-md",
                            "bg-gray-50 dark:bg-gray-800",
                            "border border-gray-200 dark:border-gray-700"
                        )}
                    >
                        <div>
                            <h3 className="font-medium">{skill.name}</h3>
                            <p className="text-sm text-gray-500 dark:text-gray-400 capitalize">
                                {skill.proficiency}
                            </p>
                        </div>
                        <button
                            onClick={() => handleRemoveSkill(skill.id)}
                            className={cn(
                                "p-1 rounded-full text-gray-500 hover:text-red-500",
                                "hover:bg-gray-100 dark:hover:bg-gray-700"
                            )}
                        >
                            <X size={16} />
                        </button>
                    </div>
                ))}
            </div>

            <form onSubmit={handleAddSkill} className="space-y-4">
                <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                    <div className="space-y-2">
                        <label htmlFor="skillName" className="block text-sm font-medium">
                            Skill Name
                        </label>
                        <input
                            type="text"
                            id="skillName"
                            value={newSkill.name}
                            onChange={e => setNewSkill(prev => ({ ...prev, name: e.target.value }))}
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
                        <label htmlFor="proficiency" className="block text-sm font-medium">
                            Proficiency Level
                        </label>
                        <select
                            id="proficiency"
                            value={newSkill.proficiency}
                            onChange={e => setNewSkill(prev => ({ ...prev, proficiency: e.target.value as Skill['proficiency'] }))}
                            className={cn(
                                "w-full px-3 py-2 border rounded-md",
                                "bg-white dark:bg-gray-800",
                                "border-gray-300 dark:border-gray-700",
                                "focus:outline-none focus:ring-2 focus:ring-blue-500"
                            )}
                            required
                        >
                            <option value="beginner">Beginner</option>
                            <option value="intermediate">Intermediate</option>
                            <option value="advanced">Advanced</option>
                        </select>
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
                        {isSubmitting ? 'Adding...' : 'Add Skill'}
                    </button>
                </div>
            </form>
        </div>
    );
}; 