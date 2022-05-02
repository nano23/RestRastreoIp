package co.seg.mercadolibre.service;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.util.ReflectionTestUtils;
import co.seg.mercadolibre.entity.Distancia;
import co.seg.mercadolibre.entity.Traceo;
import co.seg.mercadolibre.implementation.DistanceServiceImpl;
import co.seg.mercadolibre.model.InfoCountryResponse;
import co.seg.mercadolibre.model.IpCountryResponse;
import co.seg.mercadolibre.model.UserCountryResponse;
import co.seg.mercadolibre.resources.TraceoException;
import co.seg.mercadolibre.socket.WebSocketController;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class DistanceServiceTest {

	@InjectMocks
	private DistanceServiceImpl distanceService;

	InfoCountryResponse infoObjeto;

	Traceo traceo;
	
	Distancia distancia;

	@Mock
	WebSocketController webSocket;

	@Mock
	RedisTemplate<String, String> redisTemplate;

	@Mock
	private HashOperations<String, Object, Object> valueOperations;
	
	@BeforeEach
	void setUp() {

		infoObjeto = new InfoCountryResponse(new UserCountryResponse(), new IpCountryResponse());
		infoObjeto.getIpCountryResponse().setLatitude(7.12539d);
		infoObjeto.getIpCountryResponse().setLongitude(-73.1198d);

		traceo = new Traceo();

		distancia = new Distancia();
		distancia.setCercana(1000d);
		distancia.setLejana(2000d);
		distancia.setPromedio(1500d);
		distancia.setInvocaciones(new ArrayList<>());
	}

	@Test
	void debeFallarDistanciaManual() {
		assertThrows(TraceoException.class, () -> distanceService.getManualDistance(new InfoCountryResponse()));
	}

	@Test
	void debeFallargenerateDistancias() {
		assertThrows(TraceoException.class, () -> distanceService.generateDistancias(new Traceo()));
	}

	@Test
	void debeFuncionarDistanciaManual() throws Exception {

		ReflectionTestUtils.setField(distanceService, "infoGeoDestinoLat", -34.61315d);

		ReflectionTestUtils.setField(distanceService, "infoGeoDestinoLng", -58.37723d);

		Double expected = distanceService.getManualDistance(infoObjeto);

		assertTrue(expected > 0);
	}

	@Test
	void debeFuncionarGenerateDistancias() throws TraceoException {

		given(redisTemplate.opsForHash()).willReturn(valueOperations);
		given(redisTemplate.opsForHash().get(anyString(),any())).willReturn(distancia);
		Mockito.doNothing().when(webSocket).sendMessage(anyString());
		
		
		Boolean ok = false;
		distanceService.generateDistancias(traceo);
		ok = true;

		assertTrue(ok);
	}
	
	@Test
	void debeFallarGetDistanciaLejana() throws TraceoException {

		given(redisTemplate.opsForHash()).willReturn(valueOperations);
		given(redisTemplate.opsForHash().get(anyString(),any())).willReturn(null);
		Mockito.doNothing().when(webSocket).sendMessage(anyString());
		
		
		Boolean ok = false;
		distanceService.generateDistancias(traceo);
		ok = true;

		assertTrue(ok);
	}
	
	

}
