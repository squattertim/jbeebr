import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IMySQLConnection } from 'app/shared/model/my-sql-connection.model';
import { MySQLConnectionService } from './my-sql-connection.service';
import { MySQLConnectionDeleteDialogComponent } from './my-sql-connection-delete-dialog.component';

@Component({
  selector: 'jhi-my-sql-connection',
  templateUrl: './my-sql-connection.component.html'
})
export class MySQLConnectionComponent implements OnInit, OnDestroy {
  mySQLConnections?: IMySQLConnection[];
  eventSubscriber?: Subscription;

  constructor(
    protected mySQLConnectionService: MySQLConnectionService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal
  ) {}

  loadAll(): void {
    this.mySQLConnectionService.query().subscribe((res: HttpResponse<IMySQLConnection[]>) => {
      this.mySQLConnections = res.body ? res.body : [];
    });
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInMySQLConnections();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IMySQLConnection): string {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInMySQLConnections(): void {
    this.eventSubscriber = this.eventManager.subscribe('mySQLConnectionListModification', () => this.loadAll());
  }

  delete(mySQLConnection: IMySQLConnection): void {
    const modalRef = this.modalService.open(MySQLConnectionDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.mySQLConnection = mySQLConnection;
  }
}
