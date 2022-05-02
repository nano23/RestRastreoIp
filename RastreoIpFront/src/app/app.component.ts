import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { Observable } from 'rxjs';
import { map, startWith } from 'rxjs/operators';
import { TranslateService } from '@ngx-translate/core';
import { BaseClass } from './shared/components/base/baseClass';
import { APP_SPINNER } from 'src/environments/environment';


/**
 * Clase que permite el manejo de la opción gestión de Movimientos Masivos
 *
 * @author Carlos Fernando Gomez Salazar - Visión Ingeniería
 * @version 1
 */
 @Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {

  APP_SPINNER = APP_SPINNER;

  constructor(public translate: TranslateService){
    translate.addLangs(['es-CO', 'en-US']);
    translate.setDefaultLang('es-CO');
  }



}
