import { Component, Input, OnInit, TemplateRef, ViewChild } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { NavigationStart, Router } from '@angular/router';
import { Alert, AlertType } from '../../models/clases/alert';
import { MensajePrincipalService } from './mensajePrincipal.service';

@Component({
  selector: 'app-mensaje-principal',
  templateUrl: './mensajePrincipal.component.html',
  styleUrls: ['./mensajePrincipal.component.css']
})
export class MensajePrincipalComponent implements OnInit {

  @ViewChild('dialogo') dialogo!: TemplateRef<any>;
  @Input() id = 'default-alert';
  @Input() fade = true;

  alerts: Alert[] = [];
  dialogRef: any;

  constructor(private ms: MensajePrincipalService,private router: Router ,private dialog: MatDialog) {

   }

  ngOnInit() {
    this.ms.alertEmitter.observers = []; // solo 1 observable activo
    this.ms.alertEmitter.subscribe(alert => {
      // clear alerts when an empty alert is received
      if (!alert.message) {
          // filter out alerts without 'keepAfterRouteChange' flag
          this.alerts = this.alerts.filter(x => x.keepAfterRouteChange);

          // remove 'keepAfterRouteChange' flag on the rest
          // @ts-ignore: Object is possibly 'null'.
          this.alerts.forEach(x => delete x.keepAfterRouteChange);
          return;
      }

      // add alert to array
      this.asignarMensaje(alert);

      // auto close alert if required
     /* if (alert.autoClose) {
          setTimeout(() => this.removeAlert(alert), 3000);
      }*/
 });

   // clear alerts on location change
    this.router.events.subscribe(event => {
              if (event instanceof NavigationStart) {
                this.alerts = [];
              }
    });
  }

  ngOnDestroy() {
    // unsubscribe to avoid memory leaks

}

  asignarMensaje(mensaje: any): void {
    if(mensaje.reiniciar)this.reset();
    this.alerts.push(mensaje.message);
    if(!this.isDialogOpen(this.dialog)){
      this.dialogRef  = this.dialog.open(this.dialogo);
      this.dialogRef.afterClosed().subscribe(() => {
        this.dialogRef = null;
    })
    }
  }

  isDialogOpen(dialog: MatDialog): boolean {
    let valor = false;
    if(this.dialogRef) return true;
    return valor;
  }

  close(alert: Alert) {
    this.alerts.splice(this.alerts.indexOf(alert), 1);
  }

  reset() {
    this.alerts = [];
  }

  cssClass(alert: Alert) {
    if (!alert) return;

    const classes = ['alert', 'alert-dismissable'];

    const alertTypeClass = {
        [AlertType.Success]: 'alert alert-success',
        [AlertType.Error]: 'alert alert-danger',
        [AlertType.Info]: 'alert alert-info',
        [AlertType.Warning]: 'alert alert-warning'
    }

    classes.push(alertTypeClass[alert.type]);

    if (alert.fade) {
        classes.push('fade');
    }

    return classes.join(' ');
}

getIcon(operacion: boolean): string{
  let valor= '';
  for(let element of this.alerts){
    if(!operacion)valor = this.getIconoForma(element.type);
    else valor = this.getIconoColor(element.type);
    break;
  };
  return valor;
}

  getIconoForma(type: AlertType): string {
    if(type === AlertType.Error) return 'error_outline';
    if(type === AlertType.Warning) return 'warning';
    if(type === AlertType.Info) return 'info';
    if(type === AlertType.Success) return 'check_circle_outline';
    return '';
  }

  getIconoColor(type: AlertType): string {
    if(type === AlertType.Error) return 'rojo';
    if(type === AlertType.Warning) return 'amarillo';
    if(type === AlertType.Info) return 'azul';
    if(type === AlertType.Success) return 'verde';
    return '';
  }


removeAlert(alert: Alert) {
  // check if already removed to prevent error on auto close
  if (!this.alerts.includes(alert)) return;

  if (this.fade) {
      // fade out alert
      // @ts-ignore: Object is possibly 'null'.
      this.alerts.find(x => x === alert).fade = true;

      // remove alert after faded out
      setTimeout(() => {
          this.alerts = this.alerts.filter(x => x !== alert);
      }, 250);
  } else {
      // remove alert
      this.alerts = this.alerts.filter(x => x !== alert);
  }
}

}
