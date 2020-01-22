import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IMySQLConnection } from 'app/shared/model/my-sql-connection.model';

type EntityResponseType = HttpResponse<IMySQLConnection>;
type EntityArrayResponseType = HttpResponse<IMySQLConnection[]>;

@Injectable({ providedIn: 'root' })
export class MySQLConnectionService {
  public resourceUrl = SERVER_API_URL + 'api/my-sql-connections';

  constructor(protected http: HttpClient) {}

  create(mySQLConnection: IMySQLConnection): Observable<EntityResponseType> {
    return this.http.post<IMySQLConnection>(this.resourceUrl, mySQLConnection, { observe: 'response' });
  }

  update(mySQLConnection: IMySQLConnection): Observable<EntityResponseType> {
    return this.http.put<IMySQLConnection>(this.resourceUrl, mySQLConnection, { observe: 'response' });
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http.get<IMySQLConnection>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IMySQLConnection[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
