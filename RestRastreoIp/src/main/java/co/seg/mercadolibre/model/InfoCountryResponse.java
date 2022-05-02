package co.seg.mercadolibre.model;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * objeto para respuesta de servicio de informacion de pais
 * 
 * @author Carlos Gomez
 *
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class InfoCountryResponse implements Serializable {

	/**
	*  serial version
	*/
	private static final long serialVersionUID = 1L;
	
	private UserCountryResponse userCountryResponse;
	
	private IpCountryResponse ipCountryResponse;
	

}
