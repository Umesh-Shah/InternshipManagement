import client from './client';

export interface StudentInfo {
  id: number;
  studentId: number;
  year: number | null;
  fname: string;
  lname: string;
  mname: string | null;
  stuEmail: string;
  stuTelephone: string | null;
  gender: string | null;
  canadaStatus: string | null;
  semester: string | null;
  internshipStatus: string | null;
  studentStatus: string | null;
  country: string | null;
}

export interface StudentEducation {
  stuEduId: number;
  studentId: number;
  degreeType: string | null;
  major: string | null;
  degreeGpa: string | null;
  university: string | null;
  universityLocation: string | null;
}

export interface StudentCertificate {
  certificateId: number;
  studentId: string;
  certificateTitle: string | null;
  certificateBody: string | null;
}

export interface StudentWork {
  stuWorkId: number;
  studentId: number;
  startDate: string | null;
  endDate: string | null;
  company: string | null;
  companyLocation: string | null;
  position: string | null;
}

export interface StudentSkill {
  studentSkillId: number;
  skillId: number;
  studentId: number;
  skillName: string | null;
}

export type StudentCreateRequest = {
  studentId: number;
  year?: number | null;
  fname: string;
  lname: string;
  mname?: string | null;
  stuEmail: string;
  stuTelephone?: string | null;
  gender?: string | null;
  canadaStatus?: string | null;
  semester?: string | null;
  country?: string | null;
};

export type StudentInfoRequest = Partial<Omit<StudentInfo, 'id' | 'studentId'>>;
export type StudentEducationRequest = Omit<StudentEducation, 'stuEduId' | 'studentId'>;
export type StudentCertificateRequest = Omit<StudentCertificate, 'certificateId' | 'studentId'>;
export type StudentWorkRequest = Omit<StudentWork, 'stuWorkId' | 'studentId'>;
export type StudentSkillsRequest = { skillIds: number[] };

const base = (studentId: number) => `/students/${studentId}`;

export const studentApi = {
  // Admin
  getAll: () => client.get<StudentInfo[]>('/students').then(r => r.data),
  create: (data: StudentCreateRequest) => client.post<StudentInfo>('/students', data).then(r => r.data),

  // Info
  getInfo: (studentId: number) =>
    client.get<StudentInfo>(`${base(studentId)}/info`).then(r => r.data),
  updateInfo: (studentId: number, data: StudentInfoRequest) =>
    client.put<StudentInfo>(`${base(studentId)}/info`, data).then(r => r.data),

  // Education
  getEducation: (studentId: number) =>
    client.get<StudentEducation>(`${base(studentId)}/education`).then(r => r.data),
  upsertEducation: (studentId: number, data: StudentEducationRequest) =>
    client.put<StudentEducation>(`${base(studentId)}/education`, data).then(r => r.data),

  // Certificate
  getCertificate: (studentId: number) =>
    client.get<StudentCertificate>(`${base(studentId)}/certificates`).then(r => r.data),
  upsertCertificate: (studentId: number, data: StudentCertificateRequest) =>
    client.put<StudentCertificate>(`${base(studentId)}/certificates`, data).then(r => r.data),

  // Work
  getWork: (studentId: number) =>
    client.get<StudentWork>(`${base(studentId)}/work`).then(r => r.data),
  upsertWork: (studentId: number, data: StudentWorkRequest) =>
    client.put<StudentWork>(`${base(studentId)}/work`, data).then(r => r.data),

  // Skills
  getSkills: (studentId: number) =>
    client.get<StudentSkill[]>(`${base(studentId)}/skills`).then(r => r.data),
  replaceSkills: (studentId: number, data: StudentSkillsRequest) =>
    client.put<StudentSkill[]>(`${base(studentId)}/skills`, data).then(r => r.data),
};
