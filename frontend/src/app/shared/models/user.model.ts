export interface User {
  id?: number | string;
  username: string;
  password?: string;
  role: number;
  active?: boolean;
}
