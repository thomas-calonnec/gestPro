import { TestBed } from '@angular/core/testing';

import { ListCardService } from './list-card.service';

describe('ListCardService', () => {
  let service: ListCardService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ListCardService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
