import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {SignInComponent} from './pages/sign-in/sign-in.component';
import {SignUpComponent} from './pages/sign-up/sign-up.component';
import {AuthComponent} from './auth.component';

const routes: Routes = [
  {
    path:'',
    component:AuthComponent,
    children:[
      { path:'',redirectTo:'sign-in',pathMatch:'full'},
      { path:'sign-in',component:SignInComponent},
      { path:'sign-up',component:SignUpComponent},
      { path:'**',redirectTo:'sign-in',pathMatch:'full'}
    ]
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class AuthRoutingModule { }
