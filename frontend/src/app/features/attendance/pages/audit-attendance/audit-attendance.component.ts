import { AfterViewInit, ChangeDetectorRef, Component, OnInit, ViewChild, inject, signal } from '@angular/core';
import { TranslationService } from '../../../../core/services/translation.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MatTableModule, MatTableDataSource } from '@angular/material/table';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatSelectModule } from '@angular/material/select';
import { MatIconModule } from '@angular/material/icon';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatNativeDateModule, MAT_DATE_LOCALE } from '@angular/material/core';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { AttendanceService } from '../../services/attendance.service';
import { DailyAttendanceDto, EventAttendanceDto } from '../../models/attendance.model';
import { AuditEditDialogComponent } from '../../components/audit-edit-dialog/audit-edit-dialog.component';
import { MatSort, MatSortModule } from '@angular/material/sort';
import { MatPaginator, MatPaginatorModule } from '@angular/material/paginator';

@Component({
  selector: 'app-audit-attendance',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    MatTableModule,
    MatSortModule,
    MatPaginatorModule,
    MatSelectModule,
    MatDatepickerModule,
    MatInputModule,
    MatButtonModule,
    MatIconModule,
    MatNativeDateModule,
    MatDialogModule,
    MatTooltipModule
  ],
  providers: [{ provide: MAT_DATE_LOCALE, useValue: 'es-AR' }],
  templateUrl: './audit-attendance.component.html',
  styleUrls: ['./audit-attendance.component.scss']
})
export class AuditAttendanceComponent implements OnInit, AfterViewInit {
  selectedDate = signal<Date>(new Date());
  
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
  dataSource = new MatTableDataSource<DailyAttendanceDto>();
  pageSizeOptions = [5, 10, 20, 50];
  att: EventAttendanceDto[] = [];
  loading = signal<boolean>(false);
  displayedColumns: string[] = ['numberEmployee', 'employee', 'schedule', 'checkIn', 'checkOut', 'reason', 'date'];

  private attendanceService = inject(AttendanceService);
  private dialog = inject(MatDialog);
  private cdr = inject(ChangeDetectorRef);
  private translationService = inject(TranslationService);
  t = this.translationService.translate.bind(this.translationService);

  ngOnInit(): void {
    this.loadAttendances();
  }
  ngAfterViewInit(): void {

    this.dataSource.sortingDataAccessor = (item: DailyAttendanceDto, property: string) => {
      switch (property) {
        case 'employee':
          return `${item.employeeName} ${item.employeeLastName}`.toLowerCase();
        case 'checkIn':
          return item.checkInTime || '';
        case 'checkOut':
          return item.checkOutTime || '';
        case 'numberEmployee':
          return item.numberEmployee ?? 0;
        case 'schedule':
          return item.scheduleName?.toLowerCase() ?? '';
        case 'reason':
          return item.reason?.toLowerCase() ?? '';
        case 'date':
          return item.date ? new Date(item.date).getTime() : 0;
        default:
          return (item as any)[property];
      }
    };
  }

  onDateChange(): void {
    this.loadAttendances();
  }

  loadAttendances(): void {
    if (!this.selectedDate()) return;
    this.loading.set(true);
    const dateStr = this.formatDate(this.selectedDate());
    this.attendanceService.getDailyAttendance(dateStr).subscribe({
      next: (data) => {
        this.dataSource.data = data;
        this.loading.set(false);
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error(err);
        this.loading.set(false);
      }
    });
  }

  editCheckIn(att: DailyAttendanceDto): void {
    if (!att.checkInEventId) return;
    this.openEditDialog(att.checkInEventId, att.checkInTime || '00:00:00', 'Entrada');
  }

  editCheckOut(att: DailyAttendanceDto): void {
    if (!att.checkOutEventId) return;
    this.openEditDialog(att.checkOutEventId, att.checkOutTime || '00:00:00', 'Salida');
  }

  onPageSizeChange(size: number): void {
    if (this.dataSource.paginator) {
      this.dataSource.paginator.pageSize = size;
      this.dataSource.paginator.firstPage();
    }
  }

  private openEditDialog(eventId: number, currentValue: string, eventType: string): void {
    const dialogRef = this.dialog.open(AuditEditDialogComponent, {
      width: '400px',
      data: { eventId, currentValue, eventType }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.loadAttendances();
      }
    });
  }

  private formatDate(date: Date): string {
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');
    return `${year}-${month}-${day}`;
  }
}
