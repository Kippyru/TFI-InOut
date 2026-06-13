import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { MaterialModule } from '../../ui/materials-module';
import { UserService } from '../../../core/services/user.service';

@Component({
  selector: 'app-drawer-menu',
  standalone: true,
  imports: [CommonModule, RouterModule, MaterialModule],
  templateUrl: './drawer-menu.html',
  styleUrl: './drawer-menu.css',
})
export class DrawerMenu implements OnInit {
  isAdmin = false;

  constructor(private userService: UserService, private cdr: ChangeDetectorRef) { }

  ngOnInit() {
    this.userService.getMe().subscribe({
      next: (user) => {
        this.isAdmin = user.role === 1;
        this.cdr.detectChanges();
      }
    });
  }

}
