package cl.duoc.sanosysalvos.geolocation.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de Geolocalización - Sanos y Salvos")
                        .version("1.0.0")
                        .description("Servicios para registro de ubicaciones de vecinos en tiempo real e historial de geolocalización."));
    }
}
