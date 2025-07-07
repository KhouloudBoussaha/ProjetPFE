import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NotificationConsultComponent } from './notification-consult.component';

describe('NotificationConsultComponent', () => {
  let component: NotificationConsultComponent;
  let fixture: ComponentFixture<NotificationConsultComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [NotificationConsultComponent]
    });
    fixture = TestBed.createComponent(NotificationConsultComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
