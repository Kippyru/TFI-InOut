import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { MatDialog } from '@angular/material/dialog';
import { EmployeeService } from '../../services/employee.service';
import { Employee } from '../../models/employee.model';
import { AssignScheduleDialogComponent } from '../../components/assign-schedule-dialog/assign-schedule-dialog.component';
import { MaterialModule } from '../../../../shared/ui/materials-module';

@Component({
  selector: 'app-employee-detail',
  standalone: true,
  imports: [CommonModule, RouterModule, MaterialModule],
  templateUrl: './employee-detail.component.html',
  styleUrls: ['./employee-detail.component.scss']
})
export class EmployeeDetailComponent implements OnInit {
  employee: Employee | null = null;
  loading = true;
  error = false;

  constructor(
    private route: ActivatedRoute,
    private employeeService: EmployeeService,
    private cdr: ChangeDetectorRef,
    private dialog: MatDialog
  ) { }

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.loadEmployee(id);
    } else {
      this.loading = false;
      this.error = true;
    }
  }

  loadEmployee(id: string): void {
    this.employeeService.getEmployeeById(id).subscribe({
      next: (data) => {
        this.employee = data;
        this.loading = false;
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('Error loading employee', err);
        this.error = true;
        this.loading = false;
      }
    });
  }

  getRoleName(roleId: string | number | undefined): string {
    return roleId == 1 ? 'Admin' : roleId == 2 ? 'Employee' : 'Desconocido';
  }

  openAssignScheduleDialog(): void {
    if (this.employee && this.employee.id) {
      const dialogRef = this.dialog.open(AssignScheduleDialogComponent, {
        width: '450px',
        data: {
          employeeId: this.employee.id,
          employeeName: `${this.employee.name} ${this.employee.lastName}`
        }
      });

      dialogRef.afterClosed().subscribe(result => {
        if (result) {
          alert('Turno asignado correctamente al empleado.');
        }
      });
    }
  }
}
