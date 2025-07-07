import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SimulatePcaComponent } from './simulate-pca.component';

describe('SimulatePcaComponent', () => {
  let component: SimulatePcaComponent;
  let fixture: ComponentFixture<SimulatePcaComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [SimulatePcaComponent]
    });
    fixture = TestBed.createComponent(SimulatePcaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
