import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TraceoComponent } from './traceo.component';

describe('TraceoComponent', () => {
  let component: TraceoComponent;
  let fixture: ComponentFixture<TraceoComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ TraceoComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(TraceoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
