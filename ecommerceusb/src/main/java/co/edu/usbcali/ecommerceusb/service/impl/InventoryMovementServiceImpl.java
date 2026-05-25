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
        List<InventoryMovement> list = inventoryMovementRepository.findAll();
        if (list.isEmpty()) return List.of();
        return InventoryMovementMapper.modelToInventoryMovementResponseList(list);
    }

    @Override
    public InventoryMovementResponse getInventoryMovementById(Integer id) throws Exception {
        if (id == null || id <= 0) throw new Exception("Debe ingresar el id para buscar");
        InventoryMovement im = inventoryMovementRepository.findById(id)
                .orElseThrow(() -> new Exception(String.format("Movimiento de inventario no encontrado con el id: %d", id)));
        return InventoryMovementMapper.modelToInventoryMovementResponse(im);
    }

    @Override
    public InventoryMovementResponse createInventoryMovement(CreateInventoryMovementRequest req) throws Exception {
        if (req.getProductId() == null || req.getProductId() <= 0)
            throw new Exception("El campo productId debe contener un valor mayor a 0");
        if (Objects.isNull(req.getType()) || req.getType().isBlank())
            throw new Exception("El campo type no puede ser nulo ni vacío");
        if (req.getQty() == null || req.getQty() <= 0)
            throw new Exception("El campo qty debe contener un valor mayor a 0");

        Product product = productRepository.findById(req.getProductId())
                .orElseThrow(() -> new Exception("El producto no existe"));
        Order order = null;
        if (req.getOrderId() != null)
            order = orderRepository.findById(req.getOrderId())
                    .orElseThrow(() -> new Exception("La orden no existe"));

        InventoryMovement im = InventoryMovementMapper.createInventoryMovementRequestToInventoryMovement(
                product, order, InventoryMovement.MovementType.valueOf(req.getType()), req.getQty());
        im = inventoryMovementRepository.save(im);
        return InventoryMovementMapper.modelToInventoryMovementResponse(im);
    }

    @Override
    public InventoryMovementResponse updateInventoryMovement(Integer id, UpdateInventoryMovementRequest req) throws Exception {
        if (id == null || id <= 0) throw new Exception("Debe ingresar el id para actualizar");
        if (req.getProductId() == null || req.getProductId() <= 0)
            throw new Exception("El campo productId debe contener un valor mayor a 0");
        if (Objects.isNull(req.getType()) || req.getType().isBlank())
            throw new Exception("El campo type no puede ser nulo ni vacío");
        if (req.getQty() == null || req.getQty() <= 0)
            throw new Exception("El campo qty debe contener un valor mayor a 0");

        InventoryMovement im = inventoryMovementRepository.findById(id)
                .orElseThrow(() -> new Exception(String.format("Movimiento de inventario no encontrado con el id: %d", id)));
        Product product = productRepository.findById(req.getProductId())
                .orElseThrow(() -> new Exception("El producto no existe"));
        Order order = null;
        if (req.getOrderId() != null)
            order = orderRepository.findById(req.getOrderId())
                    .orElseThrow(() -> new Exception("La orden no existe"));

        im.setProduct(product);
        im.setOrder(order);
        im.setType(InventoryMovement.MovementType.valueOf(req.getType()));
        im.setQty(req.getQty());
        im = inventoryMovementRepository.save(im);
        return InventoryMovementMapper.modelToInventoryMovementResponse(im);
    }

    @Override
    public void deleteInventoryMovement(Integer id) throws Exception {
        if (id == null || id <= 0) throw new Exception("Debe ingresar el id para eliminar");
        if (!inventoryMovementRepository.existsById(id))
            throw new Exception(String.format("Movimiento de inventario no encontrado con el id: %d", id));
        inventoryMovementRepository.deleteById(id);
    }
}
