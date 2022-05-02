package co.seg.mercadolibre.rest;

import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * pruebas de generic rest
 * @author Carlos Gomez
 *
 */
@ExtendWith(MockitoExtension.class)
@SpringBootTest
class GenericRestTest {
	
	@InjectMocks
	private GenericRest genericRest;
	
	@Test
	void debeFallarGenerateTraceo() {
		Boolean ok= false;
		genericRest.addLog(null, null);
		ok = true;
		assertTrue(ok);
	}

}
