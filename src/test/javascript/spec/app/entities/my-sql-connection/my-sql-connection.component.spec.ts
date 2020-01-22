import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { JbeebrTestModule } from '../../../test.module';
import { MySQLConnectionComponent } from 'app/entities/my-sql-connection/my-sql-connection.component';
import { MySQLConnectionService } from 'app/entities/my-sql-connection/my-sql-connection.service';
import { MySQLConnection } from 'app/shared/model/my-sql-connection.model';

describe('Component Tests', () => {
  describe('MySQLConnection Management Component', () => {
    let comp: MySQLConnectionComponent;
    let fixture: ComponentFixture<MySQLConnectionComponent>;
    let service: MySQLConnectionService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [JbeebrTestModule],
        declarations: [MySQLConnectionComponent],
        providers: []
      })
        .overrideTemplate(MySQLConnectionComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(MySQLConnectionComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(MySQLConnectionService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new MySQLConnection('123')],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.mySQLConnections && comp.mySQLConnections[0]).toEqual(jasmine.objectContaining({ id: '123' }));
    });
  });
});
