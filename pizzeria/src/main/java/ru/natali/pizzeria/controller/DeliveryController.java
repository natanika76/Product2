package ru.natali.pizzeria.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.natali.pizzeria.dto.DeliveryDTO;
import ru.natali.pizzeria.exception.ResourceNotFoundException;
import ru.natali.pizzeria.service.DeliveryService;

import java.util.List;

@RestController
@RequestMapping("/api/deliveries")
@RequiredArgsConstructor
public class DeliveryController {

    private final DeliveryService deliveryService;

    @GetMapping
    public ResponseEntity<List<DeliveryDTO>> getAllDeliveries() {
        try {
            return ResponseEntity.ok(deliveryService.findAll());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error fetching deliveries", e);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<DeliveryDTO> getDeliveryById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(deliveryService.findById(id));
        } catch (ResourceNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    @PostMapping
    public ResponseEntity<DeliveryDTO> createDelivery(@RequestBody DeliveryDTO deliveryDTO) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(deliveryService.create(deliveryDTO));
        } catch (ResourceNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<DeliveryDTO> updateDelivery(
            @PathVariable Long id,
            @RequestBody DeliveryDTO deliveryDTO) {
        try {
            return ResponseEntity.ok(deliveryService.update(id, deliveryDTO));
        } catch (ResourceNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDelivery(@PathVariable Long id) {
        try {
            deliveryService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    @PatchMapping("/{id}/assign")
    public ResponseEntity<DeliveryDTO> assignDeliveryToEmployee(
            @PathVariable Long id,
            @RequestParam Long employeeId) {
        try {
            return ResponseEntity.ok(deliveryService.assignToEmployee(id, employeeId));
        } catch (ResourceNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<DeliveryDTO> updateDeliveryStatus(
            @PathVariable Long id,
            @RequestParam String status) {
        try {
            return ResponseEntity.ok(deliveryService.updateStatus(id, status));
        } catch (ResourceNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }
}
