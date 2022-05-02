package co.seg.mercadolibre.rest;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import co.seg.mercadolibre.resources.TraceoException;
import lombok.extern.slf4j.Slf4j;

/**
 * clase generica para controladores Rest
 */
@Slf4j
public class GenericRest {

	@Autowired
	protected ModelMapper modelMapper;

	/**
	 * metodo para guardar informacion en el log de la aplicacion
	 * 
	 * @param clase   nombre de la clase
	 * @param metodo  nombre del metodo
	 * @param mensaje mensaje a insertar en el log
	 */
	protected void addLog(StackTraceElement[] stackTrace, String mensaje) {
		if (stackTrace != null && stackTrace.length > 0)
			log.error("clase: " + stackTrace[0].getClassName() + "Metodo: " + stackTrace[0].getMethodName()
					+ "Mensaje: " + mensaje);
	}

	/**
	 * construir mensaje de error
	 * @param e excepcion
	 * @return cadena de respuesta
	 */
	protected String getErrorMensaje(Exception e) {
		String error = "";
		if (e instanceof TraceoException)
			error = ((TraceoException) e).getTipoExcepcion().getMensaje();
		else
			error = (e.getLocalizedMessage() + " - " + e.getMessage());

		return error;
	}



}
