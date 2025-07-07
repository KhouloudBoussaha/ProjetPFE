import { Component } from '@angular/core';
import {  Router,NavigationEnd } from '@angular/router';


@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'Continuix';
  name:string = '';
  email:string = '';
  message:string = ''
showSidebar = true;
constructor(private router: Router) {
    this.router.events.subscribe(event => {
      if (event instanceof NavigationEnd) {
       console.log('📦 État du localStorage :', {
        accessToken: localStorage.getItem('accessToken'),
        role: localStorage.getItem('role'),
        username: localStorage.getItem('username'),});
        const hideOnRoutes = ['/login', '/reset-password','/home','/contact']

        // Vérifie si l'url courante est dans la liste
        this.showSidebar = !hideOnRoutes.some(route => event.urlAfterRedirects.startsWith(route));
      }
    });
  }
}
