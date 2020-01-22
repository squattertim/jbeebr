import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { JbeebrTestModule } from '../../../test.module';
import { MySQLConnectionDetailComponent } from 'app/entities/my-sql-connection/my-sql-connection-detail.component';
import { MySQLConnection } from 'app/shared/model/my-sql-connection.model';

describe('Component Tests', () => {
  describe('MySQLConnection Management Detail Component', () => {
    let comp: MySQLConnectionDetailComponent;
    let fixture: ComponentFixture<MySQLConnectionDetailComponent>;
    const route = ({ data: of({ mySQLConnection: new MySQLConnection('123') }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [JbeebrTestModule],
        declarations: [MySQLConnectionDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(MySQLConnectionDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(MySQLConnectionDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load mySQLConnection on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.mySQLConnection).toEqual(jasmine.objectContaining({ id: '123' }));
      });
    });
  });
});
