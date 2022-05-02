package co.seg.mercadolibre.service;

import static org.junit.Assert.assertNotNull;
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
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import com.google.gson.Gson;
import co.seg.mercadolibre.entity.Traceo;
import co.seg.mercadolibre.implementation.TraceoServiceImpl;
import co.seg.mercadolibre.model.CurrencyServiceResponse;
import co.seg.mercadolibre.model.InfoCountryResponse;
import co.seg.mercadolibre.model.IpCountryLocation;
import co.seg.mercadolibre.model.IpCountryLocationLanguage;
import co.seg.mercadolibre.model.IpCountryResponse;
import co.seg.mercadolibre.model.UserCountryCurrency;
import co.seg.mercadolibre.model.UserCountryResponse;
import co.seg.mercadolibre.model.UserCountryTimezone;
import co.seg.mercadolibre.resources.TraceoException;
import co.seg.mercadolibre.services.CurrencyService;
import co.seg.mercadolibre.services.DistanceService;
import co.seg.mercadolibre.services.InfoCountryService;
import co.seg.mercadolibre.socket.WebSocketController;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class TraceoServiceTest {

	@InjectMocks
	private TraceoServiceImpl traceoService;

	InfoCountryResponse infoObjeto;

	@Mock
	WebSocketController webSocket;

	@Mock
	RedisTemplate<String, String> redisTemplate;

	@Mock
	private HashOperations<String, Object, Object> valueOperations;

	@Mock
	InfoCountryService infoCountryService;

	@Mock
	DistanceService distanceService;

	@Mock
	CurrencyService currencyService;

	String ip;

	@BeforeEach
	void setUp() {

		ip = "190.96.207.70";

		infoObjeto = new InfoCountryResponse(new UserCountryResponse(), new IpCountryResponse());
		infoObjeto.getIpCountryResponse().setLatitude(7.12539d);
		infoObjeto.getIpCountryResponse().setLongitude(-73.1198d);
		infoObjeto.getIpCountryResponse().setLocation(new IpCountryLocation());
		infoObjeto.getIpCountryResponse().getLocation().setLanguages(new ArrayList<>());
		infoObjeto.getIpCountryResponse().getLocation().getLanguages()
				.add(new IpCountryLocationLanguage("co", "co", "co"));
		infoObjeto.getIpCountryResponse().setCountryCode("co");
		infoObjeto.getIpCountryResponse().setCountryName("Colombia");
		infoObjeto.getUserCountryResponse().setTimezone(new UserCountryTimezone());
		infoObjeto.getUserCountryResponse().getTimezone().setCurrentTime("2022-05-01T16:23:50-05:00");
		infoObjeto.getUserCountryResponse().getTimezone().setGmtOffset(7200);
		infoObjeto.getUserCountryResponse().setCurrency(new UserCountryCurrency());
		infoObjeto.getUserCountryResponse().getCurrency().setName("peso");
		infoObjeto.getUserCountryResponse().getCurrency().setCode("COP");
	}

	@Test
	void debeFallarGenerateTraceo() {
		assertThrows(Exception.class, () -> traceoService.generateTraceo(ip));
	}

	@Test
	void debeFuncionarGenerateTraceo() throws Exception {

		given(redisTemplate.opsForHash()).willReturn(valueOperations);
		given(infoCountryService.getCountryByIp(anyString())).willReturn(infoObjeto);
		given(distanceService.getManualDistance(any())).willReturn(1000d);

		given(currencyService.getAll()).willReturn(new Gson().fromJson(
				"{\"date\":\"2022-05-01\",\"success\":true,\"rates\":{\"AED\":3.875049,\"AFN\":90.730358,\"ALL\":121.062165,\"AMD\":478.193008,\"ANG\":1.901329,\"AOA\":427.929644,\"ARS\":121.587915,\"AUD\":1.492298,\"AWG\":1.899542,\"AZN\":1.789485,\"BAM\":1.953086,\"BBD\":2.130107,\"BDT\":91.43095,\"BGN\":1.953929,\"BHD\":0.397622,\"BIF\":2127.950813,\"BMD\":1.055008,\"BND\":1.455555,\"BOB\":7.263596,\"BRL\":5.24629,\"BSD\":1.055018,\"BTC\":2.7433769E-5,\"BTN\":80.607955,\"BWP\":12.772456,\"BYN\":3.55171,\"BYR\":20678.153664,\"BZD\":2.126513,\"CAD\":1.355247,\"CDF\":2123.730826,\"CHF\":1.026359,\"CLF\":0.032595,\"CLP\":899.404805,\"CNY\":6.972017,\"COP\":4177.482894,\"CRC\":698.099254,\"CUC\":1.055008,\"CUP\":27.957708,\"CVE\":110.512053,\"CZK\":24.597485,\"DJF\":187.49593,\"DKK\":7.440727,\"DOP\":58.078363,\"DZD\":152.681072,\"EGP\":19.494824,\"ERN\":15.82512,\"ETB\":54.201027,\"EUR\":1.0,\"FJD\":2.268377,\"FKP\":0.810923,\"GBP\":0.838352,\"GEL\":3.21253,\"GGP\":0.810923,\"GHS\":7.965142,\"GIP\":0.810923,\"GMD\":57.060399,\"GNF\":9278.793966,\"GTQ\":8.081247,\"GYD\":220.759859,\"HKD\":8.278304,\"HNL\":25.863563,\"HRK\":7.568095,\"HTG\":114.994447,\"HUF\":378.207121,\"IDR\":15324.094377,\"ILS\":3.52523,\"IMP\":0.810923,\"INR\":80.73268,\"IQD\":1540.311446,\"IRR\":44574.081294,\"ISK\":137.888354,\"JEP\":0.810923,\"JMD\":163.217699,\"JOD\":0.748314,\"JPY\":137.203239,\"KES\":122.222508,\"KGS\":86.617618,\"KHR\":4270.671639,\"KMF\":491.369479,\"KPW\":949.507182,\"KRW\":1332.986575,\"KWD\":0.323572,\"KYD\":0.879165,\"KZT\":469.315117,\"LAK\":13068.907298,\"LBP\":1596.720074,\"LKR\":369.247651,\"LRD\":159.838042,\"LSL\":16.752921,\"LTL\":3.115164,\"LVL\":0.638164,\"LYD\":5.048238,\"MAD\":10.523693,\"MDL\":19.635115,\"MGA\":4220.031618,\"MKD\":61.658122,\"MMK\":1953.31932,\"MNT\":3159.604154,\"MOP\":8.52742,\"MRO\":376.637617,\"MUR\":45.67545,\"MVR\":16.299755,\"MWK\":858.251229,\"MXN\":21.535094,\"MYR\":4.592975,\"MZN\":67.34113,\"NAD\":16.753383,\"NGN\":438.102652,\"NIO\":37.732311,\"NOK\":9.861477,\"NPR\":128.972809,\"NZD\":1.632187,\"OMR\":0.406106,\"PAB\":1.055018,\"PEN\":4.048594,\"PGK\":3.68398,\"PHP\":55.261835,\"PKR\":195.910896,\"PLN\":4.679677,\"PYG\":7218.259208,\"QAR\":3.841263,\"RON\":4.949342,\"RSD\":117.640548,\"RUB\":75.27491,\"RWF\":1080.328028,\"SAR\":3.957547,\"SBD\":8.477397,\"SCR\":14.193909,\"SDG\":472.118664,\"SEK\":10.353045,\"SGD\":1.457847,\"SHP\":1.45317,\"SLL\":14872.969384,\"SOS\":610.849283,\"SRD\":21.966329,\"STD\":21836.532235,\"SVC\":9.231032,\"SYP\":2650.653911,\"SZL\":16.615851,\"THB\":36.138766,\"TJS\":13.140064,\"TMT\":3.692527,\"TND\":3.220409,\"TOP\":2.439702,\"TRY\":15.683319,\"TTD\":7.169328,\"TWD\":31.071565,\"TZS\":2452.854207,\"UAH\":31.913565,\"UGX\":3750.530952,\"USD\":1.055008,\"UYU\":43.739551,\"UZS\":11789.712741,\"VEF\":2.255925609940166E11,\"VND\":24226.672534,\"VUV\":118.774524,\"WST\":2.727662,\"XAF\":655.036752,\"XAG\":0.046227,\"XAU\":5.56E-4,\"XCD\":2.851212,\"XDR\":0.787394,\"XOF\":654.62719,\"XPF\":119.558774,\"YER\":264.064751,\"ZAR\":16.653276,\"ZMK\":9496.340852,\"ZMW\":17.961267,\"ZWL\":339.712094},\"base\":\"EUR\",\"timestamp\":1651442944}",
				CurrencyServiceResponse.class));

		Traceo traceo = traceoService.generateTraceo(ip);

		assertNotNull(traceo);
		
		assertNotNull(traceo.getMonedaEnDolar());
		
		assertNotNull(traceo.getMonedaNombre());
		
	}
	
	
	@Test
	void debeFuncionarGenerateTraceoSinUserCountry() throws Exception {
		
		InfoCountryResponse infoObjetoObj = new InfoCountryResponse(null, infoObjeto.getIpCountryResponse());

		given(redisTemplate.opsForHash()).willReturn(valueOperations);
		given(infoCountryService.getCountryByIp(anyString())).willReturn(infoObjetoObj);
		given(distanceService.getManualDistance(any())).willReturn(1000d);

		Traceo traceo = traceoService.generateTraceo(ip);

		assertNotNull(traceo.getDistanciaEstimada());
		
		assertNotNull(traceo.getMoneda());
		
		traceo = new Traceo();
		
		assertNotNull(traceo.getDistanciaEstimada());
		
		assertNotNull(traceo.getMoneda());
		
		
	}
	
	
	@Test
	void debeFuncionarGenerateTraceoSinIpCountry() throws Exception {
		
		InfoCountryResponse infoObjetoObj = new InfoCountryResponse(infoObjeto.getUserCountryResponse(), null);
		given(infoCountryService.getCountryByIp(anyString())).willReturn(infoObjetoObj);

		assertThrows(TraceoException.class, () -> traceoService.generateTraceo(ip));
		
	}

}
