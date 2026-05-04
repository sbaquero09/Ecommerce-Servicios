package co.edu.usbcali.ecommerceusb.mapper;

import co.edu.usbcali.ecommerceusb.dto.PaymentResponse;
import co.edu.usbcali.ecommerceusb.model.Order;
import co.edu.usbcali.ecommerceusb.model.Payment;

import java.time.OffsetDateTime;
import java.util.List;

public class PaymentMapper {

    public static PaymentResponse modelToPaymentResponse(Payment payment) {
        return PaymentResponse.builder()
                .id(payment.getId())
                .orderId(payment.getOrder() != null ? payment.getOrder().getId() : null)
                .status(payment.getStatus() != null ? payment.getStatus().name() : null)
                .providerRef(payment.getProviderRef())
                .idempotencyKey(payment.getIdempotencyKey())
                .build();
    }

    public static List<PaymentResponse> modelToPaymentResponseList(List<Payment> payments) {
        return payments.stream().map(PaymentMapper::modelToPaymentResponse).toList();
    }

    public static Payment createPaymentRequestToPayment(Order order, Payment.PaymentStatus status,
                                                        String providerRef, String idempotencyKey) {
        return Payment.builder()
                .order(order)
                .status(status)
                .providerRef(providerRef)
                .idempotencyKey(idempotencyKey)
                .createdAt(OffsetDateTime.now())
                .build();
    }
}
