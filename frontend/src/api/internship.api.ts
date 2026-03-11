import client from './client';

export interface InternshipStatus {
  studentInternshipId: number;
  studentId: number;
  studentName: string;
  companyId: number;
  companyName: string | null;
  jobId: number;
  jobPosition: string | null;
  internshipId: number;
  internshipType: string;
  internshipStatus: string | null;
  studentStatus: string | null;
}

export interface AssignInternshipRequest {
  studentId: number;
  companyId: number;
  jobId: number;
  internshipId: number;
  internshipType: string;
  internshipStatus: string;
}

export const internshipApi = {
  getAll: () => client.get<InternshipStatus[]>('/internships/status').then(r => r.data),
  getByStudent: (studentId: number) =>
    client.get<InternshipStatus[]>(`/internships/status/student/${studentId}`).then(r => r.data),
  assign: (data: AssignInternshipRequest) =>
    client.post<InternshipStatus>('/internships/status', data).then(r => r.data),
};
