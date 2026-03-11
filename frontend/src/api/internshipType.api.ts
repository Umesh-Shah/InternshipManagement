import client from './client';

export interface InternshipType {
  internshipId: number;
  internshipType: string;
  description: string | null;
  internshipName: string | null;
}

export type InternshipTypeRequest = Omit<InternshipType, 'internshipId'>;

export const internshipTypeApi = {
  getAll: () => client.get<InternshipType[]>('/internship-types').then(r => r.data),
  getById: (id: number) => client.get<InternshipType>(`/internship-types/${id}`).then(r => r.data),
  create: (data: InternshipTypeRequest) => client.post<InternshipType>('/internship-types', data).then(r => r.data),
  update: (id: number, data: InternshipTypeRequest) => client.put<InternshipType>(`/internship-types/${id}`, data).then(r => r.data),
  delete: (id: number) => client.delete(`/internship-types/${id}`),
};
