import { Injectable, Output, EventEmitter } from '@angular/core';
import { Alert } from '../../models/clases/alert';


@Injectable({
  providedIn: 'root'
})
export class MensajePrincipalService {

  @Output() alertEmitter: EventEmitter<any> = new EventEmitter();

  constructor() { }

  setMensaje(mensaje: Alert) {
    this.alertEmitter.emit(mensaje);
  }
}
