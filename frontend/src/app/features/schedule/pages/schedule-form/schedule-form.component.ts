import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { MaterialModule } from '../../../../shared/ui/materials-module';
import { ScheduleService } from '../../services/schedule.service';
import { ScheduleDto } from '../../models/schedule.model';

@Component({
  selector: 'app-schedule-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule, MaterialModule],
  templateUrl: './schedule-form.component.html',
  styleUrls: ['./schedule-form.component.scss']
})
export class ScheduleFormComponent implements OnInit {
  scheduleForm: FormGroup;
  isEditMode = false;
  scheduleId: number | null = null;
  loading = false;
  loadingData = false;

  constructor(
    private fb: FormBuilder,
    private scheduleService: ScheduleService,
    private router: Router,
    private cdr: ChangeDetectorRef,
    private route: ActivatedRoute
  ) {
    this.scheduleForm = this.fb.group({
      name: ['', Validators.required],
      hourWork: [0, [Validators.required, Validators.min(1)]],
      checkInTolerance: [0, [Validators.required, Validators.min(0)]],
      checkOutTolerance: [0, [Validators.required, Validators.min(0)]],
      active: [1]
    });
  }

  ngOnInit(): void {
    const idParam = this.route.snapshot.paramMap.get('id');
    if (idParam) {
      this.isEditMode = true;
      this.scheduleId = +idParam;
      this.loadSchedule(this.scheduleId);
      this.cdr.detectChanges();
    }
  }

  loadSchedule(id: number): void {
    this.loadingData = true;
    this.scheduleService.getScheduleById(id).subscribe({
      next: (data) => {
        this.scheduleForm.patchValue(data);
        this.loadingData = false;
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('Error loading schedule', err);
        this.loadingData = false;
      }
    });
  }

  onSubmit(): void {
    if (this.scheduleForm.valid) {
      this.loading = true;
      const scheduleData: ScheduleDto = this.scheduleForm.value;

      if (this.isEditMode && this.scheduleId) {
        scheduleData.id = this.scheduleId;
        this.scheduleService.updateSchedule(this.scheduleId, scheduleData).subscribe({
          next: () => {
            this.loading = false;
            this.router.navigate(['/dashboard/schedule']);
          },
          error: (err) => {
            console.error('Error updating schedule', err);
            this.loading = false;
            alert('Error al actualizar el turno');
          }
        });
      } else {
        this.scheduleService.createSchedule(scheduleData).subscribe({
          next: () => {
            this.loading = false;
            this.router.navigate(['/dashboard/schedule']);
          },
          error: (err) => {
            console.error('Error creating schedule', err);
            this.loading = false;
            alert('Error al crear el turno');
          }
        });
      }
    }
  }
}
