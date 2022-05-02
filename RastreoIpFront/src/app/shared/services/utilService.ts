import { CurrencyPipe, DatePipe } from "@angular/common";
import { Injectable } from "@angular/core";
import { AbstractControl, FormControl, ValidatorFn } from "@angular/forms";
import { DomSanitizer } from "@angular/platform-browser";
import { TranslateService } from "@ngx-translate/core";

@Injectable({
  providedIn: 'root'
})
export class UtilService {



  getCampoValor(campo: string, valor: any): any {
    return this.sanitized.bypassSecurityTrustHtml('<div class="dataLabel" data-label="'+ campo + '">' + (valor ? valor : '') + '</div>');
  }

  constructor(public datepipe: DatePipe, public currencyPipe: CurrencyPipe, private sanitized : DomSanitizer,public translateService: TranslateService){

  }

  isNumber(n : any) {
    return !isNaN(parseFloat(n)) && !isNaN(n - 0);
  }

  /*
  *validar si el dato suministrado se encuentra en el array, retorna un objeto
  */
  existeDatoPorNombre(options: any[], dato: any): any {
    return options.find(obj => obj.nombre === dato);
  }

    /*
  *validar si el dato suministrado se encuentra en el array, retorna un objeto
  */
  existeDatoPorCodigoNombre(options: any[], dato: any): any {
    return options.find(obj => obj.codigo + ' - ' + obj.nombre === dato);
  }

      /*
  *validar si el dato suministrado se encuentra en el array, retorna un objeto por campo definido
  */
  existeDatoPorCampoDefinido(options: any[], dato: any,campoDefinido: string): any {
    return options.find(obj => obj[campoDefinido] === dato);
  }

        /*
  *validar si el dato suministrado se encuentra en el array, retorna un objeto por campo definido
  */
  existeDatoPorCampos(options: any[], dato: any,primero: string, segundo: string): any {
    return options.find(obj => obj[primero] + ' - '+ obj[segundo] === dato);
  }



      /*
  *validar si el dato suministrado se encuentra en el array, retorna un objeto
  */
  existeDatoPorIdNombre(options: any[], dato: any): any {
    return options.find(obj => obj.id + ' - ' + obj.nombre === dato);
  }

  /*
  * Validar si el dato suministrado se encuentra en el array, comparando por código retorna un objeto
  */
  existeDatoPorCodigo(options: any[], dato: any): any {
    return options.find(obj => obj.codigo === dato);
  }

  /*
  * Validar si el dato suministrado se encuentra en el array, comparando por la etiqueta retorna un objeto
  */
  existeDatoPorEtiqueta(options: any[], dato: any): any {
    return options.find(obj => obj.etiqueta === dato);
  }

  static getFechaAno(fecha: any): number{
    return new Date(fecha).getFullYear();
  }


  getValorFormato(valor: number, formato?: string): any {
    return this.currencyPipe.transform(valor,'USD','symbol',formato ? formato : '1.0-0');
  }

  validarPorcentaje(event: any, valor: any): boolean {
    var charCode = (event.which) ? event.which : event.keyCode;
    var isOk = true;
    if (charCode != 46 && charCode > 31
      && (charCode < 48 || charCode > 57)) {
        isOk = false;
    } else if(valor && String(valor).indexOf('.') !== -1 && charCode === 46){
      isOk = false;
    }

    return isOk;
  }

  keydownNumericoEntero(event: any, control: FormControl) {
    if (event?.key?.length <= 1) {
      if (!(event.altKey || event.ctrlKey || event.metaKey)) {
        if (!event.key.match(/\d/)) {
          event.preventDefault();
        }
      }
    }
  }

  pasteNumericoEntero(event: any, control: FormControl) {
    control.setValue(control.value.replaceAll(/([^\d])/g,''));
  }

  keydowNumericoEnteroBusqueda(event: any, control: FormControl) {
    if (event?.key?.length <= 1) {
      if (!(event.altKey || event.ctrlKey || event.metaKey)) {
        if (!event.key.match(/[\d|\||>|<|=|:]/)) {
          event.preventDefault();
        }
      }
    }
  }

