import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { environment } from "src/environments/environment";

/**
 * servicio para procesar información de traceo de ip
 */
@Injectable({
  providedIn: 'root',
})
export class TraceoService {

  private API_PATH_OPCION = environment.API_PATH_TRACEO + "/";

  private PATH_TRACEO = "traceo";

constructor(private http: HttpClient) {}

tracearIp(ip: string) {
  return this.http.get(this.API_PATH_OPCION + this.PATH_TRACEO + '/' + ip);
}


}
