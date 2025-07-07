import { TestBed } from '@angular/core/testing';

import { TicketPcaService } from './ticket-pca.service';

describe('TicketPcaService', () => {
  let service: TicketPcaService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TicketPcaService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
