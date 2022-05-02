package co.seg.mercadolibre.model;

import java.io.Serializable;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * objeto para respuesta de servicio de ip country
 * 
 * @author Carlos Gomez
 *
 */
@Getter
@Setter
@NoArgsConstructor
public class IpCountryLocation implements Serializable {

	/**
	 * serial version
	 */
	private static final long serialVersionUID = 1L;
	
	private List<IpCountryLocationLanguage> languages = null;

}
