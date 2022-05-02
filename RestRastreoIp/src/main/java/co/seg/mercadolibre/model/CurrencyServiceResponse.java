package co.seg.mercadolibre.model;

import java.io.Serializable;
import java.util.Map;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * objeto para respuesta de servicio de moneda
 * 
 * @author Carlos Gomez
 *
 */
@Getter
@Setter
@NoArgsConstructor
public class CurrencyServiceResponse implements Serializable {

	/**
	 * serial version
	 */
	private static final long serialVersionUID = 1L;

	private Map<String, Double> rates;

}
