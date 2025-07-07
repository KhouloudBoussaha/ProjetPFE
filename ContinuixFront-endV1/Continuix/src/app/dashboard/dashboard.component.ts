import { Component } from '@angular/core';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent {
  showWelcome = true;
  showCompanyInfo = false;

  companyDescriptionFr = `
    Sopra HR Software est une filiale du groupe Sopra Steria, spécialisée dans les solutions numériques 
    de gestion des ressources humaines. Présente dans plus de 50 pays et accompagnant près de 950 clients, 
    elle propose des outils innovants pour la paie, la gestion des talents, l'administration RH et le suivi du temps. 
    Grâce à son approche éthique et à l’intégration de l’intelligence artificielle, Sopra HR aide les entreprises 
    de toutes tailles à moderniser leur gestion RH et à renforcer leur performance.
  `;

  companyDescriptionEn = `
    Sopra HR Software is a subsidiary of the Sopra Steria Group, dedicated to digital human resources management solutions. 
    Operating in over 50 countries and serving nearly 950 clients, the company provides innovative tools for payroll, 
    talent management, HR administration, and time tracking. Through its ethical approach and integration of artificial intelligence, 
    Sopra HR enables organizations of all sizes to modernize their HR systems and enhance overall efficiency.
  `;

  onAlertClick() {
    this.showCompanyInfo = true;
    // Optionally navigate to alert component: this.router.navigate(['/alerts']);
  }
}
