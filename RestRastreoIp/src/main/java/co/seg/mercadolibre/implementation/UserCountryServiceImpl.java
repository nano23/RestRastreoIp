package co.seg.mercadolibre.implementation;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import co.seg.mercadolibre.model.UserCountryResponse;
import co.seg.mercadolibre.resources.ETipoExcepcion;
import co.seg.mercadolibre.resources.TraceoException;
import co.seg.mercadolibre.services.UserCountryService;
import reactor.core.publisher.Mono;

/**
 * Servicio para obtener informacion de paises
 * 
 * @author Carlos Gomez
 *
 */
@Service(value = "userCountry")
public class UserCountryServiceImpl implements UserCountryService {

	@Value("${external.services.userCountry.apikey}")
	String userCountryServiceApiKey;

	@Value("${external.services.userCountry.url}")
	String userCountryServiceUri;

	/**
	 * consulta por ip la informacion de un pais utilizando un servicio en linea
	 * 
	 * @return retorna objeto con la informacion del pais
	 */
	public Mono<UserCountryResponse> getCountryByIp(String ip) throws TraceoException {
		try {
			WebClient webClient = WebClient.create(this.getUriService());
			return webClient.get()
					.uri(uriBuilder -> uriBuilder.path(ip).queryParam("token", userCountryServiceApiKey).build())
					.retrieve().bodyToMono(UserCountryResponse.class);
		} catch (Exception e) {
			throw new TraceoException(ETipoExcepcion.USERCOUNTRY_GETCOUNTRYBYIP);
		}
	}

	/**
	 * genera ruta principal del servicio de monedas
	 * 
	 * @return
	 */
	private String getUriService() {
		return UriComponentsBuilder.fromHttpUrl(userCountryServiceUri).build().toUriString();
	}

}
