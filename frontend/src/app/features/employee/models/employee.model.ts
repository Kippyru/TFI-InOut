export interface Employee {
  id?: number | string;
  name: string;
  lastName: string;
  numberEmployee?: string;
  cuil: string;
  dni: string;
  state?: string; 
  dateEntry?: string | Date;
  active?: boolean; 
  user?: number | string; 
}
