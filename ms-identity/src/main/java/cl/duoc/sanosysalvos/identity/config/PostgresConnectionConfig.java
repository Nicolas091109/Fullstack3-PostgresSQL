package cl.duoc.sanosysalvos.identity.config;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PostgresConnectionConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(PostgresConnectionConfig.class);

    @Bean
    public ApplicationRunner postgresConnectionRunner(DataSource dataSource) {
        return args -> {
            try (Connection connection = dataSource.getConnection()) {
                DatabaseMetaData metaData = connection.getMetaData();
                LOGGER.info(
                        "Conexion PostgreSQL exitosa: url={}, producto={}, version={}",
                        metaData.getURL(),
                        metaData.getDatabaseProductName(),
                        metaData.getDatabaseProductVersion());
            } catch (Exception exception) {
                LOGGER.error("Error al conectar con PostgreSQL en ms-identity", exception);
                throw exception;
            }
        };
    }
}
