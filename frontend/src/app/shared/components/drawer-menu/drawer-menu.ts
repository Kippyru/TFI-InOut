import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { MaterialModule } from '../../ui/materials-module';
import { Token } from '../../../core/storage/token';
import { TranslationService } from '../../../core/services/translation.service';


@Component({
  selector: 'app-drawer-menu',
  standalone: true,
  imports: [CommonModule, RouterModule, MaterialModule],
  templateUrl: './drawer-menu.html',
  styleUrl: './drawer-menu.css',
})
export class DrawerMenu implements OnInit {
  isAdmin = false;
  t!: (key: string) => string;

  constructor(private token: Token, private translationService: TranslationService) {
    this.t = this.translationService.translate.bind(this.translationService);
  }

  ngOnInit() {
    this.isAdmin = this.token.getRole() === 1;
  }
}
