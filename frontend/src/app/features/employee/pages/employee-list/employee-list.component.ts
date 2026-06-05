import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { EmployeeService } from '../../services/employee.service';
import { Employee } from '../../models/employee.model';

@Component({
  selector: 'app-employee-list',
  standalone: true,
  imports: [CommonModule, RouterModule, MatTableModule, MatButtonModule, MatIconModule, MatSnackBarModule],
  templateUrl: './employee-list.component.html',
  styleUrls: ['./employee-list.component.scss']
})
export class EmployeeListComponent implements OnInit {
  employees: Employee[] = [];
  displayedColumns: string[] = ['id', 'name', 'lastName', 'role', 'numberEmployee', 'active', 'actions'];

  constructor(
    private employeeService: EmployeeService,
    private snackBar: MatSnackBar,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.loadEmployees();
  }

  loadEmployees(): void {
    this.employeeService.getEmployees().subscribe({
      next: (data) => {
        this.employees = data;
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('Error loading employees', err);
        this.snackBar.open('Error al cargar empleados', 'Cerrar', { duration: 3000 });
      }
    });
  }

  deleteEmployee(id: string | number): void {
    if (confirm('¿Estás seguro de eliminar este empleado?')) {
      this.employeeService.deleteEmployee(id).subscribe({
        next: () => {
          this.snackBar.open('Empleado eliminado', 'Cerrar', { duration: 3000 });
          this.loadEmployees();
        },
        error: (err) => {
          console.error('Error deleting employee', err);
          this.snackBar.open('Error al eliminar empleado', 'Cerrar', { duration: 3000 });
        }
      });
    }
  }

  restoreEmployee(id: string | number): void {
    if (confirm('¿Estás seguro de restaurar este empleado?')) {
      this.employeeService.restoreEmployee(id).subscribe({
        next: () => {
          this.snackBar.open('Empleado restaurado', 'Cerrar', { duration: 3000 });
          this.loadEmployees();
        },
        error: (err) => {
          console.error('Error restoring employee', err);
          this.snackBar.open('Error al restaurar empleado', 'Cerrar', { duration: 3000 });
        }
      });
    }
  }

  getRoleName(roleId: string | number): string {
    return roleId == 1 ? 'Admin' : roleId == 2 ? 'Employee' : 'Unknown';
  }
}
