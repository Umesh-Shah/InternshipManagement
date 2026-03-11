import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useForm } from 'react-hook-form';
import { login } from '@/api/auth.api';
import useAuthStore from './useAuthStore';
import { Button } from '@/components/ui/button';

interface FormValues {
  username: string;
  password: string;
}

export default function LoginPage() {
  const navigate = useNavigate();
  const setAuth = useAuthStore((s) => s.setAuth);
  const [error, setError] = useState<string | null>(null);

  const {
    register,
    handleSubmit,
    formState: { errors, isSubmitting },
  } = useForm<FormValues>();

  const onSubmit = async (data: FormValues) => {
    setError(null);
    try {
      const res = await login(data);
      setAuth(res.token, {
        username: res.username,
        role: res.role,
        studentId: res.studentId,
      });
      navigate(res.role === 'ROLE_ADMIN' ? '/admin' : '/student', { replace: true });
    } catch {
      setError('Invalid username or password.');
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-background">
      <div className="w-full max-w-sm space-y-6 p-8 rounded-xl border border-border shadow-sm bg-card">
        <div>
          <h1 className="text-2xl font-semibold tracking-tight text-card-foreground">
            IMS Login
          </h1>
          <p className="text-sm text-muted-foreground mt-1">Internship Management System</p>
        </div>

        <form onSubmit={handleSubmit(onSubmit)} className="space-y-4">
          <div className="space-y-1">
            <label className="text-sm font-medium" htmlFor="username">
              Username
            </label>
            <input
              id="username"
              autoComplete="username"
              className="w-full rounded-lg border border-input bg-background px-3 py-2 text-sm outline-none focus:ring-2 focus:ring-ring"
              {...register('username', { required: 'Username is required' })}
            />
            {errors.username && (
              <p className="text-xs text-destructive">{errors.username.message}</p>
            )}
          </div>

          <div className="space-y-1">
            <label className="text-sm font-medium" htmlFor="password">
              Password
            </label>
            <input
              id="password"
              type="password"
              autoComplete="current-password"
              className="w-full rounded-lg border border-input bg-background px-3 py-2 text-sm outline-none focus:ring-2 focus:ring-ring"
              {...register('password', { required: 'Password is required' })}
            />
            {errors.password && (
              <p className="text-xs text-destructive">{errors.password.message}</p>
            )}
          </div>

          {error && <p className="text-sm text-destructive">{error}</p>}

          <Button type="submit" className="w-full" disabled={isSubmitting}>
            {isSubmitting ? 'Signing in…' : 'Sign in'}
          </Button>
        </form>
      </div>
    </div>
  );
}
