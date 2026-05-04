package co.edu.usbcali.ecommerceusb.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CreateInventoryMovementRequest {
    private Integer productId;
    private Integer orderId;
    private String type;
    private Integer qty;
}
