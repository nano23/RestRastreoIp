package co.seg.mercadolibre.entity;

import lombok.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Entidad para almacenar la información de traceo - redis
 * @author Carlos Gomez
 *
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(value = { "distanciaDouble", "monedaEnDolar" , "monedaNombre" })
public class Traceo extends Generic implements Serializable {

	/**
	 * serial version
	 */
	private static final long serialVersionUID = 1L;

	private String ip;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss")
	private Date fechaActual = new Date();

	private String pais;

	private String isoCode;

	private List<String> idiomas;

	private String monedaEnDolar;

	private String monedaNombre;

	private String hora;

	private Double distanciaDouble = 0d;

	public String getDistanciaEstimada() {
		return distanciaDouble > 0 ? String.format("%.0f", distanciaDouble) + " " + KMS : NO_ARGUMENTS;
	}

	public String getMoneda() {
		return monedaNombre != null && monedaEnDolar != null
				? (monedaNombre + " " + "(1 " + monedaNombre + " = " + monedaEnDolar + ")")
				: NO_ARGUMENTS;
	}

}
