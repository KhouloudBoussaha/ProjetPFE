import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RobotPredictorComponent } from './robot-predictor.component';

describe('RobotPredictorComponent', () => {
  let component: RobotPredictorComponent;
  let fixture: ComponentFixture<RobotPredictorComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [RobotPredictorComponent]
    });
    fixture = TestBed.createComponent(RobotPredictorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
