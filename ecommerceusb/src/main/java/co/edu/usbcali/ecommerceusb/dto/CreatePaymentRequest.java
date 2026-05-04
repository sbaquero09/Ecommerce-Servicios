package co.edu.usbcali.ecommerceusb.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CreatePaymentRequest {
    private Integer orderId;
    private String status;
    private String providerRef;
    private String idempotencyKey;
}
