package co.seg.mercadolibre.services;

import co.seg.mercadolibre.model.UserCountryResponse;
import co.seg.mercadolibre.resources.TraceoException;
import reactor.core.publisher.Mono;

/**
 * Servicio para obtener informacion de paises
 * 
 * @author Carlos Gomez
 *
 */
public interface UserCountryService {

	/**
	 * consulta por ip la informacion de un pais utilizando un servicio en linea
	 * 
	 * @return retorna objeto con la informacion del pais
	 */
	public Mono<UserCountryResponse> getCountryByIp(String ip) throws TraceoException;
}
