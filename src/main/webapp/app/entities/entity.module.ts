import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'my-sql-connection',
        loadChildren: () => import('./my-sql-connection/my-sql-connection.module').then(m => m.JbeebrMySQLConnectionModule)
      }
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ])
  ]
})
export class JbeebrEntityModule {}
