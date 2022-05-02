package co.seg.mercadolibre.model;

import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonProperty;
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
public class IpCountryResponse implements Serializable {
	
	/**
	 * serial version
	 */
	private static final long serialVersionUID = 1L;
	@JsonProperty("country_code")
	private String countryCode;
	@JsonProperty("country_name")
	private String countryName;
	@JsonProperty("region_code")
	private String regionCode;
	private Double latitude;
	private Double longitude;
	private IpCountryLocation location;

}
