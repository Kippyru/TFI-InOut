import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { MaterialModule } from '../../ui/materials-module';
import { Token } from '../../../core/storage/token';


@Component({
  selector: 'app-drawer-menu',
  standalone: true,
  imports: [CommonModule, RouterModule, MaterialModule],
  templateUrl: './drawer-menu.html',
  styleUrl: './drawer-menu.css',
})
export class DrawerMenu implements OnInit {
  isAdmin = false;

  constructor(private token: Token) { }

  ngOnInit() {
    this.isAdmin = this.token.getRole() === 1;
  }
}
