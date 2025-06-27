import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NotificationFormComponent } from './notification-form.component';

describe('NotificationFormComponent', () => {
  let component: NotificationFormComponent;
  let fixture: ComponentFixture<NotificationFormComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [NotificationFormComponent]
    });
    fixture = TestBed.createComponent(NotificationFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
