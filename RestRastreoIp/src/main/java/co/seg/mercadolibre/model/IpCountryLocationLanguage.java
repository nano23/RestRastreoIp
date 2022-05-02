package co.seg.mercadolibre.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * objeto para respuesta de servicio de ip country
 * 
 * @author Carlos Gomez
 *
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class IpCountryLocationLanguage implements Serializable {
	
	/**
	 * serial version
	 */
	private static final long serialVersionUID = 1L;
	
	private String code;
	private String name;
	@JsonProperty("native")
	private String nativeValue;

}
