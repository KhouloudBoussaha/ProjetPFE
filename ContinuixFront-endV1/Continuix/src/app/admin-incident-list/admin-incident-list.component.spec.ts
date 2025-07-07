import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminIncidentListComponent } from './admin-incident-list.component';

describe('AdminIncidentListComponent', () => {
  let component: AdminIncidentListComponent;
  let fixture: ComponentFixture<AdminIncidentListComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [AdminIncidentListComponent]
    });
    fixture = TestBed.createComponent(AdminIncidentListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
