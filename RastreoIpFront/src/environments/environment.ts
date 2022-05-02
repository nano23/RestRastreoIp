export const APP_SPINNER = "APP_SPINNER";

export const environment = {
  production: false,

  API_PATH_TRACEO : 'http://localhost:8080/rest/v1',
};

export let TIPO_GRILLA_BOTON = {
  header: null,
  field: 'operation',
  width: '160px',
  pinned: 'right',
  right: '0px',
  type: 'button',
  class: 'alinearCentro',
  buttons: [
    {
      type: 'icon',
      text: 'delete',
      icon: 'attach_money',
      tooltip: '',
      color: 'warn',
      popOkText: '',
      popCloseText: '',
      popDescription: null,
      popOkColor: 'primary',
      popCloseColor: 'accent',
      pop: true,
      popTitle: '',
      click: null,
      iif: null,
      class: 'botonGrilla botonGenerar'
    },
    {
      type: 'icon',
      text: 'delete',
      icon: 'access_time',
      tooltip: '',
      color: 'error',
      popOkText: '',
      popCloseText: '',
      popDescription: null,
      popOkColor: 'primary',
      popCloseColor: 'warn',
      pop: true,
      popTitle: '',
      click: null,
      iif: null,
      class: 'botonGrilla botonMensaje'
    },
  ],
};

export let TIPO_GRILLA_NORMAL = {
  header: '',
  field: '',
  type: null,
  formatter: null,
  cellTemplate: null,
  class: null,
  sortable: true,
  description: '',
  pinned: null,
  width: null
};
