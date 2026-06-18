package cl.duoc.sanosysalvos.geolocation.repository;

import cl.duoc.sanosysalvos.geolocation.model.Ubicacion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UbicacionRepository extends JpaRepository<Ubicacion, Long> {
}
