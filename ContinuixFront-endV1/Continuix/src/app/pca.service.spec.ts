import { TestBed } from '@angular/core/testing';

import { PcaService } from './pca.service';

describe('PcaService', () => {
  let service: PcaService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PcaService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
