export class Alert {
  id!: string;
  type!: AlertType;
  message!: string;
  autoClose: boolean = false;
  keepAfterRouteChange!: boolean;
  fade!: boolean;
  esTitulo?: boolean = false;
  reiniciar: boolean = false;

  constructor(init?: Partial<Alert>) {
      Object.assign(this, init);
  }
}

export enum AlertType {
  Success,
  Error,
  Info,
  Warning
}

