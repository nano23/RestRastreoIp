package co.seg.mercadolibre.entity;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entidad para almacenar la información de invocacion - redis
 * @author Carlos Gomez
 *
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Invocacion extends Generic implements Serializable {
	
	/**
	 * serial version
	 */
	private static final long serialVersionUID = 1L;
	
	private String pais;
	private Integer distancia;
	private Integer conteo;
	

}
