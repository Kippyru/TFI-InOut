import { NgModule } from "@angular/core";
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatToolbarModule } from '@angular/material/toolbar';
import {MatSidenavModule} from '@angular/material/sidenav';
import {MatTooltipModule} from '@angular/material/tooltip';

@NgModule({
    exports: [MatToolbarModule, MatButtonModule, MatIconModule, MatSidenavModule, MatTooltipModule],
})
export class MaterialModule { }