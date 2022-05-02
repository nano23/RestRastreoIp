package co.seg.mercadolibre.service;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import co.seg.mercadolibre.implementation.IpCountryServiceImpl;
import co.seg.mercadolibre.model.IpCountryResponse;
import reactor.core.publisher.Mono;

@ExtendWith(MockitoExtension.class)
class IpCountryServiceTest {
	
	@InjectMocks
	private IpCountryServiceImpl ipCountryService;
	
	private String ip;

	@BeforeEach
	void setUp() {

		ip = "190.96.207.70";
	}

	@Test
    void debeFallar()  {
          assertThrows(
        		  Exception.class,
                  () -> ipCountryService.getCountryByIp(ip)
           );
    }
	
	@Test
    void debeRetornarObjeto() throws Exception  {
		
		ReflectionTestUtils.setField(ipCountryService, "ipCountryServiceUri", "http://api.ipapi.com/");
		
		ReflectionTestUtils.setField(ipCountryService, "ipCountryServiceApiKey", "92a132b7deac904f941338d041145e4c");
    	

    	Mono<IpCountryResponse> expected = ipCountryService.getCountryByIp(ip);

        assertNotNull(expected.block());
    }

}
