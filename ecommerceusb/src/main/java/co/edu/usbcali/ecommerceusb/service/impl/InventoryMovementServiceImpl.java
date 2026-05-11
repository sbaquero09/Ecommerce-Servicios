package co.edu.usbcali.ecommerceusb.service.impl;

import co.edu.usbcali.ecommerceusb.dto.CreateInventoryMovementRequest;
import co.edu.usbcali.ecommerceusb.dto.InventoryMovementResponse;
import co.edu.usbcali.ecommerceusb.dto.UpdateInventoryMovementRequest;
import co.edu.usbcali.ecommerceusb.mapper.InventoryMovementMapper;
import co.edu.usbcali.ecommerceusb.model.InventoryMovement;
import co.edu.usbcali.ecommerceusb.model.Order;
import co.edu.usbcali.ecommerceusb.model.Product;
import co.edu.usbcali.ecommerceusb.repository.InventoryMovementRepository;
import co.edu.usbcali.ecommerceusb.repository.OrderRepository;
import co.edu.usbcali.ecommerceusb.repository.ProductRepository;
import co.edu.usbcali.ecommerceusb.service.InventoryMovementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class InventoryMovementServiceImpl implements InventoryMovementService {

    @Autowired
    private InventoryMovementRepository inventoryMovementRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Override
    public List<InventoryMovementResponse> getInventoryMovements() {
        List<InventoryMovement> inventoryMovements = inventoryMovementRepository.findAll();

        if (inventoryMovements.isEmpty()) {
            return List.of();
        }

        return InventoryMovementMapper.modelToInventoryMovementResponseList(inventoryMovements);
    }

    @Override
    public InventoryMovementResponse getInventoryMovementById(Integer id) throws Exception {

        if (id == null || id <= 0) {
            throw new Exception("Debe ingresar el id para buscar");
        }

        InventoryMovement inventoryMovement = inventoryMovementRepository.findById(id)
                .orElseThrow(() ->
                        new Exception(
                                String.format("Movimiento de inventario no encontrado con el id: %d", id)));

        return InventoryMovementMapper.modelToInventoryMovementResponse(inventoryMovement);
    }

    @Override
    public InventoryMovementResponse createInventoryMovement(
            CreateInventoryMovementRequest createInventoryMovementRequest) throws Exception {

        if (createInventoryMovementRequest.getProductId() == null ||
                createInventoryMovementRequest.getProductId() <= 0) {
            throw new Exception("El campo productId debe contener un valor mayor a 0");
        }

        if (Objects.isNull(createInventoryMovementRequest.getType()) ||
                createInventoryMovementRequest.getType().isBlank()) {
            throw new Exception("El campo type no puede ser nulo ni vacío");
        }

        InventoryMovement.MovementType movementType;
        try {
            movementType = InventoryMovement.MovementType.valueOf(createInventoryMovementRequest.getType());
        } catch (IllegalArgumentException e) {
            throw new Exception("El type debe ser uno de: DEBIT, CREDIT, RESERVE, RELEASE");
        }

        if (createInventoryMovementRequest.getQty() == null || createInventoryMovementRequest.getQty() <= 0) {
            throw new Exception("El campo qty debe contener un valor mayor a 0");
        }

        Product product = productRepository.findById(createInventoryMovementRequest.getProductId())
                .orElseThrow(() -> new Exception("El producto no existe"));

        Order order = null;
        if (createInventoryMovementRequest.getOrderId() != null) {
            order = orderRepository.findById(createInventoryMovementRequest.getOrderId())
                    .orElseThrow(() -> new Exception("La orden no existe"));
        }

        InventoryMovement inventoryMovement = InventoryMovementMapper.createInventoryMovementRequestToInventoryMovement(
                product, order, movementType, createInventoryMovementRequest.getQty());

        inventoryMovement = inventoryMovementRepository.save(inventoryMovement);
        return InventoryMovementMapper.modelToInventoryMovementResponse(inventoryMovement);
    }

    @Override
    public InventoryMovementResponse updateInventoryMovement(
            Integer id, UpdateInventoryMovementRequest updateInventoryMovementRequest) throws Exception {

        if (id == null || id <= 0) {
            throw new Exception("Debe ingresar el id para actualizar");
        }

        if (updateInventoryMovementRequest.getProductId() == null ||
                updateInventoryMovementRequest.getProductId() <= 0) {
            throw new Exception("El campo productId debe contener un valor mayor a 0");
        }

        if (Objects.isNull(updateInventoryMovementRequest.getType()) ||
                updateInventoryMovementRequest.getType().isBlank()) {
            throw new Exception("El campo type no puede ser nulo ni vacío");
        }

        InventoryMovement.MovementType movementType;
        try {
            movementType = InventoryMovement.MovementType.valueOf(updateInventoryMovementRequest.getType());
        } catch (IllegalArgumentException e) {
            throw new Exception("El type debe ser uno de: DEBIT, CREDIT, RESERVE, RELEASE");
        }

        if (updateInventoryMovementRequest.getQty() == null || updateInventoryMovementRequest.getQty() <= 0) {
            throw new Exception("El campo qty debe contener un valor mayor a 0");
        }

        InventoryMovement inventoryMovement = inventoryMovementRepository.findById(id)
                .orElseThrow(() ->
                        new Exception(
                                String.format("Movimiento de inventario no encontrado con el id: %d", id)));

        Product product = productRepository.findById(updateInventoryMovementRequest.getProductId())
                .orElseThrow(() -> new Exception("El producto no existe"));

        Order order = null;
        if (updateInventoryMovementRequest.getOrderId() != null) {
            order = orderRepository.findById(updateInventoryMovementRequest.getOrderId())
                    .orElseThrow(() -> new Exception("La orden no existe"));
        }

        inventoryMovement.setProduct(product);
        inventoryMovement.setOrder(order);
        inventoryMovement.setType(movementType);
        inventoryMovement.setQty(updateInventoryMovementRequest.getQty());

        inventoryMovement = inventoryMovementRepository.save(inventoryMovement);
        return InventoryMovementMapper.modelToInventoryMovementResponse(inventoryMovement);
    }
}
