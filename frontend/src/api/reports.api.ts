import api from './client';

export interface StudentReportRow {
  studentId: number;
  fname: string;
  lname: string;
  year: number;
  country: string;
  semester: string;
  internshipStatus: string;
  studentStatus: string;
}

export interface CompanyReportRow {
  companyId: number;
  companyName: string;
  city: string;
  country: string;
  email: string;
  telephone: string;
}

export interface InternshipTypeReportRow {
  internshipType: string;
  studentCount: number;
}

export interface GpaReportRow {
  studentId: number;
  fname: string;
  lname: string;
  university: string;
  universityLocation: string;
  degreeType: string;
  degreeGpa: string;
}

export interface JobReportRow {
  jobId: number;
  jobPosition: string;
  companyName: string;
  applicantCount: number;
}

export interface ReportFilters {
  years: number[];
  countries: string[];
  universities: string[];
  universityLocations: string[];
  internshipTypes: string[];
}

export interface StudentReportParams {
  year?: number;
  country?: string;
  semester?: string;
  internshipStatus?: string;
  studentStatus?: string;
}

export interface GpaReportParams {
  year?: number;
  country?: string;
  university?: string;
  universityLocation?: string;
  degreeType?: string;
}

function cleanParams(params: Record<string, unknown>) {
  return Object.fromEntries(
    Object.entries(params).filter(([, v]) => v !== undefined && v !== '')
  );
}

export const reportsApi = {
  getFilters: () =>
    api.get<ReportFilters>('/reports/filters').then(r => r.data),

  getStudents: (params: StudentReportParams = {}) =>
    api.get<StudentReportRow[]>('/reports/students', { params: cleanParams(params as Record<string, unknown>) }).then(r => r.data),

  getCompanies: (city?: string) =>
    api.get<CompanyReportRow[]>('/reports/companies', { params: city ? { city } : {} }).then(r => r.data),

  getInternshipTypes: (internshipType?: string) =>
    api.get<InternshipTypeReportRow[]>('/reports/internship-types', {
      params: internshipType ? { internshipType } : {},
    }).then(r => r.data),

  getGpa: (params: GpaReportParams = {}) =>
    api.get<GpaReportRow[]>('/reports/gpa', { params: cleanParams(params as Record<string, unknown>) }).then(r => r.data),

  getJobs: (companyId?: number) =>
    api.get<JobReportRow[]>('/reports/jobs', { params: companyId ? { companyId } : {} }).then(r => r.data),

  downloadStudentsPdf: (params: StudentReportParams = {}) =>
    api.get('/reports/students/pdf', { params: cleanParams(params as Record<string, unknown>), responseType: 'arraybuffer' }),

  downloadCompaniesPdf: (city?: string) =>
    api.get('/reports/companies/pdf', { params: city ? { city } : {}, responseType: 'arraybuffer' }),

  downloadInternshipTypesPdf: (internshipType?: string) =>
    api.get('/reports/internship-types/pdf', {
      params: internshipType ? { internshipType } : {},
      responseType: 'arraybuffer',
    }),

  downloadGpaPdf: (params: GpaReportParams = {}) =>
    api.get('/reports/gpa/pdf', { params: cleanParams(params as Record<string, unknown>), responseType: 'arraybuffer' }),

  downloadJobsPdf: (companyId?: number) =>
    api.get('/reports/jobs/pdf', { params: companyId ? { companyId } : {}, responseType: 'arraybuffer' }),
};
