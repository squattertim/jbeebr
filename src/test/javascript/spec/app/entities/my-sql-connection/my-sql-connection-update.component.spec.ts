import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { JbeebrTestModule } from '../../../test.module';
import { MySQLConnectionUpdateComponent } from 'app/entities/my-sql-connection/my-sql-connection-update.component';
import { MySQLConnectionService } from 'app/entities/my-sql-connection/my-sql-connection.service';
import { MySQLConnection } from 'app/shared/model/my-sql-connection.model';

describe('Component Tests', () => {
  describe('MySQLConnection Management Update Component', () => {
    let comp: MySQLConnectionUpdateComponent;
    let fixture: ComponentFixture<MySQLConnectionUpdateComponent>;
    let service: MySQLConnectionService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [JbeebrTestModule],
        declarations: [MySQLConnectionUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(MySQLConnectionUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(MySQLConnectionUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(MySQLConnectionService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new MySQLConnection('123');
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new MySQLConnection();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
