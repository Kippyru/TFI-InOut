export interface Employee {
  id?: number | string;
  name: string;
  lastName: string;
  role: string | number; // 1 = Admin, 2 = Employee
  numberEmployee: string;
  cuil: string;
  dni: string;
  state?: string; // Para notas/comentarios
  dateEntry?: string | Date;
  active?: number; // 1 = active, 0 = inactive (soft delete)
}
