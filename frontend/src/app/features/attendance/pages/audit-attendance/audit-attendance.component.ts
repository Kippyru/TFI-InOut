import { AfterViewInit, ChangeDetectorRef, Component, OnInit, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MatTableModule, MatTableDataSource } from '@angular/material/table';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatNativeDateModule, MAT_DATE_LOCALE } from '@angular/material/core';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { AttendanceService } from '../../services/attendance.service';
import { DailyAttendanceDto, EventAttendanceDto } from '../../models/attendance.model';
import { AuditEditDialogComponent } from '../../components/audit-edit-dialog/audit-edit-dialog.component';
import { MatSort, MatSortModule } from '@angular/material/sort';

@Component({
  selector: 'app-audit-attendance',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    MatTableModule,
    MatSortModule,
    MatDatepickerModule,
    MatInputModule,
    MatButtonModule,
    MatIconModule,
    MatNativeDateModule,
    MatDialogModule
  ],
  providers: [{ provide: MAT_DATE_LOCALE, useValue: 'es-AR' }],
  templateUrl: './audit-attendance.component.html',
  styleUrls: ['./audit-attendance.component.scss']
})
export class AuditAttendanceComponent implements OnInit, AfterViewInit {
  selectedDate: Date = new Date();
  @ViewChild(MatSort) set matSort(sort: MatSort) {
    if (sort) {
      this.dataSource.sort = sort;
    }
  }
  dataSource = new MatTableDataSource<DailyAttendanceDto>();
  att: EventAttendanceDto[] = [];
  loading = false;
  displayedColumns: string[] = ['numberEmployee', 'employee', 'schedule', 'checkIn', 'checkOut', 'reason', 'date'];

  constructor(
    private attendanceService: AttendanceService,
    private dialog: MatDialog,
    private cdr: ChangeDetectorRef
  ) { }

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
    if (!this.selectedDate) return;
    this.loading = true;
    const dateStr = this.formatDate(this.selectedDate);
    this.attendanceService.getDailyAttendance(dateStr).subscribe({
      next: (data) => {
        this.dataSource.data = data;
        this.loading = false;
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error(err);
        this.loading = false;
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
