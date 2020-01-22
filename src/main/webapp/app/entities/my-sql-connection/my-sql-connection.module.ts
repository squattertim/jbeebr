import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { JbeebrSharedModule } from 'app/shared/shared.module';
import { MySQLConnectionComponent } from './my-sql-connection.component';
import { MySQLConnectionDetailComponent } from './my-sql-connection-detail.component';
import { MySQLConnectionUpdateComponent } from './my-sql-connection-update.component';
import { MySQLConnectionDeleteDialogComponent } from './my-sql-connection-delete-dialog.component';
import { mySQLConnectionRoute } from './my-sql-connection.route';

@NgModule({
  imports: [JbeebrSharedModule, RouterModule.forChild(mySQLConnectionRoute)],
  declarations: [
    MySQLConnectionComponent,
    MySQLConnectionDetailComponent,
    MySQLConnectionUpdateComponent,
    MySQLConnectionDeleteDialogComponent
  ],
  entryComponents: [MySQLConnectionDeleteDialogComponent]
})
export class JbeebrMySQLConnectionModule {}
