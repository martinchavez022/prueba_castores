package com.example.demo.common.service;

import com.example.demo.common.entity.Historial;
import com.example.demo.common.repository.HistorialRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HistorialService
{
    private final HistorialRepository historialRepository;

    public HistorialService(HistorialRepository historialRepository) {
        this.historialRepository = historialRepository;
    }

    /**
     * Obtener historial de movimientos por tipo
     * @param tipo El tipo de movimiento para filtrar los movimientos
     * @return List<Historial>
     */
    public List<Historial> obtenerHistorialPorTipo(String tipo) {
        if (tipo == "") {
            return historialRepository.findAll();
        }
        return historialRepository.findByTipo(tipo.toLowerCase());
    }
}
