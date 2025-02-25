import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { DashboardComponent } from './dashboard.component';
import { UserComponent } from './pages/user/user.component';
import { ActivitesComponent } from './pages/activites/activites.component';
import { ActivityDetailsComponent } from './pages/activity-details/activity-details.component';
import { CategoriesComponent } from './pages/categories/categories.component';

const routes: Routes = [
  {
    path: '',
    component: DashboardComponent,
    children: [
      { path: '', redirectTo: 'users', pathMatch: 'full' },
      {path: 'activities/:id', component: ActivityDetailsComponent},
      { path: 'activities', component: ActivitesComponent },
      { path: 'users', component: UserComponent },
      {path:'categories', component: CategoriesComponent},
      { path: '**', redirectTo: 'errors/404' },
    ],
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class DashboardRoutingModule {}
