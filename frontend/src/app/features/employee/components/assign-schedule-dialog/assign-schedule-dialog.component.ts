import { Component, Inject, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MaterialModule } from '../../../../shared/ui/materials-module';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { MAT_DATE_LOCALE } from '@angular/material/core';
import { ScheduleService } from '../../../schedule/services/schedule.service';
import { ScheduleDto, ScheduleEmployeeDto } from '../../../schedule/models/schedule.model';

@Component({
  selector: 'app-assign-schedule-dialog',
  standalone: true,
  imports: [CommonModule, MaterialModule, ReactiveFormsModule],
  providers: [
    { provide: MAT_DATE_LOCALE, useValue: 'es-AR' }
  ],
  templateUrl: './assign-schedule-dialog.component.html',
  styleUrls: ['./assign-schedule-dialog.component.scss']
})
export class AssignScheduleDialogComponent implements OnInit {
  assignForm: FormGroup;

  // ── Signals para el manejo de estado ──
  schedules = signal<ScheduleDto[]>([]);
  loading = signal(false);
  loadingSchedules = signal(true);

  constructor(
    private fb: FormBuilder,
    private scheduleService: ScheduleService,
    private dialogRef: MatDialogRef<AssignScheduleDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: { employeeId: number; employeeName: string }
  ) {
    this.assignForm = this.fb.group({
      scheduleId: ['', Validators.required],
      startDate: ['', Validators.required],
      endDate: [''] // Es opcional según tu lógica
    });
  }

  ngOnInit(): void {
    this.scheduleService.listSchedules().subscribe({
      next: (data) => {
        // Corrección del bug: Evaluación más flexible por si llega como string 'true' o truthy
        const activeSchedules = data.filter(s => s.active === true || String(s.active) === 'true');
        this.schedules.set(activeSchedules);
        this.loadingSchedules.set(false);
      },
      error: (err) => {
        console.error('Error loading schedules', err);
        this.loadingSchedules.set(false);
      }
    });
  }

  onSubmit(): void {
    if (this.assignForm.valid) {
      this.loading.set(true);
      const formValue = this.assignForm.value;

      const assignment: ScheduleEmployeeDto = {
        employeeId: this.data.employeeId,
        scheduleId: formValue.scheduleId,
        startDate: this.formatDate(formValue.startDate),
      };

      if (formValue.endDate) {
        assignment.endDate = this.formatDate(formValue.endDate);
      }

      this.scheduleService.assignSchedule(assignment).subscribe({
        next: (res) => {
          this.loading.set(false);
          this.dialogRef.close(res);
        },
        error: (err) => {
          console.error('Error assigning schedule', err);
          this.loading.set(false);
          alert('Hubo un error al asignar el turno.');
        }
      });
    }
  }

  onCancel(): void {
    this.dialogRef.close();
  }

  private formatDate(date: Date): string {
    if (!date) return '';
    const d = new Date(date);
    let month = '' + (d.getMonth() + 1);
    let day = '' + d.getDate();
    const year = d.getFullYear();

    if (month.length < 2) month = '0' + month;
    if (day.length < 2) day = '0' + day;

    return [year, month, day].join('-');
  }
}