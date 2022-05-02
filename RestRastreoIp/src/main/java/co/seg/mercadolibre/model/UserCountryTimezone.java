package co.seg.mercadolibre.model;

import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonProperty;
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
public class UserCountryTimezone  implements Serializable {
	
	/**
	 * serial version
	 */
	private static final long serialVersionUID = 1L;
	@JsonProperty("current_time")
	private String currentTime;
	@JsonProperty("gmt_offset")
	private Integer gmtOffset;

}
