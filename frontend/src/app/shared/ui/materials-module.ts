import { NgModule } from "@angular/core";
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatTableModule } from '@angular/material/table';
import { MatDialogModule } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';
import { MatListModule } from '@angular/material/list';
import { MatProgressSpinnerModule, MatSpinner } from '@angular/material/progress-spinner';
import { MatCardModule } from '@angular/material/card';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { MatButtonToggleModule } from "@angular/material/button-toggle";
import { MatSlideToggleModule } from '@angular/material/slide-toggle';
import { MatDividerModule } from '@angular/material/divider';
import { MatRippleModule } from '@angular/material/core';

@NgModule({
    exports: [
        MatToolbarModule,
        MatButtonModule,
        MatIconModule,
        MatSidenavModule,
        MatTooltipModule,
        MatTableModule,
        MatDialogModule,
        MatFormFieldModule,
        MatInputModule,
        MatSelectModule,
        MatDatepickerModule,
        MatNativeDateModule,
        MatListModule,
        MatProgressSpinnerModule,
        MatCardModule,
        MatSnackBarModule,
        MatButtonToggleModule,
        MatSlideToggleModule,
        MatDividerModule,
        MatRippleModule,
    ],
})
export class MaterialModule { }
