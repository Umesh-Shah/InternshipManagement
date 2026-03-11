import { useEffect } from 'react';
import { useForm } from 'react-hook-form';
import { useNavigate, useParams } from 'react-router-dom';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { jobApi } from '@/api/job.api';
import type { JobRequest } from '@/api/job.api';
import { companyApi } from '@/api/company.api';
import { internshipTypeApi } from '@/api/internshipType.api';

export function JobFormPage() {
  const { id } = useParams<{ id: string }>();
  const isEdit = !!id;
  const navigate = useNavigate();
  const queryClient = useQueryClient();

  const { data: existing } = useQuery({
    queryKey: ['jobs', Number(id)],
    queryFn: () => jobApi.getById(Number(id)),
    enabled: isEdit,
  });
  const { data: companies = [] } = useQuery({
    queryKey: ['companies'],
    queryFn: companyApi.getAll,
  });
  const { data: internshipTypes = [] } = useQuery({
    queryKey: ['internshipTypes'],
    queryFn: internshipTypeApi.getAll,
  });

  const {
    register,
    handleSubmit,
    reset,
    formState: { errors, isSubmitting },
  } = useForm<JobRequest>();

  useEffect(() => {
    if (existing) reset(existing);
  }, [existing, reset]);

  const createMutation = useMutation({
    mutationFn: jobApi.create,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['jobs'] });
      navigate('/admin/jobs');
    },
  });

  const updateMutation = useMutation({
    mutationFn: ({ data }: { data: JobRequest }) => jobApi.update(Number(id), data),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['jobs'] });
      navigate('/admin/jobs');
    },
  });

  function onSubmit(data: JobRequest) {
    // Coerce salary and companyId to numbers since they come from form as strings
    const payload: JobRequest = {
      ...data,
      salary: data.salary ? Number(data.salary) : null,
      companyId: Number(data.companyId),
    };
    if (isEdit) updateMutation.mutate({ data: payload });
    else createMutation.mutate(payload);
  }

  const inputClass =
    'w-full px-3 py-2 border border-input rounded-md bg-background text-foreground text-sm focus:outline-none focus:ring-2 focus:ring-ring';
  const labelClass = 'block text-sm font-medium text-foreground mb-1';
  const errorClass = 'text-destructive text-xs mt-1';

  return (
    <div className="max-w-2xl">
      <h1 className="text-2xl font-semibold text-foreground mb-6">
        {isEdit ? 'Edit Job' : 'Add Job'}
      </h1>
      <form onSubmit={handleSubmit(onSubmit)} className="space-y-4">
        <div className="grid grid-cols-2 gap-4">
          <div>
            <label className={labelClass}>Position *</label>
            <input
              className={inputClass}
              {...register('jobPosition', { required: 'Required' })}
            />
            {errors.jobPosition && <p className={errorClass}>{errors.jobPosition.message}</p>}
          </div>
          <div>
            <label className={labelClass}>Company *</label>
            <select className={inputClass} {...register('companyId', { required: 'Required' })}>
              <option value="">Select company…</option>
              {companies.map(c => (
                <option key={c.companyId} value={c.companyId}>
                  {c.companyName}
                </option>
              ))}
            </select>
            {errors.companyId && <p className={errorClass}>{errors.companyId.message}</p>}
          </div>
          <div>
            <label className={labelClass}>Salary</label>
            <input className={inputClass} type="number" {...register('salary')} />
          </div>
          <div>
            <label className={labelClass}>Internship Type</label>
            <select className={inputClass} {...register('internshipType')}>
              <option value="">Select type…</option>
              {internshipTypes.map(t => (
                <option key={t.internshipId} value={t.internshipType}>
                  {t.internshipName ?? t.internshipType}
                </option>
              ))}
            </select>
          </div>
          <div>
            <label className={labelClass}>Skills (comma separated)</label>
            <input className={inputClass} {...register('jobSkill')} />
          </div>
        </div>
        <div>
          <label className={labelClass}>Description</label>
          <textarea className={inputClass} rows={3} {...register('description')} />
        </div>
        <div>
          <label className={labelClass}>Requirements</label>
          <textarea className={inputClass} rows={3} {...register('requirements')} />
        </div>
        <div>
          <label className={labelClass}>Responsibilities</label>
          <textarea className={inputClass} rows={3} {...register('responsibilities')} />
        </div>
        <div className="flex gap-3 pt-2">
          <button
            type="submit"
            disabled={isSubmitting || createMutation.isPending || updateMutation.isPending}
            className="px-4 py-2 bg-primary text-primary-foreground rounded-md text-sm hover:opacity-90 transition-opacity disabled:opacity-50"
          >
            {isEdit ? 'Save Changes' : 'Create Job'}
          </button>
          <button
            type="button"
            onClick={() => navigate('/admin/jobs')}
            className="px-4 py-2 border border-border rounded-md text-sm text-foreground hover:bg-muted transition-colors"
          >
            Cancel
          </button>
        </div>
      </form>
    </div>
  );
}
