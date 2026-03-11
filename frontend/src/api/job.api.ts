import client from './client';

export interface Job {
  jobId: number;
  jobPosition: string;
  description: string | null;
  requirements: string | null;
  salary: number | null;
  companyId: number;
  responsibilities: string | null;
  jobSkill: string | null;
  internshipType: string | null;
}

export type JobRequest = Omit<Job, 'jobId'>;

export const jobApi = {
  getAll: (companyId?: number) =>
    client.get<Job[]>('/jobs', { params: companyId ? { companyId } : {} }).then(r => r.data),
  getById: (id: number) => client.get<Job>(`/jobs/${id}`).then(r => r.data),
  create: (data: JobRequest) => client.post<Job>('/jobs', data).then(r => r.data),
  update: (id: number, data: JobRequest) => client.put<Job>(`/jobs/${id}`, data).then(r => r.data),
  delete: (id: number) => client.delete(`/jobs/${id}`),
};
