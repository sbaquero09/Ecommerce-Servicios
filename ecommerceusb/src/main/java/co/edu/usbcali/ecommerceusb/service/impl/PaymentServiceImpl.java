package co.edu.usbcali.ecommerceusb.service.impl;

import co.edu.usbcali.ecommerceusb.dto.CreatePaymentRequest;
import co.edu.usbcali.ecommerceusb.dto.PaymentResponse;
import co.edu.usbcali.ecommerceusb.mapper.PaymentMapper;
import co.edu.usbcali.ecommerceusb.model.Order;
import co.edu.usbcali.ecommerceusb.model.Payment;
import co.edu.usbcali.ecommerceusb.repository.OrderRepository;
import co.edu.usbcali.ecommerceusb.repository.PaymentRepository;
import co.edu.usbcali.ecommerceusb.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Override
    public List<PaymentResponse> getPayments() {
        List<Payment> payments = paymentRepository.findAll();

        if (payments.isEmpty()) {
            return List.of();
        }

        return PaymentMapper.modelToPaymentResponseList(payments);
    }

    @Override
    public PaymentResponse getPaymentById(Integer id) throws Exception {

        if (id == null || id <= 0) {
            throw new Exception("Debe ingresar el id para buscar");
        }

        Payment payment = paymentRepository.findById(id.longValue())
                .orElseThrow(() ->
                        new Exception(
                                String.format("Pago no encontrado con el id: %d", id)));

        return PaymentMapper.modelToPaymentResponse(payment);
    }

    @Override
    public PaymentResponse createPayment(CreatePaymentRequest createPaymentRequest) throws Exception {

        // Validar que el campo orderId no sea nulo ni <= 0
        if (createPaymentRequest.getOrderId() == null || createPaymentRequest.getOrderId() <= 0) {
            throw new Exception("El campo orderId debe contener un valor mayor a 0");
        }

        // Validar que el campo status no sea nulo ni vacío
        if (Objects.isNull(createPaymentRequest.getStatus()) ||
                createPaymentRequest.getStatus().isBlank()) {
            throw new Exception("El campo status no puede ser nulo ni vacío");
        }

        // Validar que el status sea un valor válido
        Payment.PaymentStatus paymentStatus;
        try {
            paymentStatus = Payment.PaymentStatus.valueOf(createPaymentRequest.getStatus());
        } catch (IllegalArgumentException e) {
            throw new Exception("El status debe ser uno de: SUCCEEDED, FAILED");
        }

        // Validar que el campo idempotencyKey no sea nulo ni vacío
        if (Objects.isNull(createPaymentRequest.getIdempotencyKey()) ||
                createPaymentRequest.getIdempotencyKey().isBlank()) {
            throw new Exception("El campo idempotencyKey no puede ser nulo ni vacío");
        }

        // Validar que la orden existe
        Order order = orderRepository.findById(createPaymentRequest.getOrderId().longValue())
                .orElseThrow(() -> new Exception("La orden no existe"));

        Payment payment = PaymentMapper.createPaymentRequestToPayment(
                order, paymentStatus,
                createPaymentRequest.getProviderRef(),
                createPaymentRequest.getIdempotencyKey());

        payment = paymentRepository.save(payment);
        return PaymentMapper.modelToPaymentResponse(payment);
    }
}
