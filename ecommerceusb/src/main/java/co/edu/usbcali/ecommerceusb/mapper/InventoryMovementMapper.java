package co.edu.usbcali.ecommerceusb.mapper;

import co.edu.usbcali.ecommerceusb.dto.InventoryMovementResponse;
import co.edu.usbcali.ecommerceusb.model.InventoryMovement;
import co.edu.usbcali.ecommerceusb.model.Order;
import co.edu.usbcali.ecommerceusb.model.Product;

import java.time.OffsetDateTime;
import java.util.List;

public class InventoryMovementMapper {

    public static InventoryMovementResponse modelToInventoryMovementResponse(InventoryMovement inventoryMovement) {
        return InventoryMovementResponse.builder()
                .id(inventoryMovement.getId())
                .productId(inventoryMovement.getProduct() != null ? inventoryMovement.getProduct().getId() : null)
                .productName(inventoryMovement.getProduct() != null ? inventoryMovement.getProduct().getName() : null)
                .orderId(inventoryMovement.getOrder() != null ? inventoryMovement.getOrder().getId() : null)
                .type(inventoryMovement.getType() != null ? inventoryMovement.getType().name() : null)
                .qty(inventoryMovement.getQty())
                .build();
    }

    public static List<InventoryMovementResponse> modelToInventoryMovementResponseList(
            List<InventoryMovement> inventoryMovements) {
        return inventoryMovements.stream().map(InventoryMovementMapper::modelToInventoryMovementResponse).toList();
    }

    public static InventoryMovement createInventoryMovementRequestToInventoryMovement(
            Product product, Order order,
            InventoryMovement.MovementType type, Integer qty) {
        return InventoryMovement.builder()
                .product(product)
                .order(order)
                .type(type)
                .qty(qty)
                .createdAt(OffsetDateTime.now())
                .build();
    }
}
