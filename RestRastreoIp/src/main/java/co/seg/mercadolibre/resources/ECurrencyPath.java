package co.seg.mercadolibre.resources;

/**
 * listo de paths para el servicio de currency
 * @author Usuario
 *
 */
public enum ECurrencyPath {
	
	LATEST("/fixer/latest");

	private String path;

    private ECurrencyPath(String path) {
        this.path =  path;
    }
    
    public String getPath() {
        return path;
    }
    
}
