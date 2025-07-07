import { Component, OnInit } from '@angular/core';
import * as d3 from 'd3';
import { AiService } from '../ai.service';
import { PCA } from '../models/PCA';
import { AiRequestDTO } from '../ai-request-dto';
@Component({
  selector: 'app-robot-predictor',
  templateUrl: './robot-predictor.component.html',
  styleUrls: ['./robot-predictor.component.css']
})
export class RobotPredictorComponent {

  featureImpact: string = '';
  incidentType: string = '';
  prediction: string | null = null;
  isLoading: boolean = false;
  predictionResult: PCA | null = null;
  
constructor(private aiService: AiService) {}
  ngOnInit(): void {
    this.initRobotAndDataFlow();
  }

  initRobotAndDataFlow() {
    // Clear previous SVG to avoid duplication
    d3.select('#data-flow').selectAll('svg').remove();
    
    const svg = d3.select('#data-flow')
      .append('svg')
      .attr('width', 300)
      .attr('height', 200);

    // Robot SVG (head and body)
    svg.append('circle') // Head
      .attr('cx', 150)
      .attr('cy', 50)
      .attr('r', 20)
      .attr('fill', '#4CAF50')
      .attr('class', 'robot-head');

    svg.append('rect') // Body
      .attr('x', 130)
      .attr('y', 70)
      .attr('width', 40)
      .attr('height', 60)
      .attr('rx', 5)
      .attr('fill', '#4CAF50');

    svg.append('circle') // Left eye
      .attr('cx', 140)
      .attr('cy', 45)
      .attr('r', 3)
      .attr('fill', '#FFF');

    svg.append('circle') // Right eye
      .attr('cx', 160)
      .attr('cy', 45)
      .attr('r', 3)
      .attr('fill', '#FFF');

    // Data flow particles
    const particles = d3.range(10).map(() => ({
      x: Math.random() * 300,
      y: Math.random() * 200
    }));

    svg.selectAll('.particle')
      .data(particles)
      .enter()
      .append('circle')
      .attr('class', 'particle')
      .attr('cx', d => d.x)
      .attr('cy', d => d.y)
      .attr('r', 2)
      .attr('fill', '#FFFFFF')
      .transition()
      .duration(1500)
      .attr('cx', 150)
      .attr('cy', 50) // Converge to head
      .ease(d3.easeLinear)
      .on('end', () => {
        this.initRobotAndDataFlow(); // Restart animation
      });
  }

 predict() {
  if (!this.featureImpact || !this.incidentType) {
    alert('Veuillez remplir tous les champs.');
    return;
  }

  this.isLoading = true;

  const dto: AiRequestDTO = {
    impact_encoded: this.encodeImpact(this.featureImpact),
    incident_type_encoded: this.encodeIncidentType(this.incidentType)
  };

  this.aiService.predictPca(dto).subscribe({
    next: (result: PCA) => {
      this.predictionResult = result;
      this.prediction = `Prédiction PCA: ${JSON.stringify(result)}`;
      this.animateRobot();
      this.isLoading = false;
    },
    error: () => {
      alert('Erreur de prédiction PCA.');
      this.isLoading = false;
    }
  });
}
private encodeIncidentType(type: string): number {
  switch (type.toUpperCase()) {
    case 'AUTHENTICATION_ISSUE': return 1;
    case 'NETWORK_ISSUE': return 2;
    case 'SYSTEM_ERROR': return 3;
    case 'USER_REQUEST': return 4;
    case 'NATURAL_INCIDENT': return 5;
    case 'DEVELOPMENT_ENVIRONMENT': return 6;
    case 'OTHER_IT_INCIDENT': return 7;
    case 'UNKNOWN': return 8;
    default: return 0;
  }
}

private encodeImpact(impact: string): number {
  switch (impact.toUpperCase()) {
    case 'LOW': return 1;
    case 'MEDIUM': return 2;
    case 'HIGH': return 3;
    default: return 0;
  }
}

private animateRobot() {
  d3.selectAll('.robot-head')
    .transition()
    .duration(300)
    .attr('fill', '#FF9800')
    .transition()
    .duration(300)
    .attr('fill', '#4CAF50');
}

  
}
