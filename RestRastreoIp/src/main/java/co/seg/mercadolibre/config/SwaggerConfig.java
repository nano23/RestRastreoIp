package co.seg.mercadolibre.config;

import java.util.ArrayList;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Clase para configurar swagger para realizar la documentación del API REST
 * 
 * @author Carlos Gomez Salazar
 * @since 1.0
 *
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {
	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2)
				.ignoredParameterTypes(org.springframework.validation.Errors.class).select()
				.apis(RequestHandlerSelectors.basePackage("co.seg.mercadolibre")).paths(PathSelectors.any()).build()
				.apiInfo(apiInfo());
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private ApiInfo apiInfo() {
		return new ApiInfo("Rastreo Api", "Rastreo Api Rest documentación", "V1", "Terminos del servicio",
				new Contact("mercadolibre co", "https://www.mercadolibre.com/co/home.html", "test@mercadolibre.com"),
				"License of API", "API license URL", new ArrayList());
	}
}