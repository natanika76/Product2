package ru.natali.pizzeria.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.natali.pizzeria.dto.DeliveryDTO;
import ru.natali.pizzeria.exception.ResourceNotFoundException;
import ru.natali.pizzeria.mapper.DeliveryMapper;
import ru.natali.pizzeria.model.Delivery;
import ru.natali.pizzeria.model.Employee;
import ru.natali.pizzeria.model.Order;
import ru.natali.pizzeria.repository.DeliveryRepository;
import ru.natali.pizzeria.repository.EmployeeRepository;
import ru.natali.pizzeria.repository.OrderRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final DeliveryMapper deliveryMapper;
    private final OrderRepository orderRepository;
    private final EmployeeRepository employeeRepository;

    public List<DeliveryDTO> findAll() {
        return deliveryRepository.findAll().stream()
                .map(deliveryMapper::toDto)
                .collect(Collectors.toList());
    }

    public DeliveryDTO findById(Long id) {
        return deliveryRepository.findById(id)
                .map(deliveryMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Delivery not found with id: " + id));
    }

    @Transactional
    public DeliveryDTO create(DeliveryDTO deliveryDTO) {
        Delivery delivery = deliveryMapper.toEntity(deliveryDTO);
        setOrderAndEmployee(delivery, deliveryDTO.getOrderId(), deliveryDTO.getEmployeeId());
        Delivery savedDelivery = deliveryRepository.save(delivery);
        return deliveryMapper.toDto(savedDelivery);
    }

    @Transactional
    public DeliveryDTO update(Long id, DeliveryDTO deliveryDTO) {
        Delivery existingDelivery = deliveryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Delivery not found with id: " + id));

        deliveryMapper.updateDeliveryFromDto(deliveryDTO, existingDelivery);
        setOrderAndEmployee(existingDelivery, deliveryDTO.getOrderId(), deliveryDTO.getEmployeeId());

        Delivery updatedDelivery = deliveryRepository.save(existingDelivery);
        return deliveryMapper.toDto(updatedDelivery);
    }

    @Transactional
    public void delete(Long id) {
        deliveryRepository.deleteById(id);
    }

    @Transactional
    public DeliveryDTO assignToEmployee(Long deliveryId, Long employeeId) {
        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new ResourceNotFoundException("Delivery not found with id: " + deliveryId));

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + employeeId));

        delivery.setEmployee(employee);
        Delivery updatedDelivery = deliveryRepository.save(delivery);
        return deliveryMapper.toDto(updatedDelivery);
    }

    @Transactional
    public DeliveryDTO updateStatus(Long id, String status) {
        Delivery delivery = deliveryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Delivery not found with id: " + id));

        delivery.setStatus(status);
        Delivery updatedDelivery = deliveryRepository.save(delivery);
        return deliveryMapper.toDto(updatedDelivery);
    }

    private void setOrderAndEmployee(Delivery delivery, Long orderId, Long employeeId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));
        delivery.setOrder(order);

        if (employeeId != null) {
            Employee employee = employeeRepository.findById(employeeId)
                    .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + employeeId));
            delivery.setEmployee(employee);
        }
    }
}
