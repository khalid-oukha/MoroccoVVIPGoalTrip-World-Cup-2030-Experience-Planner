import { NgModule } from '@angular/core';
import {CommonModule, NgOptimizedImage} from '@angular/common';

import { AuthRoutingModule } from './auth-routing.module';
import {AuthComponent} from './auth.component';
import { SignInComponent } from './pages/sign-in/sign-in.component';
import { SignUpComponent } from './pages/sign-up/sign-up.component';
import {AngularSvgIconModule, SvgIconComponent} from 'angular-svg-icon';
import {SharedModule} from "../../shared/shared.module";
import {ReactiveFormsModule} from "@angular/forms";
import {AuthService} from '../../core/services/auth.service';


@NgModule({
  declarations: [
    AuthComponent,
    SignInComponent,
    SignUpComponent
  ],
    imports: [
        CommonModule,
        AuthRoutingModule,
        AngularSvgIconModule.forRoot(),
        NgOptimizedImage,
        SharedModule,
        ReactiveFormsModule
    ],
  providers: [
    AuthService
  ]
})
export class AuthModule { }
