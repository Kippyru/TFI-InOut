import { ChangeDetectorRef, Component, Inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MaterialModule } from '../../../../shared/ui/materials-module';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { ScheduleService } from '../../services/schedule.service';
import { DetailScheduleDto } from '../../models/schedule.model';

@Component({
  selector: 'app-schedule-detail-dialog',
  standalone: true,
  imports: [CommonModule, MaterialModule, ReactiveFormsModule],
  templateUrl: './schedule-detail-dialog.component.html',
  styleUrls: ['./schedule-detail-dialog.component.scss']
})
export class ScheduleDetailDialogComponent {
  detailForm: FormGroup;
  days = ['Lunes', 'Martes', 'Miércoles', 'Jueves', 'Viernes', 'Sábado', 'Domingo'];
  loading = false;

  constructor(
    private cdr: ChangeDetectorRef,
    private fb: FormBuilder,
    private scheduleService: ScheduleService,
    private dialogRef: MatDialogRef<ScheduleDetailDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: { scheduleId: number; scheduleName: string }
  ) {
    this.detailForm = this.fb.group({
      days: [[], Validators.required],
      checkIn: ['', Validators.required],
      checkOut: ['', Validators.required]
    });
  }

  onSubmit(): void {
    if (this.detailForm.valid) {
      this.loading = true;
      this.cdr.detectChanges();
      const formValue = this.detailForm.value;
      const details: DetailScheduleDto[] = formValue.days.map((day: string) => ({
        scheduleId: this.data.scheduleId,
        day: day,
        checkIn: this.formatTime(formValue.checkIn),
        checkOut: this.formatTime(formValue.checkOut)
      }));

      this.scheduleService.addDetailsBulkToSchedule(this.data.scheduleId, details).subscribe({
        next: (res) => {
          this.loading = false;
          this.dialogRef.close(res);
        },
        error: (err) => {
          console.error('Error adding details in bulk', err);
          this.loading = false;
          alert('Hubo un error al agregar los detalles. Verifique los datos.');
        }
      });
    }
  }

  onCancel(): void {
    this.dialogRef.close();
  }

  // Ensures time string is HH:mm:ss for backend LocalTime if user enters HH:mm
  private formatTime(time: string): string {
    if (time && time.length === 5) {
      return `${time}:00`;
    }
    return time;
  }
}
