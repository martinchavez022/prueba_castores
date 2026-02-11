package com.example.demo.historial;

import com.example.demo.common.dto.ApiResponse;
import com.example.demo.common.entity.Historial;
import com.example.demo.common.service.HistorialService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/historial")
public class HistorialController {

    private final HistorialService historialService;

    public HistorialController(HistorialService historialService) {
        this.historialService = historialService;
    }

    @GetMapping({"/obtener/{tipo}", "{obtener}"})
    public ResponseEntity<ApiResponse<List<Historial>>> obtenerHistorialByTipo (
            @PathVariable(required = false) String tipo
    )
    {
        try {
            String tipo_send = "";
            if (tipo != null) {
                tipo_send = tipo;
            }
            List<Historial> historial = historialService.obtenerHistorialPorTipo(tipo_send);
            return new ResponseEntity<>(ApiResponse.ok("Historial obtenido correctamente", historial), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(ApiResponse.error(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

}
