import { AfterViewInit, ChangeDetectorRef, Component, OnInit, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { MaterialModule } from '../../../../shared/ui/materials-module';
import { ScheduleService } from '../../services/schedule.service';
import { ScheduleDto } from '../../models/schedule.model';
import { MatDialog } from '@angular/material/dialog';
import { ScheduleDetailDialogComponent } from '../../components/schedule-detail-dialog/schedule-detail-dialog.component';
import { ScheduleDetailsListDialogComponent } from '../../components/schedule-details-list-dialog/schedule-details-list-dialog.component';
import { MatSort, MatSortModule } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { MatPaginator } from '@angular/material/paginator';
import { TranslationService } from '../../../../core/services/translation.service';

@Component({
  selector: 'app-schedule-list',
  standalone: true,
  imports: [CommonModule, RouterModule, MaterialModule, MatSortModule],
  templateUrl: './schedule-list.component.html',
  styleUrls: ['./schedule-list.component.scss']
})
export class ScheduleListComponent implements OnInit, AfterViewInit {
  @ViewChild(MatSort) set matSort(sort: MatSort) {
    if (sort) {
      this.dataSource.sort = sort;
    }
  }
  @ViewChild(MatPaginator) set matPaginator(paginator: MatPaginator) {
    if (paginator) {
      this.dataSource.paginator = paginator;
    }
  }
  dataSource = new MatTableDataSource<ScheduleDto>();
  displayedColumns: string[] = ['id', 'name', 'hourWork', 'tolerances', 'active', 'actions'];
  loading = true;
  pageSizeOptions = [5, 10, 20, 50];
  t!: (key: string) => string;

  constructor(
    private scheduleService: ScheduleService,
    private dialog: MatDialog,
    private cdr: ChangeDetectorRef,
    private translationService: TranslationService
  ) {
    this.t = this.translationService.translate.bind(this.translationService);
  }

  ngOnInit(): void {
    this.loadSchedules();
  }

  ngAfterViewInit(): void {

    this.dataSource.sortingDataAccessor = (item: ScheduleDto, property: string) => {
      switch (property) {
        case 'id':
          return item.id || '';
        case 'name':
          return `${item.name}`.toLowerCase();
        case 'hourWork':
          return item.hourWork || '';
        case 'tolerances':
          return item.checkInTolerance || '';
        default:
          return (item as any)[property];
      }
    };
  }

  onDateChange(): void {
    this.loadSchedules();
  }

  onPageSizeChange(size: number): void {
    if (this.dataSource.paginator) {
      this.dataSource.paginator.pageSize = size;
      this.dataSource.paginator.firstPage();
    }
  }

  loadSchedules(): void {
    this.loading = true;
    this.scheduleService.listSchedules().subscribe({
      next: (data) => {
        this.dataSource.data = data;
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
    if (confirm(this.translationService.translate('schedule.list.confirmDelete'))) {
      this.scheduleService.deleteSchedule(id).subscribe({
        next: () => this.loadSchedules(),
        error: (err) => console.error('Error deleting schedule', err)
      });
    }
  }

  restoreSchedule(id: number | undefined): void {
    if (!id) return;
    if (confirm(this.translationService.translate('schedule.list.confirmRestore'))) {
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
