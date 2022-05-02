package co.seg.mercadolibre.implementation;

import co.seg.mercadolibre.model.CurrencyServiceResponse;
import co.seg.mercadolibre.resources.ECurrencyPath;
import co.seg.mercadolibre.services.CurrencyService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.cache.annotation.Cacheable;

/**
 * Servicio para obtener informacion de las monedas
 * 
 * @author Carlos Gomez
 *
 */
@Service(value = "currencyService")
public class CurrencyServiceImpl implements CurrencyService  {
	
	
	@Value("${external.services.taxes.apikey}")
	String taxesServiceApiKey;
	
	@Value("${external.services.taxes.url}")
	String taxesServiceUri;

	/**
	 * consultar servicio de monedas
	 * @return retorna objeto con la información de las monedas
	 */
	@Override
	@Cacheable(value = "CurrencyCache")
	public CurrencyServiceResponse getAll() throws Exception {
		
		WebClient webClient = WebClient.create(this.getUriService());
		return webClient.get()
				.uri(uriBuilder -> uriBuilder
					    .path(ECurrencyPath.LATEST.getPath())
					    .queryParam("access_key", taxesServiceApiKey)
					    .build())
					  .retrieve()
		        .bodyToMono(CurrencyServiceResponse.class).block();
	}
	
	/**
	 * genera ruta principal del servicio de monedas
	 * @return
	 */
	private String getUriService() {
		return UriComponentsBuilder.
				fromHttpUrl(taxesServiceUri).build().toUriString();
	}

}
