package co.seg.mercadolibre.services;

import co.seg.mercadolibre.entity.Traceo;
import co.seg.mercadolibre.model.InfoCountryResponse;
import co.seg.mercadolibre.resources.TraceoException;

/**
 * Servicio para obtener informacion de distancia
 * 
 * @author Carlos Gomez
 *
 */
public interface DistanceService {
	
	/**
	 * calculo de distancia manual usando Haversine formula
	 * 
	 * @return retorna distancia calculada
	 */
	public Double getManualDistance(InfoCountryResponse infoCountry) throws TraceoException;
	
	/**
	 * registrar distancias en redis y redirigir la actualziacion de mensajes al
	 * websocket y conecciones activas
	 */
	public void generateDistancias(Traceo traceo) throws TraceoException;

}
