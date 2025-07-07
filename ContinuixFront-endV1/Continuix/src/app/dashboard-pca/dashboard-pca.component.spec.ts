import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DashboardPCAComponent } from './dashboard-pca.component';

describe('DashboardPCAComponent', () => {
  let component: DashboardPCAComponent;
  let fixture: ComponentFixture<DashboardPCAComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [DashboardPCAComponent]
    });
    fixture = TestBed.createComponent(DashboardPCAComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
