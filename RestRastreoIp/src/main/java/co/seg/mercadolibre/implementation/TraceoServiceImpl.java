package co.seg.mercadolibre.implementation;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import co.seg.mercadolibre.entity.Traceo;
import co.seg.mercadolibre.model.InfoCountryResponse;
import co.seg.mercadolibre.model.IpCountryLocationLanguage;
import co.seg.mercadolibre.model.UserCountryTimezone;
import co.seg.mercadolibre.resources.ETipoExcepcion;
import co.seg.mercadolibre.resources.TraceoException;
import co.seg.mercadolibre.services.CurrencyService;
import co.seg.mercadolibre.services.DistanceService;
import co.seg.mercadolibre.services.InfoCountryService;
import co.seg.mercadolibre.services.TraceoService;

/**
 * Servicio para obtener y registrar informacion de traceo
 * 
 * @author Carlos Gomez
 *
 */
@Service(value = "traceoService")
public class TraceoServiceImpl implements TraceoService {

	/**
	 * HASH_KEY
	 */
	private static final String HASH_KEY = "TRACEO_LIST";
	private static final String UTC_CONSTANTE = "UTC";

	@Value("${internal.info.currency.base}")
	String codeDolar;

	/**
	 * Redis template
	 */
	@Autowired
	RedisTemplate<String, Object> redisTemplate;

	@Autowired
	@Qualifier("infoCountryService")
	private InfoCountryService infoCountryService;

	@Autowired
	@Qualifier("distanceService")
	private DistanceService distanceService;

	@Autowired
	@Qualifier("currencyService")
	private CurrencyService currencyService;

	/**
	 * generar registro de traceos generados por la aplicacion
	 * 
	 * @param ip direccion ip
	 * @return Traceo objeto de traceo
	 */
	@Override
	public Traceo generateTraceo(String ip) throws Exception {
		Traceo traceo = new Traceo();
		// servicio para consultar informacion de pais
		InfoCountryResponse infoCountry = infoCountryService.getCountryByIp(ip);

		// servicio para calcular la distancia
		if (infoCountry != null && infoCountry.getIpCountryResponse() != null
				&& infoCountry.getIpCountryResponse().getLatitude() != null
				&& infoCountry.getIpCountryResponse().getLongitude() != null)
			traceo.setDistanciaDouble(distanceService.getManualDistance(infoCountry));

		// setear campos adicionales
		if (infoCountry.getUserCountryResponse() != null && infoCountry.getUserCountryResponse().getTimezone() != null)
			traceo.setHora(this.generarHora(infoCountry.getUserCountryResponse().getTimezone()));

		if (infoCountry.getIpCountryResponse() != null && infoCountry.getIpCountryResponse().getLocation() != null
				&& infoCountry.getIpCountryResponse().getLocation().getLanguages() != null
				&& !infoCountry.getIpCountryResponse().getLocation().getLanguages().isEmpty())
			traceo.setIdiomas(this.getLenguajes(infoCountry.getIpCountryResponse().getLocation().getLanguages()));

		traceo.setIp(ip);

		if (infoCountry.getIpCountryResponse() != null && infoCountry.getIpCountryResponse().getCountryCode() != null)
			traceo.setIsoCode(infoCountry.getIpCountryResponse().getCountryCode());

		if (infoCountry.getUserCountryResponse() != null && infoCountry.getUserCountryResponse().getCurrency() != null
				&& infoCountry.getUserCountryResponse().getCurrency().getCode() != null)
			traceo.setMonedaEnDolar(this.getMonedaDolar(infoCountry.getUserCountryResponse().getCurrency().getCode()));

		if (infoCountry.getUserCountryResponse() != null && infoCountry.getUserCountryResponse().getCurrency() != null
				&& infoCountry.getUserCountryResponse().getCurrency().getName() != null)
			traceo.setMonedaNombre(infoCountry.getUserCountryResponse().getCurrency().getName());

		if (infoCountry.getIpCountryResponse() != null && infoCountry.getIpCountryResponse().getCountryName() != null)
			traceo.setPais(infoCountry.getIpCountryResponse().getCountryName());

		// guardar en redis
		redisTemplate.opsForHash().put(HASH_KEY, String.valueOf(traceo.getIp()), traceo);

		// calcular e informar
		distanceService.generateDistancias(traceo);

		return traceo;
	}

	/**
	 * usa el servicio cacheados de monedas para convertir el valor a dolares
	 * teniendo encuenta que la base es EUROS
	 * 
	 * @param code
	 * @return
	 * @throws TraceoException
	 */
	private String getMonedaDolar(String code) throws TraceoException {
		String moneda = null;
		try {
			double valorX = 0;
			double valorDolar = 0;
			for (Entry<String, Double> entry : currencyService.getAll().getRates().entrySet()) {
				if (entry.getKey().equals(code)) {
					valorX = entry.getValue();
				}

				if (entry.getKey().equals(codeDolar)) {
					valorDolar = entry.getValue();
				}

				if (valorX > 0 && valorDolar > 0)
					break;
			}

			moneda = (valorX > 0 && valorDolar > 0) ? (String.format("%.2f", (valorX / valorDolar)) + " " + codeDolar)
					: "";
		} catch (Exception e) {
			throw new TraceoException(ETipoExcepcion.GETMONEDADOLAR);
		}

		return moneda;
	}

	/**
	 * generar formato de hora necesario
	 * 
	 * @param timezone
	 * @return cadena de hora
	 */
	private String generarHora(UserCountryTimezone timezone) throws TraceoException {
		String hora = "";
		try {
			SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
			SimpleDateFormat formatoHora = new SimpleDateFormat("HH:mm:ss");
			if (timezone.getCurrentTime() != null)
				hora = formatoHora.format(formato.parse(timezone.getCurrentTime()));

			String offCalc = calculateOffset(timezone.getGmtOffset());
			if (offCalc != null)
				hora += " (" + UTC_CONSTANTE + " " + offCalc + ")";
		} catch (Exception e) {
			throw new TraceoException(ETipoExcepcion.GENERARHORA);
		}

		return hora;
	}

	/**
	 * obtener lista de lenguajes
	 * 
	 * @param languages
	 * @return lista lenguajes
	 */
	private List<String> getLenguajes(List<IpCountryLocationLanguage> languages) {
		List<String> lenguajes = new ArrayList<>();
		for (IpCountryLocationLanguage ipCountryLocationLanguage : languages) {
			lenguajes.add(ipCountryLocationLanguage.getName() + "(" + ipCountryLocationLanguage.getCode() + ")");
		}
		return lenguajes;
	}

	/**
	 * conversion a UTC
	 * 
	 * @param rawOffset entero offset
	 * @return cadena utc
	 */
	private String calculateOffset(int rawOffset) {
		if (rawOffset == 0) {
			return "+00:00";
		}
		long hours = TimeUnit.MILLISECONDS.toHours(rawOffset);
		long minutes = TimeUnit.MILLISECONDS.toMinutes(rawOffset);
		minutes = Math.abs(minutes - TimeUnit.HOURS.toMinutes(hours));

		return String.format("%+03d:%02d", hours, Math.abs(minutes));
	}

}
