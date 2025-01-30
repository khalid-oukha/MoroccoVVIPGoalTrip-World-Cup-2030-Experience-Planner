import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import {SharedModule} from './shared/shared.module';
import {NgxSonnerToaster} from 'ngx-sonner';
import {HTTP_INTERCEPTORS} from '@angular/common/http';
import {BaseUrlInterceptor} from './core/interceptor/base-url.service';
import {AuthorizationHeaderInterceptor} from './core/interceptor/authorization-header.service';


@NgModule({
  declarations: [
    AppComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    SharedModule,
    NgxSonnerToaster
  ],
  providers: [
    { provide: HTTP_INTERCEPTORS, useClass: BaseUrlInterceptor, multi: true },
    { provide:HTTP_INTERCEPTORS,useClass: AuthorizationHeaderInterceptor, multi: true},
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
