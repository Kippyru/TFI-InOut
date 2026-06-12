import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { MatButtonToggleModule } from '@angular/material/button-toggle';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSortModule, Sort } from '@angular/material/sort';
import { MatTooltipModule } from '@angular/material/tooltip';
import { FormsModule } from '@angular/forms';
import { EmployeeService } from '../../services/employee.service';
import { UserService } from '../../../../core/services/user.service';
import { ScheduleService } from '../../../schedule/services/schedule.service';
import { Employee } from '../../models/employee.model';
import { forkJoin, of } from 'rxjs';
import { catchError, map } from 'rxjs/operators';

interface EmployeeWithSchedule extends Employee {
  scheduleName?: string;
}

@Component({
  selector: 'app-employee-list',
  standalone: true,
  imports: [
    CommonModule, RouterModule, MatTableModule, MatButtonModule, MatIconModule,
    MatSnackBarModule, MatButtonToggleModule, FormsModule,
    MatInputModule, MatFormFieldModule, MatSortModule, MatTooltipModule
  ],
  templateUrl: './employee-list.component.html',
  styleUrls: ['./employee-list.component.scss']
})
export class EmployeeListComponent implements OnInit {
  employees: EmployeeWithSchedule[] = [];
  filteredEmployees: EmployeeWithSchedule[] = [];
  filterStatus: string = 'todos';
  searchQuery: string = '';
  displayedColumns: string[] = ['id', 'name', 'lastName', 'numberEmployee', 'scheduleName', 'active', 'actions'];

  sortField: string = '';
  sortDirection: 'asc' | 'desc' = 'asc';

  constructor(
    private employeeService: EmployeeService,
    private userService: UserService,
    private scheduleService: ScheduleService,
    private snackBar: MatSnackBar,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.loadEmployees();
  }

  loadEmployees(): void {
    this.employeeService.getEmployees().subscribe({
      next: (data) => {
        const requests = data.map(emp =>
          this.scheduleService.getActiveScheduleForEmployee(emp.id!).pipe(
            map(se => ({ ...emp, scheduleName: se?.scheduleName ?? '—' } as EmployeeWithSchedule)),
            catchError(() => of({ ...emp, scheduleName: '—' } as EmployeeWithSchedule))
          )
        );

        if (requests.length === 0) {
          this.employees = [];
          this.applyFilter();
          return;
        }

        forkJoin(requests).subscribe({
          next: (enriched) => {
            this.employees = enriched;
            this.applyFilter();
          }
        });
      },
      error: (err) => {
        console.error('Error loading employees', err);
        this.snackBar.open('Error al cargar empleados', 'Cerrar', { duration: 3000 });
      }
    });
  }

  applyFilter(): void {
    let result = [...this.employees];

    // Status filter
    if (this.filterStatus === 'activos') {
      result = result.filter(e => e.active === true || String(e.active) === 'true');
    } else if (this.filterStatus === 'inactivos') {
      result = result.filter(e => e.active === false || String(e.active) === 'false');
    }

    // Text search
    const q = this.searchQuery.trim().toLowerCase();
    if (q) {
      result = result.filter(e =>
        String(e.id ?? '').toLowerCase().includes(q) ||
        (e.name ?? '').toLowerCase().includes(q) ||
        (e.lastName ?? '').toLowerCase().includes(q) ||
        (e.numberEmployee ?? '').toLowerCase().includes(q) ||
        (e.cuil ?? '').toLowerCase().includes(q) ||
        (e.dni ?? '').toLowerCase().includes(q) ||
        (e.scheduleName ?? '').toLowerCase().includes(q)
      );
    }

    // Sorting
    if (this.sortField) {
      result.sort((a, b) => {
        const aVal = (a as any)[this.sortField] ?? '';
        const bVal = (b as any)[this.sortField] ?? '';
        const cmp = String(aVal).localeCompare(String(bVal), undefined, { numeric: true });
        return this.sortDirection === 'asc' ? cmp : -cmp;
      });
    }

    this.filteredEmployees = result;
    this.cdr.detectChanges();
  }

  onSort(field: string): void {
    if (this.sortField === field) {
      this.sortDirection = this.sortDirection === 'asc' ? 'desc' : 'asc';
    } else {
      this.sortField = field;
      this.sortDirection = 'asc';
    }
    this.applyFilter();
  }

  getSortIcon(field: string): string {
    if (this.sortField !== field) return 'unfold_more';
    return this.sortDirection === 'asc' ? 'arrow_upward' : 'arrow_downward';
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
