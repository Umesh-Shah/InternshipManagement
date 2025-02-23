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