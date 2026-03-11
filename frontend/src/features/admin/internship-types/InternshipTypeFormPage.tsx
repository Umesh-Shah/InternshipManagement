import { useEffect } from 'react';
import { useForm } from 'react-hook-form';
import { useNavigate, useParams } from 'react-router-dom';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { internshipTypeApi } from '@/api/internshipType.api';
import type { InternshipTypeRequest } from '@/api/internshipType.api';

export function InternshipTypeFormPage() {
  const { id } = useParams<{ id: string }>();
  const isEdit = !!id;
  const navigate = useNavigate();
  const queryClient = useQueryClient();

  const { data: existing } = useQuery({
    queryKey: ['internshipTypes', Number(id)],
    queryFn: () => internshipTypeApi.getById(Number(id)),
    enabled: isEdit,
  });

  const {
    register,
    handleSubmit,
    reset,
    formState: { errors, isSubmitting },
  } = useForm<InternshipTypeRequest>();

  useEffect(() => {
    if (existing) reset(existing);
  }, [existing, reset]);

  const createMutation = useMutation({
    mutationFn: internshipTypeApi.create,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['internshipTypes'] });
      navigate('/admin/internship-types');
    },
  });

  const updateMutation = useMutation({
    mutationFn: ({ data }: { data: InternshipTypeRequest }) =>
      internshipTypeApi.update(Number(id), data),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['internshipTypes'] });
      navigate('/admin/internship-types');
    },
  });

  function onSubmit(data: InternshipTypeRequest) {
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
        {isEdit ? 'Edit Internship Type' : 'Add Internship Type'}
      </h1>
      <form onSubmit={handleSubmit(onSubmit)} className="space-y-4">
        <div>
          <label className={labelClass}>Name</label>
          <input className={inputClass} {...register('internshipName')} />
        </div>
        <div>
          <label className={labelClass}>Type Code *</label>
          <input
            className={inputClass}
            {...register('internshipType', { required: 'Required' })}
          />
          {errors.internshipType && <p className={errorClass}>{errors.internshipType.message}</p>}
        </div>
        <div>
          <label className={labelClass}>Description</label>
          <textarea className={inputClass} rows={3} {...register('description')} />
        </div>
        <div className="flex gap-3 pt-2">
          <button
            type="submit"
            disabled={isSubmitting || createMutation.isPending || updateMutation.isPending}
            className="px-4 py-2 bg-primary text-primary-foreground rounded-md text-sm hover:opacity-90 transition-opacity disabled:opacity-50"
          >
            {isEdit ? 'Save Changes' : 'Create Type'}
          </button>
          <button
            type="button"
            onClick={() => navigate('/admin/internship-types')}
            className="px-4 py-2 border border-border rounded-md text-sm text-foreground hover:bg-muted transition-colors"
          >
            Cancel
          </button>
        </div>
      </form>
    </div>
  );
}
