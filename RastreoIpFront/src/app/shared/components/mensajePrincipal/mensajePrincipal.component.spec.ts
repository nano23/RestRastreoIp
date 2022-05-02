import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MensajePrincipalComponent } from './mensajePrincipal.component';

describe('MensajePrincipalComponent', () => {
  let component: MensajePrincipalComponent;
  let fixture: ComponentFixture<MensajePrincipalComponent>;

  beforeEach(async() => {
    TestBed.configureTestingModule({
      declarations: [ MensajePrincipalComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(MensajePrincipalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

