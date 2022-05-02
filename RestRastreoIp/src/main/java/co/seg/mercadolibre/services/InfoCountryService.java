package co.seg.mercadolibre.services;

import co.seg.mercadolibre.model.InfoCountryResponse;
import co.seg.mercadolibre.resources.TraceoException;

/**
 * Servicio para obtener toda la informacion de paises
 * 
 * @author Carlos Gomez
 *
 */
public interface InfoCountryService {
	
	/**
	 * consulta de forma simultanea dos servicios que entregan informacion de paises
	 * 
	 * @return InfoCountryResponse objeto respuesta
	 */
	public InfoCountryResponse getCountryByIp(String ip) throws TraceoException;

}
