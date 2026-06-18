package cl.duoc.sanosysalvos.geolocation.controller;

import cl.duoc.sanosysalvos.geolocation.model.Ubicacion;
import cl.duoc.sanosysalvos.geolocation.repository.UbicacionRepository;

import tools.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = UbicacionController.class)
public class UbicacionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UbicacionRepository ubicacionRepository;

    private Ubicacion testUbicacion;

    @BeforeEach
    void setUp() {
        testUbicacion = new Ubicacion();
        testUbicacion.setId(1L);
        testUbicacion.setLatitud(-33.456);
        testUbicacion.setLongitud(-70.648);
        testUbicacion.setNombreSector("Providencia");
        testUbicacion.setFechaRegistro(LocalDateTime.of(2026, 6, 17, 2, 30));
    }

    @Test
    void testUbicar_Success() throws Exception {
        when(ubicacionRepository.save(any(Ubicacion.class))).thenReturn(testUbicacion);

        mockMvc.perform(post("/api/geo/ubicar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testUbicacion)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.latitud").value(-33.456))
                .andExpect(jsonPath("$.longitud").value(-70.648))
                .andExpect(jsonPath("$.nombreSector").value("Providencia"));

        verify(ubicacionRepository).save(any(Ubicacion.class));
    }

    @Test
    void testHistorial_Success() throws Exception {
        when(ubicacionRepository.findAll()).thenReturn(List.of(testUbicacion));

        mockMvc.perform(get("/api/geo/historial"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].latitud").value(-33.456))
                .andExpect(jsonPath("$[0].longitud").value(-70.648))
                .andExpect(jsonPath("$[0].nombreSector").value("Providencia"));

        verify(ubicacionRepository).findAll();
    }
}
