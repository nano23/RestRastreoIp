import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AppComponent } from './app.component';
import { TraceoComponent } from './components/traceo/traceo.component';

const routes: Routes = [
      { path: '', redirectTo: 'traceo', pathMatch: 'full' },
      {
        path: 'traceo',
        component: TraceoComponent,
        data: { title: 'traceo', titleI18n: 'traceo' },
      }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
