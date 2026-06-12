import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { MaterialModule } from '../../../../shared/ui/materials-module';
import { ScheduleService } from '../../services/schedule.service';
import { ScheduleDto } from '../../models/schedule.model';
import { MatDialog } from '@angular/material/dialog';
import { ScheduleDetailDialogComponent } from '../../components/schedule-detail-dialog/schedule-detail-dialog.component';
import { ScheduleDetailsListDialogComponent } from '../../components/schedule-details-list-dialog/schedule-details-list-dialog.component';

@Component({
  selector: 'app-schedule-list',
  standalone: true,
  imports: [CommonModule, RouterModule, MaterialModule],
  templateUrl: './schedule-list.component.html',
  styleUrls: ['./schedule-list.component.scss']
})
export class ScheduleListComponent implements OnInit {
  schedules: ScheduleDto[] = [];
  displayedColumns: string[] = ['id', 'name', 'hourWork', 'tolerances', 'state', 'actions'];
  loading = true;

  constructor(
    private scheduleService: ScheduleService,
    private dialog: MatDialog,
    private cdr: ChangeDetectorRef
  ) { }

  ngOnInit(): void {
    this.loadSchedules();
    this.cdr.detectChanges();
  }

  loadSchedules(): void {
    this.loading = true;
    this.scheduleService.listSchedules().subscribe({
      next: (data) => {
        this.schedules = data;
        this.loading = false;
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('Error loading schedules', err);
        this.loading = false;
      }
    });
  }

  deleteSchedule(id: number | undefined): void {
    if (!id) return;
    if (confirm('¿Estás seguro de que deseas eliminar este turno?')) {
      this.scheduleService.deleteSchedule(id).subscribe({
        next: () => this.loadSchedules(),
        error: (err) => console.error('Error deleting schedule', err)
      });
    }
  }

  restoreSchedule(id: number | undefined): void {
    if (!id) return;
    if (confirm('¿Estás seguro de que deseas restaurar este turno?')) {
      this.scheduleService.restoreSchedule(id).subscribe({
        next: () => this.loadSchedules(),
        error: (err) => console.error('Error restoring schedule', err)
      });
    }
  }

  addDetail(schedule: ScheduleDto): void {
    const dialogRef = this.dialog.open(ScheduleDetailDialogComponent, {
      width: '400px',
      data: { scheduleId: schedule.id, scheduleName: schedule.name }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        // Just reload or do nothing if details are not shown in list
        console.log('Detail added successfully');
      }
    });
  }

  viewDetails(schedule: ScheduleDto): void {
    this.dialog.open(ScheduleDetailsListDialogComponent, {
      width: '600px',
      data: { scheduleId: schedule.id, scheduleName: schedule.name }
    });
  }
}
