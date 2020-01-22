import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IMySQLConnection, MySQLConnection } from 'app/shared/model/my-sql-connection.model';
import { MySQLConnectionService } from './my-sql-connection.service';

@Component({
  selector: 'jhi-my-sql-connection-update',
  templateUrl: './my-sql-connection-update.component.html'
})
export class MySQLConnectionUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    name: [],
    hostname: [],
    port: [],
    dbName: [],
    username: [],
    password: []
  });

  constructor(
    protected mySQLConnectionService: MySQLConnectionService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ mySQLConnection }) => {
      this.updateForm(mySQLConnection);
    });
  }

  updateForm(mySQLConnection: IMySQLConnection): void {
    this.editForm.patchValue({
      id: mySQLConnection.id,
      name: mySQLConnection.name,
      hostname: mySQLConnection.hostname,
      port: mySQLConnection.port,
      dbName: mySQLConnection.dbName,
      username: mySQLConnection.username,
      password: mySQLConnection.password
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const mySQLConnection = this.createFromForm();
    if (mySQLConnection.id !== undefined) {
      this.subscribeToSaveResponse(this.mySQLConnectionService.update(mySQLConnection));
    } else {
      this.subscribeToSaveResponse(this.mySQLConnectionService.create(mySQLConnection));
    }
  }

  private createFromForm(): IMySQLConnection {
    return {
      ...new MySQLConnection(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      hostname: this.editForm.get(['hostname'])!.value,
      port: this.editForm.get(['port'])!.value,
      dbName: this.editForm.get(['dbName'])!.value,
      username: this.editForm.get(['username'])!.value,
      password: this.editForm.get(['password'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMySQLConnection>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }
}
