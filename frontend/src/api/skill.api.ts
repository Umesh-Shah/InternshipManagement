import client from './client';

export interface Skill {
  skillId: number;
  skillName: string;
  skillType: string | null;
}

export type SkillRequest = Omit<Skill, 'skillId'>;

export const skillApi = {
  getAll: () => client.get<Skill[]>('/skills').then(r => r.data),
  getById: (id: number) => client.get<Skill>(`/skills/${id}`).then(r => r.data),
  create: (data: SkillRequest) => client.post<Skill>('/skills', data).then(r => r.data),
  update: (id: number, data: SkillRequest) => client.put<Skill>(`/skills/${id}`, data).then(r => r.data),
  delete: (id: number) => client.delete(`/skills/${id}`),
};
