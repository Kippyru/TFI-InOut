import { Component } from '@angular/core';
import { RouterModule } from '@angular/router';
import { MaterialModule } from '../../ui/materials-module';

@Component({
  selector: 'app-drawer-menu',
  standalone: true,
  imports: [RouterModule, MaterialModule],
  templateUrl: './drawer-menu.html',
  styleUrl: './drawer-menu.css',
})
export class DrawerMenu {}
