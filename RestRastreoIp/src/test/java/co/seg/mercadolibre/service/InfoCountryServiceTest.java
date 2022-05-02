package co.seg.mercadolibre.service;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import co.seg.mercadolibre.implementation.InfoCountryServiceImpl;
import co.seg.mercadolibre.model.InfoCountryResponse;
import co.seg.mercadolibre.model.IpCountryResponse;
import co.seg.mercadolibre.model.UserCountryResponse;
import co.seg.mercadolibre.resources.TraceoException;
import co.seg.mercadolibre.services.IpCountryService;
import co.seg.mercadolibre.services.UserCountryService;
import reactor.core.publisher.Mono;

@ExtendWith(MockitoExtension.class)
@SpringBootTest 
class InfoCountryServiceTest {
	
	@MockBean
	private IpCountryService ipCountryService;
	
	@MockBean
	private UserCountryService userCountryService;
	
	@InjectMocks
	private InfoCountryServiceImpl infoCountryService;
	
	private String ip;

	@BeforeEach
	void setUp() {

		ip = "190.96.207.70";
	}

	@Test
    void debeFallar()  {
          assertThrows(
        		  TraceoException.class,
                  () -> infoCountryService.getCountryByIp(ip)
           );
    }
	
	@Test
    void debeRetornarObjeto() throws Exception  {
		
		given(ipCountryService.getCountryByIp(anyString())).willReturn(Mono.just(new IpCountryResponse()));
		
		given(userCountryService.getCountryByIp(anyString())).willReturn(Mono.just(new UserCountryResponse()));

    	InfoCountryResponse expected = infoCountryService.getCountryByIp(ip);

        assertNotNull(expected);
    }

}
