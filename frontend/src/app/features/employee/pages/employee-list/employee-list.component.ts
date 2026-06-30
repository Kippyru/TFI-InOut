import { Component, OnInit, ChangeDetectorRef, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { MatSnackBar } from '@angular/material/snack-bar';
import { FormsModule } from '@angular/forms';
import { EmployeeService } from '../../services/employee.service';
import { UserService } from '../../../../core/services/user.service';
import { ScheduleService } from '../../../schedule/services/schedule.service';
import { TranslationService } from '../../../../core/services/translation.service';
import { Employee } from '../../models/employee.model';
import { forkJoin, of } from 'rxjs';
import { catchError, map } from 'rxjs/operators';
import { MaterialModule } from '../../../../shared/ui/materials-module';

interface EmployeeWithSchedule extends Employee {
  scheduleName?: string;
}

@Component({
  selector: 'app-employee-list',
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule, MaterialModule],
  templateUrl: './employee-list.component.html',
  styleUrls: ['./employee-list.component.scss']
})
export class EmployeeListComponent implements OnInit {
  employees = signal<EmployeeWithSchedule[]>([]);
  filteredEmployees = signal<EmployeeWithSchedule[]>([]);
  filterStatus = signal<string>('todos');
  searchQuery = signal<string>('');
  
  displayedColumns: string[] = ['id', 'name', 'lastName', 'numberEmployee', 'scheduleName', 'active', 'actions'];
  sortField = signal<string>('');
  sortDirection = signal<'asc' | 'desc'>('asc');

  private employeeService = inject(EmployeeService);
  private userService = inject(UserService);
  private scheduleService = inject(ScheduleService);
  private snackBar = inject(MatSnackBar);
  private translationService = inject(TranslationService);
  private cdr = inject(ChangeDetectorRef);

  t = this.translationService.translate.bind(this.translationService);

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
          this.employees.set([]);
          this.applyFilter();
          return;
        }

        forkJoin(requests).subscribe({
          next: (enriched) => {
            this.employees.set(enriched);
            this.applyFilter();
          }
        });
      },
      error: (err) => {
        console.error('Error loading employees', err);
        this.snackBar.open(this.translationService.translate('employee.list.errorLoad'), this.translationService.translate('snackbar.close'), { duration: 3000 });
      }
    });
  }

  applyFilter(): void {
    let result = [...this.employees()];

    // Status filter
    if (this.filterStatus() === 'activos') {
      result = result.filter(e => e.active === true || String(e.active) === 'true');
    } else if (this.filterStatus() === 'inactivos') {
      result = result.filter(e => e.active === false || String(e.active) === 'false');
    }

    // Text search
    const q = this.searchQuery().trim().toLowerCase();
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
    if (this.sortField()) {
      result.sort((a, b) => {
        const aVal = (a as any)[this.sortField()] ?? '';
        const bVal = (b as any)[this.sortField()] ?? '';
        const cmp = String(aVal).localeCompare(String(bVal), undefined, { numeric: true });
        return this.sortDirection() === 'asc' ? cmp : -cmp;
      });
    }

    this.filteredEmployees.set(result);
    this.cdr.detectChanges();
  }

  onSort(field: string): void {
    if (this.sortField() === field) {
      this.sortDirection.set(this.sortDirection() === 'asc' ? 'desc' : 'asc');
    } else {
      this.sortField.set(field);
      this.sortDirection.set('asc');
    }
    this.applyFilter();
  }

  getSortIcon(field: string): string {
    if (this.sortField() !== field) return 'unfold_more';
    return this.sortDirection() === 'asc' ? 'arrow_upward' : 'arrow_downward';
  }

  deleteEmployee(employee: Employee): void {
    if (confirm(this.translationService.translate('employee.list.confirmDelete'))) {
      const requests = [this.employeeService.deleteEmployee(employee.id!)];
      if (employee.user) {
        requests.push(this.userService.deleteUser(employee.user));
      }

      forkJoin(requests).subscribe({
        next: () => {
          this.snackBar.open(this.translationService.translate('employee.list.deleted'), this.translationService.translate('snackbar.close'), { duration: 3000 });
          this.loadEmployees();
        },
        error: (err) => {
          console.error('Error deleting employee', err);
          this.snackBar.open(this.translationService.translate('employee.list.errorDelete'), this.translationService.translate('snackbar.close'), { duration: 3000 });
        }
      });
    }
  }

  restoreEmployee(employee: Employee): void {
    if (confirm(this.translationService.translate('employee.list.confirmRestore'))) {
      const requests = [this.employeeService.restoreEmployee(employee.id!)];
      if (employee.user) {
        requests.push(this.userService.restoreUser(employee.user));
      }

      forkJoin(requests).subscribe({
        next: () => {
          this.snackBar.open(this.translationService.translate('employee.list.restored'), this.translationService.translate('snackbar.close'), { duration: 3000 });
          this.loadEmployees();
        },
        error: (err) => {
          console.error('Error restoring employee', err);
          this.snackBar.open(this.translationService.translate('employee.list.errorRestore'), this.translationService.translate('snackbar.close'), { duration: 3000 });
        }
      });
    }
  }
}
