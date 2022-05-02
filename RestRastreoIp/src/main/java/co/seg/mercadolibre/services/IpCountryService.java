package co.seg.mercadolibre.services;

import co.seg.mercadolibre.model.IpCountryResponse;
import co.seg.mercadolibre.resources.TraceoException;
import reactor.core.publisher.Mono;

/**
 * Servicio para obtener informacion de paises
 * 
 * @author Carlos Gomez
 *
 */
public interface IpCountryService {
	
	/**
	 * consulta por ip la informacion de un pais utilizando un servicio en linea
	 * 
	 * @return retorna objeto con la informacion del pais
	 */
	public Mono<IpCountryResponse> getCountryByIp(String ip) throws TraceoException;

}
