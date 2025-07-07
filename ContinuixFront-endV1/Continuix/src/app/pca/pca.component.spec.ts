import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PCAComponent } from './pca.component';

describe('PCAComponent', () => {
  let component: PCAComponent;
  let fixture: ComponentFixture<PCAComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PCAComponent]
    });
    fixture = TestBed.createComponent(PCAComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
