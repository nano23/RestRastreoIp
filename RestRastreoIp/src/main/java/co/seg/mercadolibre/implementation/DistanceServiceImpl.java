package co.seg.mercadolibre.implementation;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import co.seg.mercadolibre.entity.Distancia;
import co.seg.mercadolibre.entity.Invocacion;
import co.seg.mercadolibre.entity.Traceo;
import co.seg.mercadolibre.model.InfoCountryResponse;
import co.seg.mercadolibre.resources.ETipoExcepcion;
import co.seg.mercadolibre.resources.TraceoException;
import co.seg.mercadolibre.services.DistanceService;
import co.seg.mercadolibre.socket.WebSocketController;

/**
 * Servicio para obtener informacion de distancia
 * 
 * @author Carlos Gomez
 *
 */
@Service(value = "distanceService")
public class DistanceServiceImpl implements DistanceService {

	/**
	 * HASH_KEY
	 */
	private static final String HASH_KEY = "DISTANCIA_LIST";
	private static final String KEY = "DISTANCIA";

	/**
	 * Redis template
	 */
	@Autowired
	RedisTemplate<String, Object> redisTemplate;

	@Autowired
	WebSocketController webSocket;

	@Value("${internal.info.geo.destino.lat}")
	Double infoGeoDestinoLat;

	@Value("${internal.info.geo.destino.lng}")
	Double infoGeoDestinoLng;

	/**
	 * calculo de distancia manual usando Haversine formula
	 * 
	 * @return retorna distancia calculada
	 */
	@Override
	public Double getManualDistance(InfoCountryResponse infoCountry) throws TraceoException {
		final int RADIUS_EARTH = 6371;
		try {

			double dLat = getRad(infoGeoDestinoLat - infoCountry.getIpCountryResponse().getLatitude());
			double dLong = getRad(infoGeoDestinoLng - infoCountry.getIpCountryResponse().getLongitude());

			double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
					+ Math.cos(getRad(infoCountry.getIpCountryResponse().getLatitude()))
							* Math.cos(getRad(infoGeoDestinoLat)) * Math.sin(dLong / 2) * Math.sin(dLong / 2);
			double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
			return (RADIUS_EARTH * c);
		} catch (Exception e) {
			throw new TraceoException(ETipoExcepcion.CALCULATE_DISTANCIAS);
		}
	}

	/**
	 * divide a la mitad el valor teniendo en cuenta una referencia en %
	 * 
	 * @param x valor
	 * @return calculo retornado
	 */
	private Double getRad(Double x) {
		return x * Math.PI / 180;
	}

	/**
	 * registrar distancias en redis y redirigir la actualziacion de mensajes al
	 * websocket y conecciones activas
	 */
	@Override
	public void generateDistancias(Traceo traceo) throws TraceoException {

		try {
			Distancia distanciaSaved = (Distancia) redisTemplate.opsForHash().get(HASH_KEY, KEY);

			Distancia distancia = new Distancia(getDistanciaLejana(distanciaSaved, traceo),
					getPaisLejana(distanciaSaved, traceo), getDistanciaCercana(distanciaSaved, traceo),
					getPaisCercana(distanciaSaved, traceo), getDistanciaPromedio(distanciaSaved, traceo),
					getInvocaciones(distanciaSaved, traceo));

			// guardar en redis
			redisTemplate.opsForHash().put(HASH_KEY, KEY, distancia);

			webSocket.sendMessage(JSON.toJSONString(distancia));

		} catch (Exception e) {
			throw new TraceoException(ETipoExcepcion.GENERATE_DISTANCIAS);
		}

	}

	/**
	 * genera listado de invocaciones
	 * 
	 * @param distanciaSaved objeto con las distancias anteriores
	 * @param traceo         nuevo traceo
	 * @return nuevo listado de invocaciones
	 * @throws TraceoException
	 */
	private List<Invocacion> getInvocaciones(Distancia distanciaSaved, Traceo traceo) throws TraceoException {
		List<Invocacion> invocaciones = null;
		try {
			Boolean entro = false;
			if (distanciaSaved != null) {
				invocaciones = distanciaSaved.getInvocaciones();
				for (Invocacion inv : invocaciones) {
					if (inv.getPais().equals(traceo.getPais())) {
						inv.setConteo(inv.getConteo() + 1);
						entro = true;
						break;
					}
				}
				if (Boolean.FALSE.equals(entro))
					invocaciones.add(new Invocacion(traceo.getPais(), traceo.getDistanciaDouble().intValue(), 1));
			} else {
				invocaciones = new ArrayList<>();
				invocaciones.add(new Invocacion(traceo.getPais(), traceo.getDistanciaDouble().intValue(), 1));
			}
		} catch (Exception e) {
			throw new TraceoException(ETipoExcepcion.GETINVOCACIONES);
		}

		return invocaciones;
	}

