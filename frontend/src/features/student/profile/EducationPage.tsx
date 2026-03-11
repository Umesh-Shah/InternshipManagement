import { useEffect } from 'react';
import { useForm } from 'react-hook-form';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { studentApi } from '@/api/student.api';
import type { StudentEducationRequest } from '@/api/student.api';
import useAuthStore from '@/features/auth/useAuthStore';

const inputClass =
  'w-full px-3 py-2 border border-input rounded-md bg-background text-foreground text-sm focus:outline-none focus:ring-2 focus:ring-ring';
const labelClass = 'block text-sm font-medium text-foreground mb-1';

export function EducationPage() {
  const studentId = useAuthStore(s => s.user?.studentId) as number;
  const queryClient = useQueryClient();

  const { data } = useQuery({
    queryKey: ['student-edu', studentId],
    queryFn: () => studentApi.getEducation(studentId),
    enabled: !!studentId,
  });

  const { register, handleSubmit, reset, formState: { isSubmitting } } =
    useForm<StudentEducationRequest>();

  useEffect(() => { if (data) reset(data); }, [data, reset]);

  const mutation = useMutation({
    mutationFn: (req: StudentEducationRequest) => studentApi.upsertEducation(studentId, req),
    onSuccess: () => queryClient.invalidateQueries({ queryKey: ['student-edu', studentId] }),
  });

  return (
    <div className="max-w-2xl">
      <h1 className="text-2xl font-semibold text-foreground mb-6">Education</h1>
      <form onSubmit={handleSubmit(data => mutation.mutate(data))} className="space-y-4">
        <div className="grid grid-cols-2 gap-4">
          <div>
            <label className={labelClass}>Degree Type</label>
            <input className={inputClass} {...register('degreeType')} placeholder="e.g. Bachelor, Master" />
          </div>
          <div>
            <label className={labelClass}>Major</label>
            <input className={inputClass} {...register('major')} />
          </div>
          <div>
            <label className={labelClass}>GPA</label>
            <input className={inputClass} {...register('degreeGpa')} />
          </div>
          <div>
            <label className={labelClass}>University</label>
            <input className={inputClass} {...register('university')} />
          </div>
          <div className="col-span-2">
            <label className={labelClass}>University Location</label>
            <input className={inputClass} {...register('universityLocation')} />
          </div>
        </div>
        {mutation.isSuccess && <p className="text-green-600 text-sm">Saved.</p>}
        {mutation.isError && <p className="text-destructive text-sm">Save failed.</p>}
        <div className="pt-2">
          <button
            type="submit"
            disabled={isSubmitting || mutation.isPending}
            className="px-4 py-2 bg-primary text-primary-foreground rounded-md text-sm hover:opacity-90 transition-opacity disabled:opacity-50"
          >
            Save Changes
          </button>
        </div>
      </form>
    </div>
  );
}
