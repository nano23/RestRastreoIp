package co.seg.mercadolibre.entity;

import java.beans.Transient;
import java.io.Serializable;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entidad para almacenar la información de distancia - redis
 * @author Carlos Gomez
 *
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Distancia extends Generic implements Serializable {
	
	/**
	 * serial version
	 */
	private static final long serialVersionUID = 1L;
	
	private Double lejana;
	private String paisLejana;
	private Double cercana;
	private String paisCercana;
	private Double promedio;
	private List<Invocacion> invocaciones;
	
	@Transient
	public String getDistanciaLejana() {
		return this.lejana.toString() + " " +  KMS;
	}
	
	@Transient
	public String getDistanciaCercana() {
		return this.cercana.toString() + " " +  KMS;
	}
	
	@Transient
	public String getDistanciaPromedio() {
		return this.promedio.toString() + " " +  KMS;
	}
	
}
