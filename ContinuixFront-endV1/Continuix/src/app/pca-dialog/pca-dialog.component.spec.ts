import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PcaDialogComponent } from './pca-dialog.component';

describe('PcaDialogComponent', () => {
  let component: PcaDialogComponent;
  let fixture: ComponentFixture<PcaDialogComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PcaDialogComponent]
    });
    fixture = TestBed.createComponent(PcaDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
