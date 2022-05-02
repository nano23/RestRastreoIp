package co.seg.mercadolibre.implementation;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import co.seg.mercadolibre.model.IpCountryResponse;
import co.seg.mercadolibre.resources.ETipoExcepcion;
import co.seg.mercadolibre.resources.TraceoException;
import co.seg.mercadolibre.services.IpCountryService;
import reactor.core.publisher.Mono;

/**
 * Servicio para obtener informacion de paises
 * 
 * @author Carlos Gomez
 *
 */
@Service(value = "ipCountryService")
public class IpCountryServiceImpl implements IpCountryService {

	@Value("${external.services.ipCountry.apikey}")
	String ipCountryServiceApiKey;

	@Value("${external.services.ipCountry.url}")
	String ipCountryServiceUri;

	/**
	 * consulta por ip la informacion de un pais utilizando un servicio en linea
	 * 
	 * @return retorna objeto con la informacion del pais
	 */
	public Mono<IpCountryResponse> getCountryByIp(String ip) throws TraceoException {
		try {
			WebClient webClient = WebClient.create(this.getUriService());
			return webClient.get()
					.uri(uriBuilder -> uriBuilder.path(ip).queryParam("access_key", ipCountryServiceApiKey).build())
					.retrieve().bodyToMono(IpCountryResponse.class);
		} catch (Exception e) {
			throw new TraceoException(ETipoExcepcion.IPCOUNTRY_GETCOUNTRYBYIP);
		}

	}

	/**
	 * genera ruta principal del servicio de monedas
	 * 
	 * @return
	 */
	private String getUriService() {
		return UriComponentsBuilder.fromHttpUrl(ipCountryServiceUri).build().toUriString();
	}

}
