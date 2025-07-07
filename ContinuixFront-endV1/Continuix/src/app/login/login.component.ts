import { Component } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { LoginResponse } from '../models/auth.model';
import { AuthService } from '../auth.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  loginForm: FormGroup;
  loading = false;
  errorMessage: string | null = null;

  constructor(
    private authService: AuthService,
    private router: Router,
    private route: ActivatedRoute,
    private fb: FormBuilder
  ) {
    this.loginForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', Validators.required]
    });
  }

  onSubmit(): void {
    if (this.loginForm.invalid) {
      this.loginForm.markAllAsTouched();
      return;
    }

    this.authService.clearAuthData();
    this.loading = true;
    this.errorMessage = null;

    const credentials = this.loginForm.value;
    this.authService.login(credentials).subscribe({
      next: (response: LoginResponse) => {
        this.loading = false;

        // 🔁 Gérer la redirection dynamique
        const redirectUrl = this.route.snapshot.queryParamMap.get('redirect');

        if (redirectUrl) {
          // S'assurer que redirectUrl est valide et remplacer :id si nécessaire
          // Supposons que notificationId est passé dans l'URL de redirection ou comme paramètre de requête
          const notificationId = this.route.snapshot.queryParamMap.get('notificationId') || 'default-id'; // Ajustez selon la manière dont notificationId est passé
          const resolvedUrl = redirectUrl.replace(':id', notificationId);
          this.router.navigateByUrl(resolvedUrl);
        } else {
          // ✅ Revenir à la redirection basée sur le rôle
          switch (response.role) {
            case 'Admin':
              this.router.navigate(['/DashboardPCA']);
              break;
            case 'SECURITYAGENT':
              this.router.navigate(['/dashboardCommun']);
              break;
            case 'MANAGER':
              this.router.navigate(['/dashboardCommun']);
              break;
            case 'COLLABORATOR':
              this.router.navigate(['/dashboardCommun']);
              break;
            default:
              this.router.navigate(['/']);
          }
        }
      },
      error: (error) => {
        this.loading = false;
        this.errorMessage = error.message || 'Une erreur est survenue lors de la connexion.';
      }
    });
  }
}