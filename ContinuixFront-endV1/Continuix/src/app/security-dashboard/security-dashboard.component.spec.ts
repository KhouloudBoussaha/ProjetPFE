import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SecurityDashboardComponent } from './security-dashboard.component';

describe('SecurityDashboardComponent', () => {
  let component: SecurityDashboardComponent;
  let fixture: ComponentFixture<SecurityDashboardComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [SecurityDashboardComponent]
    });
    fixture = TestBed.createComponent(SecurityDashboardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
