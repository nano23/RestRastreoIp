package co.seg.mercadolibre.services;

import co.seg.mercadolibre.entity.Traceo;

/**
 * Servicio para obtener y registrar informacion de traceo
 * 
 * @author Carlos Gomez
 *
 */
public interface TraceoService {
	
	/**
	 * generar registro de traceos generados por la aplicacion
	 * 
	 * @param ip direccion ip
	 * @return Traceo objeto de traceo
	 */
	public Traceo generateTraceo(String ip) throws Exception;

}
