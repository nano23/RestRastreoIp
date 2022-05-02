import { HttpErrorResponse, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { catchError, mergeMap } from "rxjs/operators";
import { environment } from "src/environments/environment";

export const CONST_API_PUBLICAS = [
  '/rest/'
];

@Injectable()
export class DefaultInterceptor implements HttpInterceptor {
  constructor(
  ) {}

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    // Add server host
    // const url = environment.SERVER_ORIGIN + req.url;
    const url = req.url;

    const headersAuth = {
      'Accept': 'application/json',
      'Content-Type': 'application/json',
     // 'Authorization': 'Basic ' + btoa(environment.ID + ':'+ environment.SECRET)  ,
    };

    const headersNoAuth = {
      'Accept': 'application/json',
      'Content-Type': 'application/json'
    };

    // Only intercept API url
    if (!url.includes('/api/')) {
      return next.handle(req);
    }

    const isPublicApi = this.isApiPublic(req.url);

    // All APIs need JWT authorization
    let headers = null;
    if (!isPublicApi) {
      headers = headersAuth;
    } else {
      headers = headersNoAuth;
    }

    let newReq = null;
    if (!isPublicApi) {
      newReq = req.clone({ url, setHeaders: headers });
    } /*newReq = req.clone({ url, setHeaders: headers, withCredentials: true });*/ else {
      newReq = req.clone({ url, setHeaders: headers });
    }

    return next.handle(newReq);
  }

  private isApiPublic(url: string): boolean {
    let retorno = false;
    const apiEncontrada = CONST_API_PUBLICAS.find(element => url.includes(element));
    if (apiEncontrada) {
      retorno = true;
    }
    return retorno;
  }


}
