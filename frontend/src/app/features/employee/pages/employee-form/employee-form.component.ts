import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { EmployeeService } from '../../services/employee.service';

@Component({
  selector: 'app-employee-form',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    RouterModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatButtonModule,
    MatCardModule,
    MatIconModule,
    MatSnackBarModule
  ],
  templateUrl: './employee-form.component.html',
  styleUrls: ['./employee-form.component.scss']
})
export class EmployeeFormComponent implements OnInit {
  employeeForm: FormGroup;
  isEditMode = false;
  employeeId: string | null = null;
  roles = [
    { id: 1, name: 'Admin' },
    { id: 2, name: 'Employee' }
  ];

  constructor(
    private fb: FormBuilder,
    private employeeService: EmployeeService,
    private route: ActivatedRoute,
    private router: Router,
    private snackBar: MatSnackBar
  ) {
    this.employeeForm = this.fb.group({
      name: ['', Validators.required],
      lastName: ['', Validators.required],
      role: ['', Validators.required],
      numberEmployee: ['', Validators.required],
      cuil: ['', Validators.required],
      dni: ['', Validators.required],
      state: ['']
    });
  }

  ngOnInit(): void {
    this.employeeId = this.route.snapshot.paramMap.get('id');
    if (this.employeeId) {
      this.isEditMode = true;
      this.loadEmployeeData();
    }
  }

  loadEmployeeData(): void {
    this.employeeService.getEmployeeById(this.employeeId!).subscribe({
      next: (data) => {
        // Formatear el rol si viene como string
        const employeeData = { ...data, role: Number(data.role) };
        this.employeeForm.patchValue(employeeData);
      },
      error: (err) => {
        console.error('Error loading employee', err);
        this.snackBar.open('Error al cargar datos del empleado', 'Cerrar', { duration: 3000 });
        this.router.navigate(['/dashboard/employee']); // Asumiendo que la ruta padre es dashboard
      }
    });
  }

  onSubmit(): void {
    if (this.employeeForm.invalid) {
      return;
    }

    const employeeData = this.employeeForm.value;

    if (this.isEditMode) {
      this.employeeService.updateEmployee(this.employeeId!, employeeData).subscribe({
        next: () => {
          this.snackBar.open('Empleado actualizado exitosamente', 'Cerrar', { duration: 3000 });
          this.router.navigate(['/dashboard/employee']);
        },
        error: (err) => {
          console.error('Error updating employee', err);
          this.snackBar.open('Error al actualizar empleado', 'Cerrar', { duration: 3000 });
        }
      });
    } else {
      this.employeeService.createEmployee(employeeData).subscribe({
        next: () => {
          this.snackBar.open('Empleado creado exitosamente', 'Cerrar', { duration: 3000 });
          this.router.navigate(['/dashboard/employee']);
        },
        error: (err) => {
          console.error('Error creating employee', err);
          this.snackBar.open('Error al crear empleado', 'Cerrar', { duration: 3000 });
        }
      });
    }
  }
}
