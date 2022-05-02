import { DatePipe } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormControl, Validators } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { MtxGridColumn } from '@ng-matero/extensions';
import { TranslateService } from '@ngx-translate/core';
import { NgxSpinnerService } from 'ngx-spinner';
import { Observable } from 'rxjs';
import { map, startWith } from 'rxjs/operators';
import { BaseClass } from 'src/app/shared/components/base/baseClass';
import { MensajePrincipalService } from 'src/app/shared/components/mensajePrincipal/mensajePrincipal.service';
import { Alert, AlertType } from 'src/app/shared/models/clases/alert';
import { TraceoService } from 'src/app/shared/services/traceo.service';
import { UtilService } from 'src/app/shared/services/utilService';
import { APP_SPINNER, TIPO_GRILLA_BOTON, TIPO_GRILLA_NORMAL } from 'src/environments/environment';
import { Client } from '@stomp/stompjs';
import * as SockJS from 'sockjs-client';
import { Distancia } from 'src/app/shared/models/clases/Distancia';
import { Traceo } from 'src/app/shared/models/clases/Traceo';


@Component({
  selector: 'app-traceo',
  templateUrl: './traceo.component.html',
  styleUrls: ['./traceo.component.scss']
})
export class TraceoComponent extends BaseClass implements OnInit {

  private client!: Client;
  public conectado: boolean = false;
  controlCercana = new FormControl('');
  controlLejana = new FormControl('');
  controlPromedio = new FormControl('');
  controlDocumento = new FormControl('');

  controlIp  = new FormControl('');
  controlfechaActual = new FormControl('');
  controlpais = new FormControl('');
  controlisoCode = new FormControl('');
  controlidiomas = new FormControl('');
  controlhora = new FormControl('');
  controldistanciaEstimada = new FormControl('');
  controlmoneda = new FormControl('');

  tiposDocumentos: any[] | undefined;
  traceo: Traceo| undefined;
  
  alerta!: Alert;

  errorGenerico: Alert | undefined;

  get titulo(): string {
    return 'titulo';
  }

  /**
   * Constructor de la opción
   *
   * @param fb - Configulación del formulario de la opción
   * @param dialogs - Permite el manejo de ventanas modales
   * @param detClasificacionSrv - servicio para cargue de detalles de clasificacion
   * @param spinner - parametr control spinner
   * @param pasarelaService -  servicio para acceder a procesos de la pasarela de pago
   * @param utilService - servicio de utileria
   */

  constructor(private fb: FormBuilder,
    private dialogs: MatDialog,
    private spinner: NgxSpinnerService,
    public translateService: TranslateService,
    private traceoService: TraceoService,
    public utilService: UtilService,
    public mensajePricipal: MensajePrincipalService,
    private _router: Router,
    public datepipe: DatePipe) {
    super();
    this.controlDocumento.setValidators(Validators.pattern('\\b((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)(\\.|$)){4}\\b'));
    this.controlPromedio.disable();
    this.controlCercana.disable();
    this.controlLejana.disable();
    this.errorGenerico = new Alert ({type: AlertType.Error, message: 'Error al realizar la operación', autoClose: true, esTitulo: false, reiniciar: false});
  }



  /**
 * Método que permite mostrar los mensajes de error en la opción
 *
 * @param msg - Mensaje a visualizar
 * @param evento - Indica si los mensajes se deben limpiar o no
 */
  showErrorMgs(msg: any, evento: boolean = false) {
    if (msg !== null) {
      this.alerta = new Alert({ type: AlertType.Error, message: msg, autoClose: false, esTitulo: false, reiniciar: evento });
      this.setMensajePricipal(this.alerta);
    }
  }

  /**
 * Método que permite realizar la asignación de mensaje al componente principal
 *
 * @param alert - Información del mensaje a visualizar
 */
  setMensajePricipal(alert: Alert) {
    this.mensajePricipal.setMensaje(alert);
  }


  /**
   * metodo para limpiar controles
   */
  public limpiar(): void {
    this.controlDocumento.setValue(null);
  }

  /**
   * metodo que indica si boton consultar esta desabilitado
   */
  public consultarDisabled(): boolean {
    if (!this.controlDocumento.value
      || !this.controlDocumento.valid) return true;
    else return false;
  }

  validaBusqueda(): boolean {
    let entro = true;
    let dato;

    //TODO validaciones adicionales si son necesarias

    return entro;
  }

  /**
   * metodo para consultar la información
   */
  public consultar(): void {
    this.spinner.show(APP_SPINNER);
    this.mensajePricipal.setMensaje(new Alert({type: undefined, message: undefined, autoClose: true}));
    if (!this.validaBusqueda()){
      this.spinner.hide(APP_SPINNER);
      return;
    }

    this.traceoService.tracearIp(this.controlDocumento.value).subscribe(
      (res: any) => {
        this.traceo = res.data;
        this.controlIp.setValue(this.traceo?.ip);
        this.controlfechaActual.setValue(this.traceo?.fechaActual);
        this.controlpais.setValue(this.traceo?.pais);
        this.controlisoCode.setValue(this.traceo?.isoCode);
        this.controlidiomas.setValue(this.getIdiomasString(this.traceo?.idiomas));
        this.controlhora.setValue(this.traceo?.hora);
        this.controldistanciaEstimada.setValue(this.traceo?.distanciaEstimada);
        this.controlmoneda.setValue(this.traceo?.moneda);
      },
      (err: any) => {
        this.showErrorMgs(this.errorGenerico,
          true
        );
        this.spinner.hide(APP_SPINNER);
      },
      () => {
        this.spinner.hide(APP_SPINNER);
      }
    );

  }

  getIdiomasString(idiomas: string[] | undefined): any {
    let idiomasString = '';
    if (idiomas) {
      idiomas.forEach(element => {
        idiomasString += element + ' - ';
      });
    }
    //remove last characters
    idiomasString = idiomasString.substring(0, idiomasString.length - 2);
    return idiomasString;
  }


  ngOnInit() {
    this.client = new Client();
    this.client.webSocketFactory = ():any => {
      return new SockJS("http://localhost:8080//socket");
    }

    this.client.onConnect = (frame) => {
      console.log("Conectados: " + this.client.connected + ' : ' + frame);
      this.conectado = true;

      /**
       * Se ha sucrito a chat/mensaje donde escuchar cualquier
       * cambio que se haga en el servidor a nivel del canal
       * chat/mensaje
       */
      this.client.subscribe('/message', e => {
        let distancia: Distancia = JSON.parse(e.body) as Distancia;
        this.controlLejana.setValue(distancia.paisLejana + ' - ' + this.getDistancia(distancia.lejana));
        this.controlCercana.setValue(distancia.paisCercana + ' - ' + this.getDistancia(distancia.cercana));
        this.controlPromedio.setValue(this.getDistancia(distancia.promedio));

        console.log(distancia);
      })


    }

    this.client.onDisconnect = (frame) => {
      console.log("Desconectados: " + !this.client.connected + ' : ' + frame);
      this.conectado = false;
    }
    this.client.activate();
  }


  getDistancia(numero: number): any {
    return  (~~numero + ' Kms');
  }

  conectar() {
    this.client.activate();
  }

  desconectar() {
    this.client.deactivate();
  }


}
