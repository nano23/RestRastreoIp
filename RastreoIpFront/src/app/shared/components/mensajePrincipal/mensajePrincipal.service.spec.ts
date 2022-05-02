/* tslint:disable:no-unused-variable */

import { TestBed, inject, waitForAsync } from '@angular/core/testing';
import { MensajePrincipalService } from './mensajePrincipal.service';

describe('Service: MensajePrincipal', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [MensajePrincipalService]
    });
  });

  it('should ...', inject([MensajePrincipalService], (service: MensajePrincipalService) => {
    expect(service).toBeTruthy();
  }));
});
