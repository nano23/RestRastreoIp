package co.seg.mercadolibre.service;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import co.seg.mercadolibre.implementation.CurrencyServiceImpl;
import co.seg.mercadolibre.model.CurrencyServiceResponse;


@ExtendWith(MockitoExtension.class)
class CurrencyServiceTest {

	@InjectMocks
	private CurrencyServiceImpl currencyService;

	@Test
    void debeFallar()  {
          assertThrows(
        		  Exception.class,
                  () -> currencyService.getAll()
           );
    }
	

	
	@Test
    void debeRetornarObjeto() throws Exception  {
		
		ReflectionTestUtils.setField(currencyService, "taxesServiceUri", "http://data.fixer.io");
		
		ReflectionTestUtils.setField(currencyService, "taxesServiceApiKey", "932f508db423f64299e60a912a6653a3");
    	

    	CurrencyServiceResponse expected = currencyService.getAll();

        assertNotNull(expected);
    }

}
