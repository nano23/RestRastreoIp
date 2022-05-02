import { FormControl, ValidatorFn, Validators } from "@angular/forms";
import { TranslateService } from "@ngx-translate/core";
import { Observable } from "rxjs";
import { map, startWith } from "rxjs/operators";


export enum EnumTiposCampos {
  BOOLEAN,
  NONE
}

export class BaseClass {

  protected static validadorNumerico = Validators.pattern('^[0-9]+$');
  protected static validadorRequerido = Validators.required;
  protected static campoHabilitadoStatic: Map<String, any> ;
  protected static campoDesabilitadoStatic: Map<String, any>;
  //validar años
  protected static validadorAno = Validators.pattern('^\\d{4}$');
  protected static validadorPorcentaje = Validators.pattern('^(?:\\d{1,2}(?:\\.\\d{1,2})?|100(?:\\.0?0)?)$');

  rutaNavegacion: string[] = [];
  maxDate: Date;

  query = {
    q: 'null',
    sort: 'id',
    order: 'asc',
    page: 0,
    per_page: 5,
  };

  public static _initializeCamposStatic() {
    this.campoHabilitadoStatic = new Map();
    this.campoHabilitadoStatic.set('value', "");
    this.campoHabilitadoStatic.set('disabled', false);
    this.campoDesabilitadoStatic = new Map();
    this.campoDesabilitadoStatic.set('value', "");
    this.campoDesabilitadoStatic.set('disabled', true);
  }

  constructor() {
    this.maxDate = new Date();
  }

  /**
   * Método que retorna el separador de decimales
   * @returns Separador de decimales
   */
  getThousandSeparator(): string {
    return '.';
  }

  /**
   * Método que permite traer mensaje de otro
   *  
   * @returns cadena con el texto
  */
  getMensaje(obj: string, translateService: TranslateService): string {
    return translateService.instant(obj);
  }

  /**
   * Método que permite obtener el mensaje de validación para información inválida
   *
   * @param obj - Etiqueta del campo al cual se le realiza la validación
   * @returns Cadena con el mensaje a presentar
   */
  getMensajeInvalido(obj: string, translateService: TranslateService): string {
    return translateService.instant('generales.comunes.invalido', {
      objeto: translateService.instant(obj)
    });
  }

  /**
   * Método que permite obtener el mensaje de validación para campos tipo fecha con información inválida
   *
   * @param obj - Etiqueta del campo al cual se le realiza la validación
   * @returns Cadena con el mensaje a presentar
   */
  getMensajeInvalidoFechas(obj: string, translateService: TranslateService): string {
    return translateService.instant('generales.comunes.invalidoFecha', {
      objeto: translateService.instant(obj)
    });
  }

  /**
   * Método que permite obtener la etiqueta de registro no encontrados en la grilla
   * 
   * @returns Etiqueta a visualizar cuando no se presentan resultados en la grilla
   */
  getSinResultados(translateService: TranslateService) {
    return translateService.instant('generales.comunes.no_resultados');
  }

  /**
   * Método que permite realizar el filtro para el control de autocompletar por etiqueta
   * 
   * @param value - Cadena digitada por el usuario
   * @returns Lista con el conjunto de registros que contienen la cadena ingresada
  */
  protected _filteredEtiqueta(value: string, objetos: any): any[] {
    const filterValue = value.toLowerCase();
    return objetos.filter((option: any) => option.etiqueta.toLowerCase().includes(filterValue));
  }

  /**
 * Método que permite realizar el filtro para el control de autocompletar por otro campo
 * 
 * @param value - Cadena digitada por el usuario
 * @returns Lista con el conjunto de registros que contienen la cadena ingresada
*/
  protected _filteredOtroCampo(value: string, objetos: any, campo: string): any[] {
    const filterValue = value.toLowerCase();
    if(objetos) return objetos.filter((option: any) => option[campo].toLowerCase().includes(filterValue));
    else return [];
  }

  /**
 * Método que permite inicializar form control
 * @param control - control que se esta iniciando
 * @param campos - campos del control que se van a actualizar
 * @param objeto - objeto de la clase que se va a actualizar
 * @param atributo - atributo del objeto que se va a actualizar cuando se hagan modificaciones
 * @param listaValores - cuando la asignacion de valores depende de una lista
 */
  iniciaGenericoFormControl(control: FormControl, campos: Map<String, any>, objeto: any, atributo: string, validadores: ValidatorFn[], listaValores?: any[], tipo?: EnumTiposCampos): FormControl {
    control = new FormControl();
    if (campos) {
      for (let campo of campos.keys()) {
        const valor = campos.get(campo);
        if (campo === 'value') control.setValue(valor);
        if (campo === 'disabled' && valor) control.disable();
      }
    }
    if (validadores && validadores.length > 0) {
      control.setValidators(validadores);
    }
    control.valueChanges.subscribe((res: any) => {
      if (objeto && atributo) {
        if (!atributo.includes('.')) {
          if (listaValores) objeto[atributo] = this.getValorDeLista(listaValores, res, 'nombre');
          else objeto[atributo] = this.SetValorPorTipo(res, (tipo ? tipo : EnumTiposCampos.NONE));
        } else {
          const atr1 = atributo.split('.')[0];
          const atr2 = atributo.split('.')[1];
          if (listaValores) objeto[atr1][atr2] = this.getValorDeLista(listaValores, res, 'nombre');
          else objeto[atr1][atr2] = this.SetValorPorTipo(res, (tipo ? tipo : EnumTiposCampos.NONE));
        }
      }
    });
    return control;
  }
  SetValorPorTipo(res: any, tipo: EnumTiposCampos): any {
    if (tipo === EnumTiposCampos.BOOLEAN) {
      if (res === true) return 1;
      if (res === false) return 0;
    }
    return res;
  }

