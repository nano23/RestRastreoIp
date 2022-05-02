package co.seg.mercadolibre.services;

import co.seg.mercadolibre.model.CurrencyServiceResponse;

/**
 * Servicio para obtener informacion de las monedas
 * 
 * @author Carlos Gomez
 *
 */
public interface CurrencyService {

	/**
	 * consultar servicio de monedas
	 * 
	 * @return retorna objeto con la información de las monedas
	 */
	public CurrencyServiceResponse getAll() throws Exception;

}
