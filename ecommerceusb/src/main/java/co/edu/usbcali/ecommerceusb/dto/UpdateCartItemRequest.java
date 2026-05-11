package co.edu.usbcali.ecommerceusb.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UpdateCartItemRequest {
    private Integer cartId;
    private Integer productId;
    private Integer quantity;
}
