import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { MatButtonToggleModule } from '@angular/material/button-toggle';
import { FormsModule } from '@angular/forms';
import { EmployeeService } from '../../services/employee.service';
import { UserService } from '../../../../core/services/user.service';
import { Employee } from '../../models/employee.model';
import { forkJoin } from 'rxjs';

@Component({
  selector: 'app-employee-list',
  standalone: true,
  imports: [CommonModule, RouterModule, MatTableModule, MatButtonModule, MatIconModule, MatSnackBarModule, MatButtonToggleModule, FormsModule],
  templateUrl: './employee-list.component.html',
  styleUrls: ['./employee-list.component.scss']
})
export class EmployeeListComponent implements OnInit {
  employees: Employee[] = [];
  filteredEmployees: Employee[] = [];
  filterStatus: string = 'todos'; // 'todos', 'activos', 'inactivos'
  displayedColumns: string[] = ['id', 'name', 'lastName', 'numberEmployee', 'active', 'actions'];

  constructor(
    private employeeService: EmployeeService,
    private userService: UserService,
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
        this.applyFilter(); 
      },
      error: (err) => {
        console.error('Error loading employees', err);
        this.snackBar.open('Error al cargar empleados', 'Cerrar', { duration: 3000 });
      }
    });
  }

  applyFilter(): void {
    if (this.filterStatus === 'activos') {
      this.filteredEmployees = this.employees.filter(e => e.active === true || String(e.active) === 'true');
    } else if (this.filterStatus === 'inactivos') {
      this.filteredEmployees = this.employees.filter(e => e.active === false || String(e.active) === 'false');
    } else {
      this.filteredEmployees = [...this.employees];
    }
    this.cdr.detectChanges();
  }

  deleteEmployee(employee: Employee): void {
    if (confirm('¿Estás seguro de dar de baja este empleado?')) {
      const requests = [this.employeeService.deleteEmployee(employee.id!)];
      if (employee.user) {
        requests.push(this.userService.deleteUser(employee.user));
      }
      
      forkJoin(requests).subscribe({
        next: () => {
          this.snackBar.open('Empleado y Usuario dados de baja', 'Cerrar', { duration: 3000 });
          this.loadEmployees();
        },
        error: (err) => {
          console.error('Error deleting employee', err);
          this.snackBar.open('Error al dar de baja empleado', 'Cerrar', { duration: 3000 });
        }
      });
    }
  }

  restoreEmployee(employee: Employee): void {
    if (confirm('¿Estás seguro de restaurar este empleado?')) {
      const requests = [this.employeeService.restoreEmployee(employee.id!)];
      if (employee.user) {
        requests.push(this.userService.restoreUser(employee.user));
      }

      forkJoin(requests).subscribe({
        next: () => {
          this.snackBar.open('Empleado y Usuario restaurados', 'Cerrar', { duration: 3000 });
          this.loadEmployees();
        },
        error: (err) => {
          console.error('Error restoring employee', err);
          this.snackBar.open('Error al restaurar empleado', 'Cerrar', { duration: 3000 });
        }
      });
    }
  }

}
