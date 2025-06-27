import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-reset-password',
  templateUrl: './reset-password.component.html',
  styleUrls: ['./reset-password.component.css']
})
export class ResetPasswordComponent  implements OnInit{
  resetForm: FormGroup;
  token: string | null = null;
  errorMessage: string | null = null;
  successMessage: string | null = null;
  showPassword: boolean = false; // Pour toggler l'affichage du mot de passe
  isSubmitting: boolean = false; // Pour l'indicateur de chargement

  constructor(
    private route: ActivatedRoute,
    private http: HttpClient,
    private fb: FormBuilder,
     private router: Router
  ) {
    this.resetForm = this.fb.group({
      newPassword: ['', [Validators.required, Validators.minLength(8), Validators.pattern(/^(?=.*[a-zA-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,}$/)]],
      confirmPassword: ['', [Validators.required]],
      
    }, { validators: this.passwordMatchValidator });
  }

  // Validateur pour vérifier que les mots de passe correspondent
  passwordMatchValidator(form: FormGroup) {
    return form.get('newPassword')?.value === form.get('confirmPassword')?.value
      ? null : { mismatch: true };
  }

  ngOnInit(): void {
    this.token = this.route.snapshot.queryParamMap.get('token');
    if (!this.token) {
      this.errorMessage = 'Invalid or missing token. Please check the link.';
    }
  }

  togglePasswordVisibility(): void {
    this.showPassword = !this.showPassword;
  }

  onSubmit(): void {
  console.log('🚀 Submit triggered');
  console.log('✅ Form valid?', this.resetForm.valid);
  console.log('✅ Token?', this.token);
  console.log('✅ isSubmitting?', this.isSubmitting);

    if (this.resetForm.valid && this.token) {
      this.isSubmitting = true;
      const resetRequest = {
        token: this.token,
        
        newPassword: this.resetForm.value.newPassword
      };
      console.log('🔐 Token envoyé :', this.token);
      console.log('📦 Payload envoyé :', resetRequest);
      this.http.post('http://localhost:8075/api/users/reset-password', resetRequest)
        .subscribe({
         next: (response: any) => {
  console.log('✅ Réponse du backend:', response);
  this.successMessage = response?.message || 'Mot de passe réinitialisé avec succès.';
  this.errorMessage = null;
  this.isSubmitting = false;
  this.router.navigate(['/login']); // ou laisse-le temporairement si pas encore dispo
},

        });
    } else if (!this.token) {
      this.errorMessage = 'Invalid or missing token. Please check the link.';
    }
  }

  // Calculer la force du mot de passe pour l'indicateur
  getPasswordStrength(): number {
    const password = this.resetForm.get('newPassword')?.value || '';
    let strength = 0;
    if (password.length >= 8) strength++;
    if (/[A-Za-z]/.test(password)) strength++;
    if (/\d/.test(password)) strength++;
    if (/[@$!%*?&]/.test(password)) strength++;
    return strength;
  }
}
