package co.seg.mercadolibre.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import co.seg.mercadolibre.model.InfoCountryResponse;
import co.seg.mercadolibre.resources.ETipoExcepcion;
import co.seg.mercadolibre.resources.TraceoException;
import co.seg.mercadolibre.services.InfoCountryService;
import co.seg.mercadolibre.services.IpCountryService;
import co.seg.mercadolibre.services.UserCountryService;

import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;
import reactor.core.publisher.Mono;

/**
 * Servicio para obtener toda la informacion de paises
 * 
 * @author Carlos Gomez
 *
 */
@Service(value = "infoCountryService")
public class InfoCountryServiceImpl implements InfoCountryService {

	@Autowired
	@Qualifier("ipCountryService")
	private IpCountryService ipCountryService;

	@Autowired
	@Qualifier("userCountry")
	private UserCountryService userCountryService;

	/**
	 * consulta de forma simultanea dos servicios que entregan informacion de paises
	 * 
	 * @return InfoCountryResponse objeto respuesta
	 */
	@Override
	public InfoCountryResponse getCountryByIp(String ip) throws TraceoException {
		try {
			Scheduler scheduler = Schedulers.boundedElastic();
			return Mono
					.zip(userCountryService.getCountryByIp(ip).subscribeOn(scheduler),
							ipCountryService.getCountryByIp(ip).subscribeOn(scheduler), InfoCountryResponse::new)
					.block();
		} catch (Exception e) {
			throw new TraceoException(ETipoExcepcion.GETCOUNTRYBYIP);
		}

	}

}
