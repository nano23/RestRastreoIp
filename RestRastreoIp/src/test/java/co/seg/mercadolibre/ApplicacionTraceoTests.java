package co.seg.mercadolibre;

import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * pruebas de clase principal
 * @author Carlos Gomez
 *
 */
@SpringBootTest
class ApplicacionTraceoTests {

	@Test
	void contextLoads() {

		Boolean ok = false;
		ApplicationTraceo.main(new String[] {});
		ok = true;

		assertTrue(ok);
	}

}
