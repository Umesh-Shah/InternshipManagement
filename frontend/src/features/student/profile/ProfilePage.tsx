import { useEffect } from 'react';
import { useForm } from 'react-hook-form';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { studentApi } from '@/api/student.api';
import type { StudentInfoRequest } from '@/api/student.api';
import useAuthStore from '@/features/auth/useAuthStore';

const inputClass =
  'w-full px-3 py-2 border border-input rounded-md bg-background text-foreground text-sm focus:outline-none focus:ring-2 focus:ring-ring';
const labelClass = 'block text-sm font-medium text-foreground mb-1';

export function ProfilePage() {
  const studentId = useAuthStore(s => s.user?.studentId) as number;
  const queryClient = useQueryClient();

  const { data } = useQuery({
    queryKey: ['student-info', studentId],
    queryFn: () => studentApi.getInfo(studentId),
    enabled: !!studentId,
  });

  const { register, handleSubmit, reset, formState: { isSubmitting } } =
    useForm<StudentInfoRequest>();

  useEffect(() => { if (data) reset(data); }, [data, reset]);

  const mutation = useMutation({
    mutationFn: (req: StudentInfoRequest) => studentApi.updateInfo(studentId, req),
    onSuccess: () => queryClient.invalidateQueries({ queryKey: ['student-info', studentId] }),
  });

  return (
    <div className="max-w-2xl">
      <h1 className="text-2xl font-semibold text-foreground mb-6">My Profile</h1>
      <form onSubmit={handleSubmit(data => mutation.mutate(data))} className="space-y-4">
        <div className="grid grid-cols-2 gap-4">
          <div>
            <label className={labelClass}>First Name</label>
            <input className={inputClass} {...register('fname')} />
          </div>
          <div>
            <label className={labelClass}>Last Name</label>
            <input className={inputClass} {...register('lname')} />
          </div>
          <div>
            <label className={labelClass}>Middle Name</label>
            <input className={inputClass} {...register('mname')} />
          </div>
          <div>
            <label className={labelClass}>Email</label>
            <input className={inputClass} type="email" {...register('stuEmail')} />
          </div>
          <div>
            <label className={labelClass}>Telephone</label>
            <input className={inputClass} {...register('stuTelephone')} />
          </div>
          <div>
            <label className={labelClass}>Gender</label>
            <select className={inputClass} {...register('gender')}>
              <option value="">— Select —</option>
              <option value="M">Male</option>
              <option value="F">Female</option>
              <option value="O">Other</option>
            </select>
          </div>
          <div>
            <label className={labelClass}>Canada Status</label>
            <input className={inputClass} {...register('canadaStatus')} />
          </div>
          <div>
            <label className={labelClass}>Semester</label>
            <input className={inputClass} {...register('semester')} />
          </div>
          <div>
            <label className={labelClass}>Country</label>
            <input className={inputClass} {...register('country')} />
          </div>
          <div>
            <label className={labelClass}>Year</label>
            <input className={inputClass} type="number" {...register('year', { valueAsNumber: true })} />
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