  pasteNumericoEnteroBusqueda(event: any, control: FormControl) {
    control.setValue(control.value.replaceAll(/([^\d|\||>|<|=|:])/g,''));
  }

  pasteSoloTextoBusqueda(event: any, control: FormControl) {
    control.setValue(control.value.replaceAll(/[^a-zA-Z*]/g,''));
  }

  keydownTextoBusqueda(event: any, control: FormControl) {
    if (event?.key?.length <= 1) {
      if (!(event.altKey || event.ctrlKey || event.metaKey)) {
        if (!event.key.match(/[^0-9.]/)) {
          event.preventDefault();
        }
      }
    }
  }



  /*
  * Metodo para cargar array de elementos de tablas editables tipo excel
  */
  getArrayValores(objetos: any[], objetosExcel: any[], obj: any): any[] {
    objetos.forEach(element => {
      const excelObj = obj.getExcelObj(element, this);
      objetosExcel.push(Object.values(excelObj));
    });
    if(!objetosExcel.length){      const excelObj = obj.getExcelObj({}, this); objetosExcel.push(Object.values(excelObj));}
    return objetosExcel;
  }

  formatoNumero(valor: number): any{
    if(!valor) valor = 0;
    return this.currencyPipe.transform(valor, 'USD', '', '1.0-2');
  }

  formatoMoneda(valor: number): any{
    if(!valor) valor = 0;
    return this.currencyPipe.transform(valor, 'USD', 'symbol', '1.0-2');
  }


  limpiarValorDecimal(valor: any): any {
    return valor ? valor.replaceAll(/([^\d\.])/g, '') : 0;
  }

  /*
  * Validar si el dato suministrado se encuentra en el array, retorna un objeto
  */
  existeDatoPorId(options: any[], dato: any): any {
    return options.find(obj => obj.id  === dato);
  }

  /**
   * Redondear un número a N decimales
   * @param num Número a redondear
   * @param decimales Número de decimales a aplicar
   * @returns Número redondeado a dos decimales
   */
  redondearNumeroDosDecimales(num: number, decimales?: number){
    if(!num) num = 0;
    if(!decimales) decimales = 2;

    let factor: number = this.completarFactor(1, decimales);
    return Math.round((num + Number.EPSILON) * factor) / factor;
  }

  /**
   * Método que permite obtener el factor a aplicar para redondear un número según los decimales deseados
   *
   * @param num Factor a utilizar para redondeo de decimales
   * @param size Número de decimales a aplicar
   * @returns Factor final a aplicar para el redondeo
   */
  completarFactor(num: any, size: number) {
    num = num.toString();
    while (num.length <= size) num = num + "0";
    return num;
  }

  /**
   * Método que permite validar cajas numéricas en los búscadores según las reglas de consulta del cliente
   * Ejemplo: >500, <=340,  1:10 (rango),  2|9|7 (valores específicos)
   * @param event - Evento de tecla ejecutado en el control
   * @param control - Control que lanza el evento
   */
  keydowNumeroEnteroBusqueda(event: any, control: FormControl) {
    if (event?.key?.length <= 1) {
      if (!(event.altKey || event.ctrlKey || event.metaKey)) {
        if (!event.key.match(/\d|[>|<|=|\||:]/)) {
          event.preventDefault();
        } else if(control.value){
          let pos = event.target.selectionStart;
          var post = control.value.substring(0,pos) + event.key + control.value.substring(pos);
          var matches = this.match(post);
          if (!matches) {
              event.preventDefault();
          }
        }
      }
    }
  }

  /**
   * Método que permite validar cajas numéricas en los búscadores según las reglas de consulta del cliente
   * Ejemplo: >500, <=340,  1:10 (rango),  2|9|7 (valores específicos)
   * @param event - Evento ejecutado en el control
   * @param control - Control que lanza el evento
   */
  pasteNumeroEnteroBusqueda(event: any, control: FormControl) {
    if(control && control.value){
      let post = control.value;
      var matches = this.match(post);
      if (!matches) {
        control.setValue('');
      }
    }
  }

