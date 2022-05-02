package co.seg.mercadolibre.resources;

/**
 * objeto para el manejo de excepciones controladas en la aplicacion
 * 
 * @author Carlos Gomez
 *
 */
public class TraceoException extends Exception {

	private static final long serialVersionUID = 1L;
	private final ETipoExcepcion tipoExcepcion;

	public ETipoExcepcion getTipoExcepcion() {
		return tipoExcepcion;
	}

	public TraceoException(ETipoExcepcion tipoExcepcion) {
		super();
		this.tipoExcepcion = tipoExcepcion;
	}

}
