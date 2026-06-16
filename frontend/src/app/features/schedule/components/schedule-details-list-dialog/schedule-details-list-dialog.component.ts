import { ChangeDetectorRef, Component, Inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MaterialModule } from '../../../../shared/ui/materials-module';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { ScheduleService } from '../../services/schedule.service';
import { DetailScheduleDto } from '../../models/schedule.model';

@Component({
  selector: 'app-schedule-details-list-dialog',
  standalone: true,
  imports: [CommonModule, MaterialModule, ReactiveFormsModule],
  templateUrl: './schedule-details-list-dialog.component.html',
  styleUrls: ['./schedule-details-list-dialog.component.scss']
})
export class ScheduleDetailsListDialogComponent implements OnInit {
  details: DetailScheduleDto[] = [];
  displayedColumns: string[] = ['day', 'checkIn', 'checkOut', 'actions'];
  loading = true;
  editingId: number | null = null;
  editForm: FormGroup;

  constructor(
    private scheduleService: ScheduleService,
    private cdr: ChangeDetectorRef,
    private fb: FormBuilder,
    private dialogRef: MatDialogRef<ScheduleDetailsListDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: { scheduleId: number; scheduleName: string }
  ) {
    this.editForm = this.fb.group({
      checkIn: ['', Validators.required],
      checkOut: ['', Validators.required]
    });
  }

  ngOnInit(): void {
    this.loadDetails();
  }

  loadDetails(): void {
    this.loading = true;
    this.scheduleService.getDetailsBySchedule(this.data.scheduleId).subscribe({
      next: (data) => {
        this.details = data;
        this.loading = false;
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('Error loading details', err);
        this.loading = false;
        this.cdr.detectChanges();
      }
    });
  }

  deleteDetail(id: number | undefined): void {
    if (!id) return;
    if (confirm('¿Desea eliminar este día del turno?')) {
      this.scheduleService.deleteDetail(id).subscribe({
        next: () => this.loadDetails(),
        error: (err) => console.error('Error deleting detail', err)
      });
    }
  }

  startEdit(detail: DetailScheduleDto): void {
    if (detail.id) {
      this.editingId = detail.id;
      this.editForm.patchValue({
        checkIn: detail.checkIn.substring(0, 5),
        checkOut: detail.checkOut.substring(0, 5)
      });
    }
  }

  cancelEdit(): void {
    this.editingId = null;
  }

  saveEdit(detail: DetailScheduleDto): void {
    if (this.editForm.valid && detail.id) {
      const updatedDetail: DetailScheduleDto = {
        ...detail,
        checkIn: this.formatTime(this.editForm.value.checkIn),
        checkOut: this.formatTime(this.editForm.value.checkOut)
      };
      
      this.scheduleService.updateDetail(detail.id, updatedDetail).subscribe({
        next: () => {
          this.editingId = null;
          this.loadDetails();
        },
        error: (err) => {
          console.error('Error updating detail', err);
          alert('Error al actualizar el detalle');
        }
      });
    }
  }

  private formatTime(time: string): string {
    if (time && time.length === 5) {
      return `${time}:00`;
    }
    return time;
  }

  onClose(): void {
    this.dialogRef.close();
  }
}
