package co.seg.mercadolibre.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import co.seg.mercadolibre.entity.Traceo;
import co.seg.mercadolibre.resources.Respuesta;
import co.seg.mercadolibre.services.TraceoService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import javax.servlet.http.HttpServletResponse;

/**
 * Servicio para generar operaciones de traceo
 * 
 * @author Carlos Gomez
 *
 */
@RestController
@RequestMapping("/rest/v1/traceo")
public class TraceoRest extends GenericRest {

	@Autowired
	private TraceoService traceoService;

	/**
	 * funcion que permite por medio de una direccion ip de consulta generar
	 * informacion del pais al quie pertenece
	 * 
	 * @param ip direccion a consultar
	 * @return objeto tipo traceo con la informacion del pais
	 */
	@ApiOperation(value = "direccion ip a consulta", notes = "carga información de paises usando la direccion ip como parametro")
	@GetMapping(value = "/{ip}")
	public ResponseEntity<Respuesta<Traceo>> get(@ApiParam(hidden = true) HttpServletResponse response,
			@PathVariable(value = "ip") String ip) {
		HttpStatus httpStatus = null;
		Respuesta<Traceo> resultado = null;

		try {
			httpStatus = HttpStatus.OK;
			resultado = new Respuesta<>(traceoService.generateTraceo(ip));
		} catch (Exception e) {
			httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
			String mensaje = this.getErrorMensaje(e);
			resultado = new Respuesta<>(mensaje);
			this.addLog(e.getStackTrace(), mensaje);
		}

		response.setHeader("Access-Control-Allow-Origin", "*");
		return new ResponseEntity<>(resultado, httpStatus);
	}

}
