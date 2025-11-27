package com.kjm_sports.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kjm_sports.model.Boleta;
import com.kjm_sports.model.DetalleBoleta;
import com.kjm_sports.model.Producto;
import com.kjm_sports.repository.BoletaRepository;
import com.kjm_sports.repository.ProductoRepository;

@RestController
@RequestMapping("/api/boletas")
public class BoletaController {

    @Autowired
    private BoletaRepository boletaRepository;

    @Autowired
    private ProductoRepository productoRepository;

    // Ver todas las ventas
    @GetMapping
    public List<Boleta> listarBoletas() {
        return boletaRepository.findAll();
    }

    // CREAR UNA VENTA (COMPLEJA)
  @PostMapping
    public Boleta crearBoleta(@RequestBody Boleta boleta) {
        boleta.setFecha(LocalDateTime.now());
        Double totalCalculado = 0.0;

        for (DetalleBoleta detalle : boleta.getDetalles()) {
            Producto productoOriginal = productoRepository.findById(detalle.getProducto().getId()).orElse(null);
            
            if (productoOriginal != null) {
                // --- CONTROL DE STOCK ---
                // Si no hay suficiente stock, lanzamos un error (opcional, pero recomendado)
                if (productoOriginal.getStock() < detalle.getCantidad()) {
                     throw new RuntimeException("No hay suficiente stock para el producto: " + productoOriginal.getNombre());
                }

                // Restamos el stock
                productoOriginal.setStock(productoOriginal.getStock() - detalle.getCantidad());
                // Guardamos el producto con el nuevo stock
                productoRepository.save(productoOriginal);
                // ------------------------

                detalle.setPrecioUnitario(productoOriginal.getPrecio());
                detalle.setSubtotal(detalle.getCantidad() * productoOriginal.getPrecio());
                totalCalculado += detalle.getSubtotal();
                detalle.setBoleta(boleta);
            }
        }

        boleta.setTotal(totalCalculado);
        return boletaRepository.save(boleta);
    }
    }