import client from './client';

export interface JobApplication {
  studentJobId: number;
  jobId: number;
  jobPosition: string | null;
  companyId: number | null;
  companyName: string | null;
  studentId: number;
  studentName: string;
  flag: string;
}

export type JobApplicationRequest = {
  studentId: number;
  jobId: number;
};

export const jobApplicationApi = {
  apply: (data: JobApplicationRequest) =>
    client.post<JobApplication>('/job-applications', data).then(r => r.data),

  getByStudent: (studentId: number) =>
    client.get<JobApplication[]>('/job-applications', { params: { studentId } }).then(r => r.data),

  getPending: () =>
    client.get<JobApplication[]>('/job-applications/pending').then(r => r.data),

  approve: (id: number) =>
    client.put<JobApplication>(`/job-applications/${id}/approve`).then(r => r.data),

  getApprovedByJob: (jobId: number) =>
    client.get<JobApplication[]>('/job-applications/approved', { params: { jobId } }).then(r => r.data),
};
