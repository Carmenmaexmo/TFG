import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ViniloDetalleComponent } from './vinilo-detalle.component';

describe('ViniloDetalleComponent', () => {
  let component: ViniloDetalleComponent;
  let fixture: ComponentFixture<ViniloDetalleComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ViniloDetalleComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ViniloDetalleComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
