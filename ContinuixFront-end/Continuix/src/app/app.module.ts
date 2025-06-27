import {CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { FormsModule } from '@angular/forms';
import { ReactiveFormsModule } from '@angular/forms';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import { LoginComponent } from './login/login.component';
import { OrganizationChartModule } from 'primeng/organizationchart';
import { AdminDashboardComponent } from './admin-dashboard/admin-dashboard.component';
import { DialogModule } from 'primeng/dialog';
import { TooltipModule } from 'primeng/tooltip';
import { HomeComponent } from './home/home.component';
import{register} from 'swiper/element/bundle';
import { HeaderComponentComponent } from './header-component/header-component.component';
import { HeaderSidebarComponent } from './header-sidebar/header-sidebar.component';
import { ListComponent } from './list/list.component';
import { AuthInterceptor } from './auth.interceptor';
import { NotificationFormComponent } from './notification-form/notification-form.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { NavbarComponent } from './navbar/navbar.component';
import { NotificationHistoryComponent } from './notification-history/notification-history.component';
import { ResetPasswordComponent } from './reset-password/reset-password.component';
import { NotificationConsultComponent } from './notification-consult/notification-consult.component';
import { AlerteComponent } from './alerte/alerte.component'; 
import { PCAComponent } from './pca/pca.component';
import { PcaDialogComponent } from './pca-dialog/pca-dialog.component';
import { RobotPredictorComponent } from './robot-predictor/robot-predictor.component';
import { DashboardComponent } from './dashboard/dashboard.component';
import { MatCardModule } from '@angular/material/card';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatFormFieldModule } from '@angular/material/form-field';
import { AboutSectionComponent } from './dashboard/about-section/about-section.component';

register();
@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    AdminDashboardComponent,
    HomeComponent,
    HeaderComponentComponent,
    HeaderSidebarComponent,
    ListComponent,
    NotificationFormComponent,
    NavbarComponent,
    NotificationHistoryComponent,
    ResetPasswordComponent,
    NotificationConsultComponent,
    PCAComponent,
    AlerteComponent,
    PcaDialogComponent,
    RobotPredictorComponent,
    DashboardComponent,

  AboutSectionComponent

   

    
 
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    ReactiveFormsModule,
    NgbModule ,
    HttpClientModule,
    OrganizationChartModule,
    DialogModule,
    TooltipModule,
    BrowserModule,
    BrowserAnimationsModule,
    MatInputModule,
    MatButtonModule,
    MatIconModule,
    MatFormFieldModule, 
    MatCardModule,
   
    
  
  ],
  providers: [ { provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true }],
  bootstrap: [AppComponent],
  schemas:[CUSTOM_ELEMENTS_SCHEMA],
})
export class AppModule { }
