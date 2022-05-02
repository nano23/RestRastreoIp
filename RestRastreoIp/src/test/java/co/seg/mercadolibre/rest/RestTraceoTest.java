package co.seg.mercadolibre.rest;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.zalando.problem.ProblemModule;
import org.zalando.problem.violations.ConstraintViolationProblemModule;
import com.fasterxml.jackson.databind.ObjectMapper;
import co.seg.mercadolibre.entity.Traceo;
import co.seg.mercadolibre.services.TraceoService;

/**
 * pruebas al servicio de traseo
 * @author carlos gomez
 *
 */
@WebMvcTest(controllers = TraceoRest.class)
@ActiveProfiles("test")
class RestTraceoTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private TraceoService service;

	@Autowired
	private ObjectMapper objectMapper;

	private Traceo traceo;

	private String ip;

	@BeforeEach
	void setUp() {


		objectMapper.registerModule(new ProblemModule());
		objectMapper.registerModule(new ConstraintViolationProblemModule());
	}

	@Test
	void debeConsultar() throws Exception {
		
		traceo = new Traceo();
		traceo.setFechaActual(null);
		ip = "190.96.207.70";

		given(service.generateTraceo(anyString())).willReturn(traceo);

		this.mockMvc.perform(get("/rest/v1/traceo/" + ip)).andExpect(status().isOk()).andExpect(content().json(
				"{\"data\":{\"ip\":null,\"fechaActual\":null,\"pais\":null,\"isoCode\":null,\"idiomas\":null,\"hora\":null,\"moneda\":\"NA\"}}"));
	}
	
	@Test
	void debeFallar5xx() throws Exception {
		
		traceo = new Traceo();
		ip = "190.96.207.70";

		given(service.generateTraceo(anyString())).willThrow(new Exception());

		this.mockMvc.perform(get("/rest/v1/traceo/" + ip)).andExpect(status().is5xxServerError());
	}

}
