package co.seg.mercadolibre.service;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import co.seg.mercadolibre.implementation.UserCountryServiceImpl;
import co.seg.mercadolibre.model.UserCountryResponse;
import reactor.core.publisher.Mono;

@ExtendWith(MockitoExtension.class)
class UserCountryServiceTest {

	
	@InjectMocks
	private UserCountryServiceImpl userCountryService;
	
	private String ip;

	@BeforeEach
	void setUp() {

		ip = "190.96.207.70";
	}

	@Test
    void debeFallar()  {
          assertThrows(
        		  Exception.class,
                  () -> userCountryService.getCountryByIp(ip)
           );
    }
	
	@Test
    void debeRetornarObjeto() throws Exception  {
		
		ReflectionTestUtils.setField(userCountryService, "userCountryServiceUri", "http://usercountry.agum.com/v1.0/json/");
		
		ReflectionTestUtils.setField(userCountryService, "userCountryServiceApiKey", "6d12311124f62da02d500edc253da9c7b8322d41b18f1cb7");
    	

    	Mono<UserCountryResponse> expected = userCountryService.getCountryByIp(ip);

        assertNotNull(expected.block());
    }
	
}
