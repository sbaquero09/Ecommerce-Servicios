package co.edu.usbcali.ecommerceusb.mapper;

import co.edu.usbcali.ecommerceusb.dto.OrderResponse;
import co.edu.usbcali.ecommerceusb.model.Order;
import co.edu.usbcali.ecommerceusb.model.User;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

public class OrderMapper {

    public static OrderResponse modelToOrderResponse(Order order) {
        return OrderResponse.builder()
                .id(order.getId())
                .userId(order.getUser() != null ? order.getUser().getId() : null)
                .userFullName(order.getUser() != null ? order.getUser().getFullName() : null)
                .status(order.getStatus() != null ? order.getStatus().name() : null)
                .totalAmount(order.getTotalAmount())
                .currency(order.getCurrency())
                .build();
    }

    public static List<OrderResponse> modelToOrderResponseList(List<Order> orders) {
        return orders.stream().map(OrderMapper::modelToOrderResponse).toList();
    }

    public static Order createOrderRequestToOrder(User user, Order.OrderStatus status,
                                                  BigDecimal totalAmount, String currency) {
        OffsetDateTime datetime = OffsetDateTime.now();
        return Order.builder()
                .user(user)
                .status(status)
                .totalAmount(totalAmount)
                .currency(currency)
                .createdAt(datetime)
                .build();
    }
}
