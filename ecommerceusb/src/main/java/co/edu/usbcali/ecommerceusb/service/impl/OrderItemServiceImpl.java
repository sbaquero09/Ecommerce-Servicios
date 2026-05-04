package co.edu.usbcali.ecommerceusb.service.impl;

import co.edu.usbcali.ecommerceusb.dto.CreateOrderItemRequest;
import co.edu.usbcali.ecommerceusb.dto.OrderItemResponse;
import co.edu.usbcali.ecommerceusb.mapper.OrderItemMapper;
import co.edu.usbcali.ecommerceusb.model.Order;
import co.edu.usbcali.ecommerceusb.model.OrderItem;
import co.edu.usbcali.ecommerceusb.model.Product;
import co.edu.usbcali.ecommerceusb.repository.OrderItemRepository;
import co.edu.usbcali.ecommerceusb.repository.OrderRepository;
import co.edu.usbcali.ecommerceusb.repository.ProductRepository;
import co.edu.usbcali.ecommerceusb.service.OrderItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class OrderItemServiceImpl implements OrderItemService {

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Override
    public List<OrderItemResponse> getOrderItems() {
        List<OrderItem> orderItems = orderItemRepository.findAll();

        if (orderItems.isEmpty()) {
            return List.of();
        }

        return OrderItemMapper.modelToOrderItemResponseList(orderItems);
    }

    @Override
    public OrderItemResponse getOrderItemById(Integer id) throws Exception {

        if (id == null || id <= 0) {
            throw new Exception("Debe ingresar el id para buscar");
        }

        OrderItem orderItem = orderItemRepository.findById(id.longValue())
                .orElseThrow(() ->
                        new Exception(
                                String.format("Item de orden no encontrado con el id: %d", id)));

        return OrderItemMapper.modelToOrderItemResponse(orderItem);
    }

    @Override
    public OrderItemResponse createOrderItem(CreateOrderItemRequest createOrderItemRequest) throws Exception {

        // Validar que el campo orderId no sea nulo ni <= 0
        if (createOrderItemRequest.getOrderId() == null || createOrderItemRequest.getOrderId() <= 0) {
            throw new Exception("El campo orderId debe contener un valor mayor a 0");
        }

        // Validar que el campo productId no sea nulo ni <= 0
        if (createOrderItemRequest.getProductId() == null || createOrderItemRequest.getProductId() <= 0) {
            throw new Exception("El campo productId debe contener un valor mayor a 0");
        }

        // Validar que el campo quantity no sea nulo ni <= 0
        if (createOrderItemRequest.getQuantity() == null || createOrderItemRequest.getQuantity() <= 0) {
            throw new Exception("El campo quantity debe contener un valor mayor a 0");
        }

        // Validar que el campo unitPriceSnapshot no sea nulo
        if (Objects.isNull(createOrderItemRequest.getUnitPriceSnapshot())) {
            throw new Exception("El campo unitPriceSnapshot no puede ser nulo");
        }

        // Validar que el campo lineTotal no sea nulo
        if (Objects.isNull(createOrderItemRequest.getLineTotal())) {
            throw new Exception("El campo lineTotal no puede ser nulo");
        }

        // Validar que la orden existe
        Order order = orderRepository.findById(createOrderItemRequest.getOrderId().longValue())
                .orElseThrow(() -> new Exception("La orden no existe"));

        // Validar que el producto existe
        Product product = productRepository.findById(createOrderItemRequest.getProductId().longValue())
                .orElseThrow(() -> new Exception("El producto no existe"));

        OrderItem orderItem = OrderItemMapper.createOrderItemRequestToOrderItem(
                order, product,
                createOrderItemRequest.getQuantity(),
                createOrderItemRequest.getUnitPriceSnapshot(),
                createOrderItemRequest.getLineTotal());

        orderItem = orderItemRepository.save(orderItem);
        return OrderItemMapper.modelToOrderItemResponse(orderItem);
    }
}
