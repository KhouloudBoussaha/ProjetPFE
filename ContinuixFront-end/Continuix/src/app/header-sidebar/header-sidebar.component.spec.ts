import { ComponentFixture, TestBed } from '@angular/core/testing';

import { HeaderSidebarComponent } from './header-sidebar.component';

describe('HeaderSidebarComponent', () => {
  let component: HeaderSidebarComponent;
  let fixture: ComponentFixture<HeaderSidebarComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [HeaderSidebarComponent]
    });
    fixture = TestBed.createComponent(HeaderSidebarComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
