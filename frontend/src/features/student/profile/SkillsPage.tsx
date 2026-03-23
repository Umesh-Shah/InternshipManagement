import { useState } from 'react';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { studentApi } from '@/api/student.api';
import { skillApi } from '@/api/skill.api';
import type { Skill } from '@/api/skill.api';
import useAuthStore from '@/features/auth/useAuthStore';
import { Check } from 'lucide-react';

export function SkillsPage() {
  const studentId = useAuthStore(s => s.user?.studentId) as number;
  const queryClient = useQueryClient();

  const { data: allSkills = [] } = useQuery({
    queryKey: ['skills'],
    queryFn: skillApi.getAll,
  });

  const { data: studentSkills = [] } = useQuery({
    queryKey: ['student-skills', studentId],
    queryFn: () => studentApi.getSkills(studentId),
    enabled: !!studentId,
  });

  const selectedIds = new Set(studentSkills.map(s => s.skillId));
  const [pending, setPending] = useState<Set<number> | null>(null);
  const active = pending ?? selectedIds;

  const mutation = useMutation({
    mutationFn: (skillIds: number[]) => studentApi.replaceSkills(studentId, { skillIds }),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['student-skills', studentId] });
      setPending(null);
    },
  });

  function toggle(skillId: number) {
    const next = new Set(active);
    if (next.has(skillId)) next.delete(skillId);
    else next.add(skillId);
    setPending(next);
  }

  return (
    <div className="max-w-2xl">
      <h1 className="text-2xl font-semibold text-foreground mb-6">Skills</h1>
      <p className="text-sm text-muted-foreground mb-4">Select all skills that apply to you.</p>

      <div className="grid grid-cols-2 gap-2 mb-6">
        {allSkills.map((skill: Skill) => {
          const isSelected = active.has(skill.skillId);
          return (
            <button
              key={skill.skillId}
              type="button"
              onClick={() => toggle(skill.skillId)}
              className={`flex items-center gap-2 px-3 py-2 rounded-md border text-sm transition-colors text-left ${
                isSelected
                  ? 'border-primary bg-primary/10 text-primary'
                  : 'border-border text-foreground hover:bg-muted'
              }`}
            >
              <span className={`w-4 h-4 rounded border flex items-center justify-center shrink-0 ${
                isSelected ? 'border-primary bg-primary text-primary-foreground' : 'border-input'
              }`}>
                {isSelected && <Check size={10} />}
              </span>
              {skill.skillName}
            </button>
          );
        })}
      </div>

      {pending !== null && (
        <div className="flex items-center gap-3">
          <button
            onClick={() => mutation.mutate(Array.from(active))}
            disabled={mutation.isPending}
            className="px-4 py-2 bg-primary text-primary-foreground rounded-md text-sm hover:opacity-90 transition-opacity disabled:opacity-50"
          >
            Save Changes
          </button>
          <button
            onClick={() => setPending(null)}
            className="px-4 py-2 border border-border rounded-md text-sm text-foreground hover:bg-muted transition-colors"
          >
            Cancel
          </button>
        </div>
      )}
      {mutation.isSuccess && pending === null && (
        <p className="text-green-600 text-sm">Skills updated.</p>
      )}
      {mutation.isError && <p className="text-destructive text-sm">Save failed.</p>}
    </div>
  );
}
