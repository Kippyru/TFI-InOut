import { Component, inject, ViewChild } from '@angular/core';
import { ViewEncapsulation } from '@angular/core';
import { Router, RouterOutlet } from '@angular/router';
import { Token } from '../../core/storage/token';
import { MaterialModule } from '../../shared/ui/materials-module';
import { DrawerMenu } from '../../shared/components/drawer-menu/drawer-menu';
import { MatDrawer } from '@angular/material/sidenav';
import { ScreenSize } from '../../core/services/screen-size';

@Component({
  selector: 'app-login',
  imports: [MaterialModule, RouterOutlet, DrawerMenu],
  templateUrl: './dashboard.html',
  styleUrl: '../dashboard/dashboard.scss',
})
export class Dashboard {

  @ViewChild('drawer') drawer!: MatDrawer;
  private readonly screenSizeService = inject(ScreenSize);

  isMobile = this.screenSizeService.isMobile;

  constructor(
    private token: Token,
    private router: Router
  ) { }

  logout() {
    this.token.removeToken();
    localStorage.clear();
    this.router.navigate(['/login']);
  }
}