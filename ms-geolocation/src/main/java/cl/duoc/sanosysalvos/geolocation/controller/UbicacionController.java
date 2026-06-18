package cl.duoc.sanosysalvos.geolocation.controller;

import cl.duoc.sanosysalvos.geolocation.model.Ubicacion;
import cl.duoc.sanosysalvos.geolocation.repository.UbicacionRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/geo")
@RequiredArgsConstructor
@Tag(name = "Geolocalización", description = "Endpoints para registro y consulta de ubicaciones de vecinos")
public class UbicacionController {

    private final UbicacionRepository ubicacionRepository;

    @PostMapping("/ubicar")
    @Operation(summary = "Registrar ubicación", description = "Guarda la ubicación geográfica actual de un vecino con su latitud, longitud y nombre del sector.")
    public ResponseEntity<Ubicacion> ubicar(@RequestBody Ubicacion ubicacion) {
        Ubicacion ubicacionGuardada = ubicacionRepository.save(ubicacion);
        return ResponseEntity.status(HttpStatus.CREATED).body(ubicacionGuardada);
    }

    @GetMapping("/historial")
    @Operation(summary = "Obtener historial de ubicaciones", description = "Retorna el listado completo del historial de ubicaciones guardadas en la plataforma.")
    public ResponseEntity<List<Ubicacion>> historial() {
        return ResponseEntity.ok(ubicacionRepository.findAll());
    }
}
