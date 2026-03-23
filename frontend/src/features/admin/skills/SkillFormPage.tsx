import { useEffect } from 'react';
import { useForm } from 'react-hook-form';
import { useNavigate, useParams } from 'react-router-dom';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { skillApi } from '@/api/skill.api';
import type { SkillRequest } from '@/api/skill.api';

export function SkillFormPage() {
  const { id } = useParams<{ id: string }>();
  const isEdit = !!id;
  const navigate = useNavigate();
  const queryClient = useQueryClient();

  const { data: existing } = useQuery({
    queryKey: ['skills', Number(id)],
    queryFn: () => skillApi.getById(Number(id)),
    enabled: isEdit,
  });

  const {
    register,
    handleSubmit,
    reset,
    formState: { errors, isSubmitting },
  } = useForm<SkillRequest>();

  useEffect(() => {
    if (existing) reset(existing);
  }, [existing, reset]);

  const createMutation = useMutation({
    mutationFn: skillApi.create,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['skills'] });
      navigate('/admin/skills');
    },
  });

  const updateMutation = useMutation({
    mutationFn: ({ data }: { data: SkillRequest }) => skillApi.update(Number(id), data),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['skills'] });
      navigate('/admin/skills');
    },
  });

  function onSubmit(data: SkillRequest) {
    if (isEdit) updateMutation.mutate({ data });
    else createMutation.mutate(data);
  }

  const inputClass =
    'w-full px-3 py-2 border border-input rounded-md bg-background text-foreground text-sm focus:outline-none focus:ring-2 focus:ring-ring';
  const labelClass = 'block text-sm font-medium text-foreground mb-1';
  const errorClass = 'text-destructive text-xs mt-1';

  return (
    <div className="max-w-md">
      <h1 className="text-2xl font-semibold text-foreground mb-6">
        {isEdit ? 'Edit Skill' : 'Add Skill'}
      </h1>
      <form onSubmit={handleSubmit(onSubmit)} className="space-y-4">
        <div>
          <label className={labelClass}>Skill Name *</label>
          <input
            className={inputClass}
            {...register('skillName', { required: 'Required' })}
          />
          {errors.skillName && <p className={errorClass}>{errors.skillName.message}</p>}
        </div>
        <div>
          <label className={labelClass}>Skill Type</label>
          <input className={inputClass} {...register('skillType')} />
        </div>
        <div className="flex gap-3 pt-2">
          <button
            type="submit"
            disabled={isSubmitting || createMutation.isPending || updateMutation.isPending}
            className="px-4 py-2 bg-primary text-primary-foreground rounded-md text-sm hover:opacity-90 transition-opacity disabled:opacity-50"
          >
            {isEdit ? 'Save Changes' : 'Create Skill'}
          </button>
          <button
            type="button"
            onClick={() => navigate('/admin/skills')}
            className="px-4 py-2 border border-border rounded-md text-sm text-foreground hover:bg-muted transition-colors"
          >
            Cancel
          </button>
        </div>
      </form>
    </div>
  );
}
