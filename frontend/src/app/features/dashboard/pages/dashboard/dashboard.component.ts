import { Component, inject, ViewChild } from '@angular/core';
import { Router, RouterOutlet } from '@angular/router';
import { Token } from '../../../../core/storage/token';
import { MaterialModule } from '../../../../shared/ui/materials-module';
import { DrawerMenu } from '../../../../shared/components/drawer-menu/drawer-menu';
import { MatDrawer } from '@angular/material/sidenav';
import { ScreenSize } from '../../../../core/services/screen-size';
import { TranslationService } from '../../../../core/services/translation.service';

@Component({
  selector: 'app-dashboard',
  imports: [MaterialModule, RouterOutlet, DrawerMenu],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.scss',
})
export class Dashboard {

  @ViewChild('drawer') drawer!: MatDrawer;
  private readonly screenSizeService = inject(ScreenSize);
  private token = inject(Token);
  private router = inject(Router);
  private translationService = inject(TranslationService);

  t = this.translationService.translate.bind(this.translationService);

  isMobile = this.screenSizeService.isMobile;

  logout() {
    this.token.removeToken();
    this.router.navigate(['/login']);
  }
}
