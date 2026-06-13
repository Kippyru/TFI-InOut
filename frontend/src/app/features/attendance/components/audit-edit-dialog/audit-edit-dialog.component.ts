import { Component, Inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef, MatDialogModule } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatSnackBar } from '@angular/material/snack-bar';
import { AttendanceService } from '../../services/attendance.service';
import { UserService } from '../../../../core/services/user.service';

@Component({
  selector: 'app-audit-edit-dialog',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    MatDialogModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule
  ],
  templateUrl: './audit-edit-dialog.component.html',
  styleUrls: ['./audit-edit-dialog.component.scss']
})
export class AuditEditDialogComponent {
  newValue: string = '';
  reason: string = '';
  loading = false;

  constructor(
    public dialogRef: MatDialogRef<AuditEditDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: { eventId: number; currentValue: string; eventType: string },
    private attendanceService: AttendanceService,
    private userService: UserService,
    private snackBar: MatSnackBar
  ) {
    // Current value might be HH:mm:ss, but HTML input type="time" uses HH:mm.
    // If we want seconds, step="1" in html allows it, but often HH:mm is sufficient.
    // The backend requires 00:00:00 format. We'll ensure it when saving.
    this.newValue = data.currentValue.substring(0, 5); // "HH:mm"
  }

  onCancel(): void {
    this.dialogRef.close(false);
  }

  onSave(): void {
    if (!this.newValue || !this.reason) return;

    // The backend expects HH:mm:ss
    const formattedTime = this.newValue.length === 5 ? `${this.newValue}:00` : this.newValue;

    this.loading = true;

    // We need adminId. Assuming userService.getCurrentUser() or similar exists.
    this.userService.getMe().subscribe({
      next: (admin) => {
        const adminId = admin.id; // User id of the admin

        this.attendanceService.auditAttendance(adminId, this.data.eventId, this.reason, formattedTime).subscribe({
          next: () => {
            this.snackBar.open('Horario editado correctamente', 'Cerrar', { duration: 3000 });
            this.loading = false;
            this.dialogRef.close(true);
          },
          error: (err) => {
            console.error(err);
            this.snackBar.open('Error al auditar', 'Cerrar', { duration: 3000 });
            this.loading = false;
          }
        });
      },
      error: (err) => {
        console.error('Could not get admin user', err);
        this.snackBar.open('Error de autenticación del administrador', 'Cerrar', { duration: 3000 });
        this.loading = false;
      }
    });
  }
}
