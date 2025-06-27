import { Component, NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { AdminDashboardComponent } from './admin-dashboard/admin-dashboard.component';
import { SecurityDashboardComponent } from './security-dashboard/security-dashboard.component';
import { HTTP_INTERCEPTORS } from '@angular/common/http';
import { AuthInterceptor } from './auth.interceptor';
import { HomeComponent } from './home/home.component';
import { HeaderComponentComponent } from './header-component/header-component.component';
import { HeaderSidebarComponent } from './header-sidebar/header-sidebar.component';
import { ListComponent } from './list/list.component';
import { NotificationFormComponent } from './notification-form/notification-form.component';
import { NotificationHistoryComponent } from './notification-history/notification-history.component';
import { ResetPasswordComponent } from './reset-password/reset-password.component';
import { NavbarComponent } from './navbar/navbar.component';
import { NotificationConsultComponent } from './notification-consult/notification-consult.component';
import { AlerteComponent } from './alerte/alerte.component';
import { PCAComponent } from './pca/pca.component';
import { authGuard } from './auth.guard';
import { RobotPredictorComponent } from './robot-predictor/robot-predictor.component';
import { DashboardComponent } from './dashboard/dashboard.component';
import { AboutSectionComponent } from './dashboard/about-section/about-section.component';

const routes: Routes = [
  { path: '', redirectTo: '/home', pathMatch: 'full' },
  { path: 'home',component: HomeComponent},
  { path: 'login', component: LoginComponent},
  { path: 'dashboard', component: AdminDashboardComponent },
  { path: 'security/dashboard', component: SecurityDashboardComponent},
  { path: 'header',component:HeaderComponentComponent},
  { path: 'headerSidebar',component:HeaderSidebarComponent},
  { path: 'list',component:ListComponent},
  {path:'SendNotification',component:NotificationFormComponent},
  {path:'reset-password',component:ResetPasswordComponent},
  {path:'navbar',component:NavbarComponent},
 { path: 'notificationConsult/:id/view', component: NotificationConsultComponent, canActivate: [authGuard] },
 {path: 'notificationConsult/:id', component: NotificationConsultComponent, canActivate: [authGuard]},
  {path:'PCA',component:PCAComponent},
  {path:'alerte',component:AlerteComponent},
  {path:'robot',component:RobotPredictorComponent},
  {path:'dashboardCommun',component:DashboardComponent},
{ path: 'a-propos', component: AboutSectionComponent },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  providers: [
   { provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true }
  ],
  exports: [RouterModule]
})
export class AppRoutingModule { }
