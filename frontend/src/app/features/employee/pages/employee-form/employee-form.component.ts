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
import { UserService } from '../../../../core/services/user.service';
import { forkJoin } from 'rxjs';

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

  userId: string | number | null = null;

  constructor(
    private fb: FormBuilder,
    private employeeService: EmployeeService,
    private userService: UserService,
    private route: ActivatedRoute,
    private router: Router,
    private snackBar: MatSnackBar
  ) {
    this.employeeForm = this.fb.group({
      name: ['', Validators.required],
      lastName: ['', Validators.required],
      role: ['', Validators.required],
      numberEmployee: [{ value: '', disabled: true }],
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

        this.userId = data.user ? data.user : null;

        const employeeData = { ...data };
        this.employeeForm.patchValue(employeeData);

      },
      error: (err) => {
        console.error('Error loading employee', err);
        this.snackBar.open('Error al cargar datos del empleado', 'Cerrar', { duration: 3000 });
        this.router.navigate(['/dashboard/employee']); //la ruta padre es dashboard
      }
    });
  }

  onSubmit(): void {
    if (this.employeeForm.invalid) {
      return;
    }

    const formData = this.employeeForm.getRawValue();

    const userData: any = {
      role: formData.role
    };

    //el legajo generado, es el username
    if (formData.numberEmployee) {
      userData.username = formData.numberEmployee;
    }

    const employeeData = {
      name: formData.name,
      lastName: formData.lastName,
      cuil: formData.cuil,
      dni: formData.dni,
      state: formData.state,
      active: true
    };

    if (this.isEditMode) {
      const updateRequests = [];
      updateRequests.push(this.employeeService.updateEmployee(this.employeeId!, employeeData));

      if (this.userId) {
        updateRequests.push(this.userService.updateUser(this.userId, userData));
      }

      forkJoin(updateRequests).subscribe({
        next: () => {
          this.snackBar.open('Empleado actualizado exitosamente', 'Cerrar', { duration: 3000 });
          this.router.navigate(['/dashboard/employee']);
        },
        error: (err) => {
          console.error('Error updating employee and user', err);
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
