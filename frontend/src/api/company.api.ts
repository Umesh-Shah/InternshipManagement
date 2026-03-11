import client from './client';

export interface Company {
  companyId: number;
  companyName: string;
  address: string | null;
  city: string | null;
  postalCode: string | null;
  country: string | null;
  contactPersonFname: string | null;
  contactPersonLname: string | null;
  contactPersonPosition: string | null;
  telephone: string | null;
  email: string | null;
  companyWebsite: string | null;
  notes: string | null;
}

export type CompanyRequest = Omit<Company, 'companyId'>;

export const companyApi = {
  getAll: () => client.get<Company[]>('/companies').then(r => r.data),
  getById: (id: number) => client.get<Company>(`/companies/${id}`).then(r => r.data),
  create: (data: CompanyRequest) => client.post<Company>('/companies', data).then(r => r.data),
  update: (id: number, data: CompanyRequest) => client.put<Company>(`/companies/${id}`, data).then(r => r.data),
  delete: (id: number) => client.delete(`/companies/${id}`),
};
