package co.edu.usbcali.ecommerceusb.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@AllArgsConstructor
@Getter
public class UpdateOrderRequest {
    private Integer userId;
    private String status;
    private BigDecimal totalAmount;
    private String currency;
}
