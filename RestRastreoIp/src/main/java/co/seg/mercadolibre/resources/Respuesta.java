package co.seg.mercadolibre.resources;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Getter;

/**
 * objeto para respuesta de servicios rest expuestos generica
 * 
 * @author Carlos Gomez
 *
 */
@Getter
public class Respuesta<T> {
	@JsonInclude(Include.NON_NULL)
	private Object msgerror;
	@JsonInclude(Include.NON_NULL)
	private T data;

	public Respuesta(String message) {
		this.msgerror = message;
	}

	public Respuesta(T objeto) {
		this.data = objeto;
	}

}
