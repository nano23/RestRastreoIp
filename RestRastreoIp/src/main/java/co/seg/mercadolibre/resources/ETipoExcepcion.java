package co.seg.mercadolibre.resources;

import lombok.Getter;

/**
 * enum para el control de tipo de excepciones
 * @author Carlos Gomez
 *
 */
@Getter
public enum ETipoExcepcion {
	
	GENERATE_DISTANCIAS(1, "Error generando distancias"),
	CALCULATE_DISTANCIAS(2, "Error calculando distancias"),
	GETINVOCACIONES(3, "Error generando invocaciones"), 
	DISTANCIAPROMEDIO(4, "Error asignando distancia promedio"), 
	PAISCERCANA(5, "Error asignando pais cercano"), 
	DISTANCIACERCANA(6, "Error asignando distancia cercana"), 
	PAISLEJANA(7, "Error asignando pais lejano"), 
	DISTANCIALEJANA(8, "Error asignando distancia lejana"), 
	GETCOUNTRYBYIP(9, "Error consultando servicios de informacion de paises"), 
	IPCOUNTRY_GETCOUNTRYBYIP(10, "Error consultando servicio de ip country"), 
	USERCOUNTRY_GETCOUNTRYBYIP(11, "Error consultando servicio de user country"), 
	GETMONEDADOLAR(12, "Error obteniendo la moneda a dolares"), 
	GENERARHORA(13, "Error generando hora");
	
	
	private Integer id;
	private String mensaje;
	
    private ETipoExcepcion(Integer id, String mensaje) {
        this.id = id;
        this.mensaje = mensaje;
    }



}
