import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IMySQLConnection, MySQLConnection } from 'app/shared/model/my-sql-connection.model';
import { MySQLConnectionService } from './my-sql-connection.service';
import { MySQLConnectionComponent } from './my-sql-connection.component';
import { MySQLConnectionDetailComponent } from './my-sql-connection-detail.component';
import { MySQLConnectionUpdateComponent } from './my-sql-connection-update.component';

@Injectable({ providedIn: 'root' })
export class MySQLConnectionResolve implements Resolve<IMySQLConnection> {
  constructor(private service: MySQLConnectionService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IMySQLConnection> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((mySQLConnection: HttpResponse<MySQLConnection>) => {
          if (mySQLConnection.body) {
            return of(mySQLConnection.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new MySQLConnection());
  }
}

export const mySQLConnectionRoute: Routes = [
  {
    path: '',
    component: MySQLConnectionComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'jbeebrApp.mySQLConnection.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: MySQLConnectionDetailComponent,
    resolve: {
      mySQLConnection: MySQLConnectionResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'jbeebrApp.mySQLConnection.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: MySQLConnectionUpdateComponent,
    resolve: {
      mySQLConnection: MySQLConnectionResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'jbeebrApp.mySQLConnection.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: MySQLConnectionUpdateComponent,
    resolve: {
      mySQLConnection: MySQLConnectionResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'jbeebrApp.mySQLConnection.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];
