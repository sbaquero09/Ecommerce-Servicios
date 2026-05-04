package co.edu.usbcali.ecommerceusb.service.impl;

import co.edu.usbcali.ecommerceusb.dto.CreateOrderRequest;
import co.edu.usbcali.ecommerceusb.dto.OrderResponse;
import co.edu.usbcali.ecommerceusb.mapper.OrderMapper;
import co.edu.usbcali.ecommerceusb.model.Order;
import co.edu.usbcali.ecommerceusb.model.User;
import co.edu.usbcali.ecommerceusb.repository.OrderRepository;
import co.edu.usbcali.ecommerceusb.repository.UserRepository;
import co.edu.usbcali.ecommerceusb.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<OrderResponse> getOrders() {
        List<Order> orders = orderRepository.findAll();

        if (orders.isEmpty()) {
            return List.of();
        }

        return OrderMapper.modelToOrderResponseList(orders);
    }

    @Override
    public OrderResponse getOrderById(Integer id) throws Exception {

        if (id == null || id <= 0) {
            throw new Exception("Debe ingresar el id para buscar");
        }

        Order order = orderRepository.findById(id.longValue())
                .orElseThrow(() ->
                        new Exception(
                                String.format("Orden no encontrada con el id: %d", id)));

        return OrderMapper.modelToOrderResponse(order);
    }

    @Override
    public OrderResponse createOrder(CreateOrderRequest createOrderRequest) throws Exception {

        // Validar que el campo userId no sea nulo ni <= 0
        if (createOrderRequest.getUserId() == null || createOrderRequest.getUserId() <= 0) {
            throw new Exception("El campo userId debe contener un valor mayor a 0");
        }

        // Validar que el campo status no sea nulo ni vacío
        if (Objects.isNull(createOrderRequest.getStatus()) ||
                createOrderRequest.getStatus().isBlank()) {
            throw new Exception("El campo status no puede ser nulo ni vacío");
        }

        // Validar que el status sea un valor válido
        Order.OrderStatus orderStatus;
        try {
            orderStatus = Order.OrderStatus.valueOf(createOrderRequest.getStatus());
        } catch (IllegalArgumentException e) {
            throw new Exception("El status debe ser uno de: CREATED, PAID, CANCELLED");
        }

        // Validar que el campo totalAmount no sea nulo
        if (Objects.isNull(createOrderRequest.getTotalAmount())) {
            throw new Exception("El campo totalAmount no puede ser nulo");
        }

        // Validar que el campo currency no sea nulo ni vacío
        if (Objects.isNull(createOrderRequest.getCurrency()) ||
                createOrderRequest.getCurrency().isBlank()) {
            throw new Exception("El campo currency no puede ser nulo ni vacío");
        }

        // Validar que el usuario existe
        User user = userRepository.findById(createOrderRequest.getUserId())
                .orElseThrow(() -> new Exception("El usuario no existe"));

        Order order = OrderMapper.createOrderRequestToOrder(
                user, orderStatus,
                createOrderRequest.getTotalAmount(),
                createOrderRequest.getCurrency());

        order = orderRepository.save(order);
        return OrderMapper.modelToOrderResponse(order);
    }
}
