import { useEffect } from 'react';
import { useForm } from 'react-hook-form';
import { useNavigate, useParams } from 'react-router-dom';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { companyApi } from '@/api/company.api';
import type { CompanyRequest } from '@/api/company.api';

export function CompanyFormPage() {
  const { id } = useParams<{ id: string }>();
  const isEdit = !!id;
  const navigate = useNavigate();
  const queryClient = useQueryClient();

  const { data: existing } = useQuery({
    queryKey: ['companies', Number(id)],
    queryFn: () => companyApi.getById(Number(id)),
    enabled: isEdit,
  });

  const {
    register,
    handleSubmit,
    reset,
    formState: { errors, isSubmitting },
  } = useForm<CompanyRequest>();

  useEffect(() => {
    if (existing) reset(existing);
  }, [existing, reset]);

  const createMutation = useMutation({
    mutationFn: companyApi.create,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['companies'] });
      navigate('/admin/companies');
    },
  });

  const updateMutation = useMutation({
    mutationFn: ({ data }: { data: CompanyRequest }) => companyApi.update(Number(id), data),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['companies'] });
      navigate('/admin/companies');
    },
  });

  function onSubmit(data: CompanyRequest) {
    if (isEdit) updateMutation.mutate({ data });
    else createMutation.mutate(data);
  }

  const inputClass =
    'w-full px-3 py-2 border border-input rounded-md bg-background text-foreground text-sm focus:outline-none focus:ring-2 focus:ring-ring';
  const labelClass = 'block text-sm font-medium text-foreground mb-1';
  const errorClass = 'text-destructive text-xs mt-1';

  return (
    <div className="max-w-2xl">
      <h1 className="text-2xl font-semibold text-foreground mb-6">
        {isEdit ? 'Edit Company' : 'Add Company'}
      </h1>
      <form onSubmit={handleSubmit(onSubmit)} className="space-y-4">
        <div className="grid grid-cols-2 gap-4">
          <div>
            <label className={labelClass}>Company Name *</label>
            <input className={inputClass} {...register('companyName', { required: 'Required' })} />
            {errors.companyName && <p className={errorClass}>{errors.companyName.message}</p>}
          </div>
          <div>
            <label className={labelClass}>Email</label>
            <input className={inputClass} type="email" {...register('email')} />
          </div>
          <div>
            <label className={labelClass}>Address</label>
            <input className={inputClass} {...register('address')} />
          </div>
          <div>
            <label className={labelClass}>City</label>
            <input className={inputClass} {...register('city')} />
          </div>
          <div>
            <label className={labelClass}>Postal Code</label>
            <input className={inputClass} {...register('postalCode')} />
          </div>
          <div>
            <label className={labelClass}>Country</label>
            <input className={inputClass} {...register('country')} />
          </div>
          <div>
            <label className={labelClass}>Contact First Name</label>
            <input className={inputClass} {...register('contactPersonFname')} />
          </div>
          <div>
            <label className={labelClass}>Contact Last Name</label>
            <input className={inputClass} {...register('contactPersonLname')} />
          </div>
          <div>
            <label className={labelClass}>Contact Position</label>
            <input className={inputClass} {...register('contactPersonPosition')} />
          </div>
          <div>
            <label className={labelClass}>Telephone</label>
            <input className={inputClass} {...register('telephone')} />
          </div>
          <div>
            <label className={labelClass}>Website</label>
            <input className={inputClass} {...register('companyWebsite')} />
          </div>
        </div>
        <div>
          <label className={labelClass}>Notes</label>
          <textarea className={inputClass} rows={3} {...register('notes')} />
        </div>
        <div className="flex gap-3 pt-2">
          <button
            type="submit"
            disabled={isSubmitting || createMutation.isPending || updateMutation.isPending}
            className="px-4 py-2 bg-primary text-primary-foreground rounded-md text-sm hover:opacity-90 transition-opacity disabled:opacity-50"
          >
            {isEdit ? 'Save Changes' : 'Create Company'}
          </button>
          <button
            type="button"
            onClick={() => navigate('/admin/companies')}
            className="px-4 py-2 border border-border rounded-md text-sm text-foreground hover:bg-muted transition-colors"
          >
            Cancel
          </button>
        </div>
      </form>
    </div>
  );
}