  /**
   * Método que permite asignar el valor a la variable de los menús de navegación de la opción
   * @param menuVisionSrv Servicio para la gestión de menús
   * @param menuId Identificador de la opción actual
   */
  /*obtenerRutaNavegacion(menuVisionSrv: MenuVisionService, menuId: number) {
    this.menusMiga = [];
    menuVisionSrv.obtenerMigaMenus(menuId).subscribe((result: any) => {
      if (result && result.status && result.status === 'SUCCESS') {
        this.menusMiga = result.listado;
      }
    });
  }*/

  /**
* Método que permite verificar si un elemento existe en una lista por id del objeto
* @param lista - listado de objetos
* @param obj - objeto a buscar
* @return boolean undica si existe o no
*/
  existeEnGrillaPorId(lista: any[], obj: any): boolean {
    let existe = false;
    lista.forEach(element => {
      if (obj && obj.id && element && element.id && obj.id == element.id) existe = true;
    });
    return existe;
  }

  /**
* Método que permite clonar objetos
* @param lista - listado de objetos
* @param obj - objeto a buscar
* @return boolean undica si existe o no
*/
  clonarObj(obj: any): any {
    return JSON.parse(JSON.stringify(obj));
  }

  /**
* Método que permite traer el valor de una lista
* @param lista a analizar
* @param valor retornado a buscar en la lista
* @param campo en el que busca la coincidencia
*/
  getValorDeLista(lista: any[], valor: any, campo: string): any {
    for (let element of lista) {
      if (element[campo] === valor) return element.id;
    }
    return null;

  }

  /**
* Método que permite traer el valor de una lista
* @param objetos lista de valores
* @param control nombre del control
* @returns observable de un array
*/
  crearFilter(objetos: any[], control: FormControl): Observable<any[]> {
    return control.valueChanges.pipe(
      startWith(''),
      map(value => this._filteredEtiqueta(value, objetos))
    );
  }

  /**
 * Método que permite obtener el mensaje de validación para campos obligatorios
 *
 * @param obj - Etiqueta del campo al cual se le realiza la validación
 * @returns Cadena con el mensaje a presentar
 */
  getMensajeRequerido(obj: string, translateService: TranslateService): string {
    return translateService.instant('generales.comunes.requerido', {
      objeto: translateService.instant(obj)
    });
  }

  /**
* Método que permite establecer las etiquetas a asignar a la ventana modal de confirmación de guardado
* @returns Información con el título y contenido de la ventana modal de confirmación de guardado
*/
 /* getDialogGuardar(translateService: TranslateService): InfoDialogo {
    return {
      mensaje: translateService.instant('generales.comunes.guardar.confirm'),
      titulo: translateService.instant('generales.comunes.confirm.titulo')
    };
  }*/

  getBuscadorUnidad(translateService: TranslateService) {
    return {
      titulo: translateService.instant('contratacion.contrato.titulo_buscador_unidad'),
      tooltip: translateService.instant('contratacion.contrato.titulo_buscador_unidad')
    };
  }

  getBuscadorRubros(translateService: TranslateService) {
    return {
      titulo: translateService.instant('presupuesto.solicitudMovimientoPptal.detalles.buscadorRubros'),
      tooltip: translateService.instant('presupuesto.solicitudMovimientoPptal.detalles.buscadorRubros')
    };
  }


  /**
* Método que permite mostrar los mensajes de error en la opción
*
* @param msg - Mensaje a visualizar
* @param evento - Indica si los mensajes se deben limpiar o no
*/
 /* showErrorMgs(msg: string, evento: boolean = false, mensajePricipal: MensajePrincipalService) {
    if (msg !== null) {
      const alerta = new Alert({ type: AlertType.Error, message: msg, autoClose: false, esTitulo: false, reiniciar: evento });
      this.setMensajePricipal(alerta, mensajePricipal);
    }
  }*/

  /**
   * Método que permite realizar la asignación de mensaje al componente principal
   *
   * @param alert - Información del mensaje a visualizar
   */
 /* setMensajePricipal(alert: Alert, mensajePricipal: MensajePrincipalService) {
    mensajePricipal.setMensaje(alert);
  }*/

  /**
   * Método que permite traer el nombre del mes por el numero
   *
   * @param fecha - fecha de la que se obtiene el mes
   * @param mesesService - servicio para obtener los meses
   */
  getMesNombre(fecha: Date, meses: any[]): String {
    let mesNombre = "";
    const mes = new Date(fecha).getMonth() + 1;
    const mesStr = mes < 10 ? '0' + mes : '' + mes;

    if (meses) {
      meses.forEach(element => {
        if (element.numero === mesStr) mesNombre = element.nombre;
      });
    }

    return mesNombre;
  }

    /**
   * 
   * @param listado listado a filtrar
   * @param campo  campo que ordena
   * @param tipo asc o desc
   * @returns listado ordenado
   */
     ordenar(listado: any[], campo: string, tipo: string): any[] {
      return listado.sort(function(a, b) {
        if(tipo === 'asc') return a[campo] - b[campo] ;
        if(tipo === 'desc') return a[campo] + b[campo] ;
      });
    }

}