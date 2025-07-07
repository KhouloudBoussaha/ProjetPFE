import { Component, NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { AdminDashboardComponent } from './admin-dashboard/admin-dashboard.component';
import { SecurityDashboardComponent } from './security-dashboard/security-dashboard.component';
import { HTTP_INTERCEPTORS } from '@angular/common/http';
import { AuthInterceptor } from './auth.interceptor';
import { HomeComponent } from './home/home.component';
import { HeaderComponentComponent } from './header-component/header-component.component';
import { ListComponent } from './list/list.component';
import { ResetPasswordComponent } from './reset-password/reset-password.component';
import { NotificationConsultComponent } from './notification-consult/notification-consult.component';
import { AlerteComponent } from './alerte/alerte.component';
import { PCAComponent } from './pca/pca.component';
import { authGuard } from './auth.guard';
import { RobotPredictorComponent } from './robot-predictor/robot-predictor.component';
import { DashboardComponent } from './dashboard/dashboard.component';
import { AboutSectionComponent } from './dashboard/about-section/about-section.component';
import { GroupManagementComponent } from './group-management/group-management.component';
import { DashboardPCAComponent } from './dashboard-pca/dashboard-pca.component';
import { SidebarComponent } from './sidebar/sidebar.component';
import { AdminIncidentListComponent } from './admin-incident-list/admin-incident-list.component';
import { SimulatePcaComponent } from './simulate-pca/simulate-pca.component';

const routes: Routes = [
  { path: '', redirectTo: '/home', pathMatch: 'full' },
  { path: 'home',component: HomeComponent},
  { path: 'login', component: LoginComponent},
  { path: 'dashboard', component: AdminDashboardComponent },
  { path: 'security/dashboard', component: SecurityDashboardComponent},
  { path: 'header',component:HeaderComponentComponent},
  { path: 'list',component:ListComponent},
  {path:'reset-password',component:ResetPasswordComponent},
  {path:'Sidebar',component:SidebarComponent},
  { path: 'notificationConsult/:id/view', component: NotificationConsultComponent, canActivate: [authGuard] },
 {path: 'notificationConsult/:id', component: NotificationConsultComponent, canActivate: [authGuard]},
  {path:'PCA',component:PCAComponent},
  {path:'alerte',component:AlerteComponent},
  {path:'robot',component:RobotPredictorComponent},
  {path:'dashboardCommun',component:DashboardComponent},
  {path: 'a-propos', component: AboutSectionComponent },
  {path:'Group',component:GroupManagementComponent},
  {path:'DashboardPCA',component:DashboardPCAComponent},
  {path:'incidentlist',component:AdminIncidentListComponent},
  {path:'simulation',component:SimulatePcaComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  providers: [
   { provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true }
  ],
  exports: [RouterModule]
})
export class AppRoutingModule { }
