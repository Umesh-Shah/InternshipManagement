import { useForm } from 'react-hook-form';
import { useNavigate } from 'react-router-dom';
import { useMutation, useQueryClient } from '@tanstack/react-query';
import { studentApi } from '@/api/student.api';
import type { StudentCreateRequest } from '@/api/student.api';

export function StudentFormPage() {
  const navigate = useNavigate();
  const queryClient = useQueryClient();

  const {
    register,
    handleSubmit,
    formState: { errors, isSubmitting },
  } = useForm<StudentCreateRequest>();

  const createMutation = useMutation({
    mutationFn: studentApi.create,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['students'] });
      navigate('/admin/students');
    },
  });

  const inputClass =
    'w-full px-3 py-2 border border-input rounded-md bg-background text-foreground text-sm focus:outline-none focus:ring-2 focus:ring-ring';
  const labelClass = 'block text-sm font-medium text-foreground mb-1';
  const errorClass = 'text-destructive text-xs mt-1';

  return (
    <div className="max-w-2xl">
      <h1 className="text-2xl font-semibold text-foreground mb-6">Add Student</h1>
      <form onSubmit={handleSubmit(data => createMutation.mutate(data))} className="space-y-4">
        <div className="grid grid-cols-2 gap-4">
          <div>
            <label className={labelClass}>Student ID *</label>
            <input
              className={inputClass}
              type="number"
              {...register('studentId', { required: 'Required', valueAsNumber: true })}
            />
            {errors.studentId && <p className={errorClass}>{errors.studentId.message}</p>}
          </div>
          <div>
            <label className={labelClass}>Year</label>
            <input className={inputClass} type="number" {...register('year', { valueAsNumber: true })} />
          </div>
          <div>
            <label className={labelClass}>First Name *</label>
            <input className={inputClass} {...register('fname', { required: 'Required' })} />
            {errors.fname && <p className={errorClass}>{errors.fname.message}</p>}
          </div>
          <div>
            <label className={labelClass}>Last Name *</label>
            <input className={inputClass} {...register('lname', { required: 'Required' })} />
            {errors.lname && <p className={errorClass}>{errors.lname.message}</p>}
          </div>
          <div>
            <label className={labelClass}>Middle Name</label>
            <input className={inputClass} {...register('mname')} />
          </div>
          <div>
            <label className={labelClass}>Email *</label>
            <input
              className={inputClass}
              type="email"
              {...register('stuEmail', { required: 'Required' })}
            />
            {errors.stuEmail && <p className={errorClass}>{errors.stuEmail.message}</p>}
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
        </div>

        {createMutation.isError && (
          <p className="text-destructive text-sm">
            Failed to create student. The student ID may already exist.
          </p>
        )}

        <div className="flex gap-3 pt-2">
          <button
            type="submit"
            disabled={isSubmitting || createMutation.isPending}
            className="px-4 py-2 bg-primary text-primary-foreground rounded-md text-sm hover:opacity-90 transition-opacity disabled:opacity-50"
          >
            Create Student
          </button>
          <button
            type="button"
            onClick={() => navigate('/admin/students')}
            className="px-4 py-2 border border-border rounded-md text-sm text-foreground hover:bg-muted transition-colors"
          >
            Cancel
          </button>
        </div>
      </form>
    </div>
  );
}