  /**
   * Método que permite validar los patrones de expresiones permitidas para cajas de búsqueda de tipo numérico
   * @param post - Cadena a validar
   * @returns Booleano indicando si la expresión es o no válida
   */
  match(post: any) {
    var matches = false;
    if (post.match(/[<|>]{0,1}((?<=[<|>])=){0,1}\d*/)[0] == post) {
        matches = true;
    }
    if (post.match(/\d*((?<=\d):){0,1}\d*/)[0] == post) {
        matches = true;
    }
    if (post.match(/(\d*((?<=\d)\|){0,1}\d*)(\|{0,1}\d*){0,}/)[0] == post) {
        matches = true;
    }
    return matches;
  }

  /**
   * Agregar dias a una fecha
   * @param fecha Fecha inicial
   * @param dias Número de días a sumar
   * @returns Fecha futura
   */
  addDays(fecha: Date, dias : number): Date{
    var futureDate = fecha;
    futureDate.setDate(fecha.getDate() + dias);
    return futureDate;
  }

  /**
   * Método que permite obtener el año actual
   * @returns Vigencia actual
   */
  obtenerVigenciaActual(): number{
    return (new Date()).getFullYear();
  }

  /**
   * Método que permite obtener el mes actual
   * @returns Mes actual
   */
  obtenerMesActual(): number{
    return (new Date()).getMonth() + 1;
  }

  /**
   * Método que permite validar si un mail es válido
   * @param email Correo a validar
   * @returns Booleano indicando si la cadena corresponde o no a un mail válido
   */
  validarEmail(email: any): boolean {
    const re = /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
    return re.test(email);
  }

  /**
     * Método que permite convertir Bytes a MG
     * @param bytes Número a convertir
     * @param decimals Número de decimales a aplicar
     * @returns Valor correspondiente en MB
     */
  convertirBytesToMB(bytes: any, decimals = 2): number {
    if (bytes === 0) return 0;
    const k = 1024;
    const dm = decimals < 0 ? 0 : decimals;
    const i = Math.floor(Math.log(bytes) / Math.log(k));
    return parseFloat((bytes / Math.pow(k, i)).toFixed(dm));
  }

  /*
   * Validar si el dato suministrado se encuentra en el array, comparando por el identificador
   */
  buscarIndiceLista(options: any[], dato: number): number {
    return options.findIndex(obj => obj.id === dato);
  }

  /**
   * Método que permite reemplazar los caracteres especiales
   * @param cadena - Cadena a procesar
   * @returns Cadena con los caracteres especiales reemplazados por sus equivalencias
   */
  reemplazarCaracteresEspeciales(cadena: string): string{
    var resultado = '';
    if(cadena){
      cadena = cadena.replace(/[ñ]/g, '[[n]]')
               .replace(/[Ñ]/g, '[[N]]')
               .trim()  // Quitar espacios al inicio y final de la cadena
               .replace(/\s+/g,' ') // Quitar espacios múltiples entre la cadena
               .replace(/ /g, '[[_]]') // Reemplazar el carácter espacio
               ;
      resultado = cadena.normalize('NFD').replace(/[\u0300-\u036f]/g, '');
      resultado = resultado.replace(/(\[\[n\]\])/g, 'ñ').replace(/(\[\[N\]\])/g,'Ñ').replace(/(\[\[_\]\])/g,' ');
      resultado = resultado.replace(/[^a-zA-Z0-9ñÑ\-*\&\s]/g, '').trim();
    }
    return resultado;
  }

  /**
   * Método que permite validar si una cadena contine algún número
   * @param cadena - Cadena a validar
   * @returns Booleano indicando si la cadena contiene o no números, true: SI, false: NO
   */
  validarTieneNumeros(cadena: string): boolean{
    let resultado: boolean = false;
    // Regular expression
    const regex = /\d/;
    if(cadena){
      resultado = regex.test(cadena);
    }
    return resultado;
  }

}
