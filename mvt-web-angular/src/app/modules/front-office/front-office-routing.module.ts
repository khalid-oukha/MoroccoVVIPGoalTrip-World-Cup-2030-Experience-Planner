import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { FrontOfficeComponent } from './front-office.component';
import { HomeComponent } from './pages/home/home.component';
import {ActivityDetailsComponent} from "./pages/activity-details/activity-details.component";
import {ActivitiesComponent} from "./pages/activities/activities.component";
import {MyPlansComponent} from "./pages/my-plans/my-plans.component";

const routes: Routes = [
  {
    path: '',
    component: FrontOfficeComponent,
    children: [
      { path: '', redirectTo: 'home', pathMatch: 'full' },
      { path: 'home', component: HomeComponent },
      {path: 'activities', component: ActivitiesComponent },
      { path: 'activities/:id', component: ActivityDetailsComponent },
      {path:'my-plans', component: MyPlansComponent },
      { path: '**', redirectTo: 'errors/404' },
    ],
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class FrontOfficeRoutingModule { }
