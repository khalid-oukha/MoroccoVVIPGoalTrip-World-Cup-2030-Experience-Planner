import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ButtonComponent } from './components/button/button.component';
import { ResponsiveHelperComponent } from './components/responsive-helper/responsive-helper.component';



@NgModule({
  declarations: [
    ButtonComponent,
    ResponsiveHelperComponent
  ],
  imports: [
    CommonModule
  ],
  exports: [ButtonComponent,ResponsiveHelperComponent]
})
export class SharedModule { }
