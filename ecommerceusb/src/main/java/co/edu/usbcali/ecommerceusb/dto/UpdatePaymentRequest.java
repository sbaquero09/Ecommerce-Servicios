package co.edu.usbcali.ecommerceusb.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UpdatePaymentRequest {
    private Integer orderId;
    private String status;
    private String providerRef;
    private String idempotencyKey;
}
