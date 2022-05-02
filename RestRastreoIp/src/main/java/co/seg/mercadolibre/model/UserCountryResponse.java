package co.seg.mercadolibre.model;

import java.io.Serializable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * objeto para respuesta de servicio de user country
 * 
 * @author Carlos Gomez
 *
 */
@Getter
@Setter
@NoArgsConstructor
public class UserCountryResponse  implements Serializable {
	
	/**
	 * serial version
	 */
	private static final long serialVersionUID = 1L;
	
	private UserCountryCurrency currency;
	private UserCountryTimezone timezone;

}
