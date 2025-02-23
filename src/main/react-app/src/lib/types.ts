export interface User {
  id: string;
  email: string;
  role: 'admin' | 'student';
  name: string;
  createdAt: string;
  updatedAt: string;
}

export interface Student {
  id: string;
  userId: string;
  studentId: string;
  firstName: string;
  lastName: string;
  email: string;
  phone: string;
  gpa: number;
  major: string;
  graduationYear: number;
  skills: Skill[];
  education: Education[];
  workExperience: WorkExperience[];
  certifications: Certification[];
}

export interface Skill {
  id: string;
  name: string;
  proficiency: 'beginner' | 'intermediate' | 'advanced';
}

export interface Education {
  id: string;
  institution: string;
  degree: string;
  field: string;
  startDate: string;
  endDate: string;
  gpa: number;
}

export interface WorkExperience {
  id: string;
  company: string;
  position: string;
  startDate: string;
  endDate: string | null;
  description: string;
}

export interface Certification {
  id: string;
  name: string;
  issuingOrganization: string;
  issueDate: string;
  expiryDate: string | null;
  credentialId: string;
}

export interface Job {
  id: string;
  title: string;
  company: string;
  location: string;
  type: 'full-time' | 'part-time' | 'internship';
  description: string;
  requirements: string[];
  salary: {
    min: number;
    max: number;
    currency: string;
  };
  status: 'open' | 'closed';
  createdAt: string;
  updatedAt: string;
}

export interface ApiResponse<T> {
  data: T;
  message: string;
  success: boolean;
} 