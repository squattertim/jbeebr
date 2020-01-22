import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IMySQLConnection } from 'app/shared/model/my-sql-connection.model';

@Component({
  selector: 'jhi-my-sql-connection-detail',
  templateUrl: './my-sql-connection-detail.component.html'
})
export class MySQLConnectionDetailComponent implements OnInit {
  mySQLConnection: IMySQLConnection | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ mySQLConnection }) => {
      this.mySQLConnection = mySQLConnection;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
