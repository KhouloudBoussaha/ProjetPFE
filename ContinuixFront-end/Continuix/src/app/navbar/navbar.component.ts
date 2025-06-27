import { Component, OnInit } from '@angular/core';
import { LoginComponent } from '../login/login.component';
import { AuthService } from '../auth.service';
import { Router } from '@angular/router';
import { Notification } from '../models/notification';
import { NotificationService } from '../notification.service';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent implements OnInit {
  username: string = '';
  role: string = '';
  displaymenu=false;
  displayNotificationManagment=false;
  displayUserManagment=false;
  displayPcaManagment=false;
  displayAlerteManagment=false;
  displaySendNotification = false;
  displayNotificationHistory = false;
  displayConfirmationStatus = false;
  displayConsultNotification = false;
 currentRole: string | null = null;
  notification: Notification | null = null;
  isNotificationMenuOpen = false;
  isUserMenuOpen = false;
isPcaMenuOpen = false;
isAlertMenuOpen = false;
  constructor(private authService: AuthService , public router: Router ,private notificationService: NotificationService){}
  ngOnInit(): void {
    
  const username = localStorage.getItem('username');
  this.username = username ?? 'Utilisateur';
  const role = localStorage.getItem('role');
    this.role = role ?? 'role';

    this.MenuDisplay();
    
}
MenuDisplay():void {
  
    const token = this.authService.getJwt();

    if (token) {
       this.currentRole = this.authService.getRole();
  console.log("Role actuel:", this.currentRole);
  this.displayUserManagment = this.currentRole === 'Admin';
  this.displayPcaManagment = this.currentRole === 'SECURITYAGENT';
  this.displayAlerteManagment = this.currentRole === 'SECURITYAGENT' || this.currentRole === 'MANAGER' || this.currentRole === 'TEAMLEADER'|| this.currentRole === 'COLLABORATOR';

  // Onglet Notification Managment (général)
  this.displayNotificationManagment = 
    this.currentRole === 'Admin' 
   

  // Sous-onglets Notification Management
  this.displaySendNotification = this.currentRole === 'Admin';
  this.displayNotificationHistory = this.currentRole === 'Admin';
  this.displayConfirmationStatus = this.currentRole === 'Admin';
  this.displayConsultNotification = 
    this.currentRole === 'SECURITYAGENT' || 
    this.currentRole === 'TEAMLEADER' || 
     this.currentRole === 'MANAGER' || 
    this.currentRole === 'COLLABORATOR';
}
}

logout(): void {
  this.authService.logout();
  this.router.navigate(['/login']);
}

isActive(paths: string[]): boolean {
  return paths.some(p => this.router.url.includes(p));
}
}