	/**
	 * genera distancia promedio
	 * 
	 * @param distanciaSaved objeto con las distancias anteriores
	 * @param traceo         nuevo traceo
	 * @return valor calculado
	 * @throws TraceoException
	 */
	private Double getDistanciaPromedio(Distancia distanciaSaved, Traceo traceo) throws TraceoException {
		double promedio = 0;
		double sumInvocacion = 0;
		int conteo = 0;

		try {
			if (distanciaSaved != null) {
				for (Invocacion inv : distanciaSaved.getInvocaciones()) {
					sumInvocacion += (inv.getDistancia() * inv.getConteo());
					conteo++;
				}
				if (conteo > 0)
					promedio = (sumInvocacion / conteo);
			} else
				promedio = traceo.getDistanciaDouble();
		} catch (Exception e) {
			throw new TraceoException(ETipoExcepcion.DISTANCIAPROMEDIO);
		}

		return promedio;
	}

	/**
	 * genera pais cercano
	 * 
	 * @param distanciaSaved objeto con las distancias anteriores
	 * @param traceo         nuevo traceo
	 * @return valor calculado
	 * @throws TraceoException
	 */
	private String getPaisCercana(Distancia distanciaSaved, Traceo traceo) throws TraceoException {
		String pais = "";
		try {
			if (distanciaSaved != null) {
				pais = traceo.getDistanciaDouble() < distanciaSaved.getCercana() ? traceo.getPais()
						: distanciaSaved.getPaisCercana();
			} else
				pais = traceo.getPais();

		} catch (Exception e) {
			throw new TraceoException(ETipoExcepcion.PAISCERCANA);
		}
		return pais;
	}

	/**
	 * genera distancia cercana
	 * 
	 * @param distanciaSaved objeto con las distancias anteriores
	 * @param traceo         nuevo traceo
	 * @return valor calculado
	 * @throws TraceoException
	 */
	private Double getDistanciaCercana(Distancia distanciaSaved, Traceo traceo) throws TraceoException {
		double distancia = 0;
		try {
			if (distanciaSaved != null) {
				distancia = traceo.getDistanciaDouble() < distanciaSaved.getCercana() ? traceo.getDistanciaDouble()
						: distanciaSaved.getCercana();
			} else
				distancia = traceo.getDistanciaDouble();

		} catch (Exception e) {
			throw new TraceoException(ETipoExcepcion.DISTANCIACERCANA);
		}
		return distancia;
	}

	/**
	 * genera pais lejano
	 * 
	 * @param distanciaSaved objeto con las distancias anteriores
	 * @param traceo         nuevo traceo
	 * @return valor calculado
	 * @throws TraceoException
	 */
	private String getPaisLejana(Distancia distanciaSaved, Traceo traceo) throws TraceoException {
		String pais = "";
		try {
			if (distanciaSaved != null) {
				pais = traceo.getDistanciaDouble() > distanciaSaved.getLejana() ? traceo.getPais()
						: distanciaSaved.getPaisLejana();
			} else
				pais = traceo.getPais();

		} catch (Exception e) {
			throw new TraceoException(ETipoExcepcion.PAISLEJANA);
		}
		return pais;
	}

	/**
	 * genera distancia lejana
	 * 
	 * @param distanciaSaved objeto con las distancias anteriores
	 * @param traceo         nuevo traceo
	 * @return valor calculado
	 * @throws TraceoException
	 */
	private Double getDistanciaLejana(Distancia distanciaSaved, Traceo traceo) throws TraceoException {
		double distancia = 0;
		try {
			if (distanciaSaved != null) {
				distancia = traceo.getDistanciaDouble() > distanciaSaved.getLejana() ? traceo.getDistanciaDouble()
						: distanciaSaved.getLejana();
			} else
				distancia = traceo.getDistanciaDouble();

		} catch (Exception e) {
			throw new TraceoException(ETipoExcepcion.DISTANCIALEJANA);
		}
		return distancia;
	}

}
