import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NotificationHistoryComponent } from './notification-history.component';

describe('NotificationHistoryComponent', () => {
  let component: NotificationHistoryComponent;
  let fixture: ComponentFixture<NotificationHistoryComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [NotificationHistoryComponent]
    });
    fixture = TestBed.createComponent(NotificationHistoryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
