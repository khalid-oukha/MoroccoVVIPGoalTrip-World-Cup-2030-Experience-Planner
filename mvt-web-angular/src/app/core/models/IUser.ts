export interface IUser {
  id: string;
  email: string;
  firstName: string;
  lastName: string;
  createdAt?: string;
  created_at?: string;
  authorities?: string[];
}
