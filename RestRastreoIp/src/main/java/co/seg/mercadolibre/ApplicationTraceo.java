package co.seg.mercadolibre;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.web.WebApplicationInitializer;
import co.seg.mercadolibre.services.CurrencyService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@EnableAutoConfiguration
@EntityScan("co.seg.mercadolibre")
@SpringBootApplication(scanBasePackages = { "co.seg.mercadolibre" })
public class ApplicationTraceo extends SpringBootServletInitializer implements WebApplicationInitializer {

	/**
	 * Bean que permite realizar el mapeo entre los DTO y las entidad
	 * 
	 * @return Objeto para realizar el mapeo de objetos
	 */
	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

	/**
	 * metodo principal de despliegue de la aplicacion
	 * 
	 * @param args parametros iniciales
	 */
	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(ApplicationTraceo.class, args);
		try {
			((CurrencyService) context.getBean("currencyService")).getAll();
		} catch (Exception e) {
			logExepction(e);
		}
	}

	/**
	 * metodo para generar logs
	 * 
	 * @param e excepcion
	 */
	private static void logExepction(Exception e) {
		log.error("Error inicializacion aplicacion: " + e.getMessage() + " " + e.getLocalizedMessage());
	}

}
