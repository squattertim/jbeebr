import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IMySQLConnection } from 'app/shared/model/my-sql-connection.model';
import { MySQLConnectionService } from './my-sql-connection.service';

@Component({
  templateUrl: './my-sql-connection-delete-dialog.component.html'
})
export class MySQLConnectionDeleteDialogComponent {
  mySQLConnection?: IMySQLConnection;

  constructor(
    protected mySQLConnectionService: MySQLConnectionService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: string): void {
    this.mySQLConnectionService.delete(id).subscribe(() => {
      this.eventManager.broadcast('mySQLConnectionListModification');
      this.activeModal.close();
    });
  }
}
