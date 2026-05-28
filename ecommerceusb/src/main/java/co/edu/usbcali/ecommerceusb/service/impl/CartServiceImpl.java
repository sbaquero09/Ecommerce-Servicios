package co.edu.usbcali.ecommerceusb.service.impl;
import co.edu.usbcali.ecommerceusb.exception.BadRequestException;
import co.edu.usbcali.ecommerceusb.exception.NotFoundException;

import co.edu.usbcali.ecommerceusb.dto.CartResponse;
import co.edu.usbcali.ecommerceusb.dto.CreateCartRequest;
import co.edu.usbcali.ecommerceusb.dto.UpdateCartRequest;
import co.edu.usbcali.ecommerceusb.mapper.CartMapper;
import co.edu.usbcali.ecommerceusb.model.Cart;
import co.edu.usbcali.ecommerceusb.model.User;
import co.edu.usbcali.ecommerceusb.repository.CartRepository;
import co.edu.usbcali.ecommerceusb.repository.UserRepository;
import co.edu.usbcali.ecommerceusb.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<CartResponse> getCarts() {
        List<Cart> carts = cartRepository.findAll();
        if (carts.isEmpty()) return List.of();
        return CartMapper.modelToCartResponseList(carts);
    }

    @Override
    public CartResponse getCartById(Integer id) throws Exception {
        if (id == null || id <= 0) throw new BadRequestException("Debe ingresar el id para buscar");
        Cart cart = cartRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Carrito no encontrado con el id: %d", id)));
        return CartMapper.modelToCartResponse(cart);
    }

    @Override
    public CartResponse createCart(CreateCartRequest createCartRequest) throws Exception {
        if (createCartRequest.getUserId() == null || createCartRequest.getUserId() <= 0)
            throw new BadRequestException("El campo userId debe contener un valor mayor a 0");
        if (Objects.isNull(createCartRequest.getStatus()) || createCartRequest.getStatus().isBlank())
            throw new BadRequestException("El campo status no puede ser nulo ni vacío");

        User user = userRepository.findById(createCartRequest.getUserId())
                .orElseThrow(() -> new NotFoundException("El usuario no existe"));

        Cart cart = CartMapper.createCartRequestToCart(user, Cart.CartStatus.valueOf(createCartRequest.getStatus()));
        cart = cartRepository.save(cart);
        return CartMapper.modelToCartResponse(cart);
    }

    @Override
    public CartResponse updateCart(Integer id, UpdateCartRequest updateCartRequest) throws Exception {
        if (id == null || id <= 0) throw new BadRequestException("Debe ingresar el id para actualizar");
        if (updateCartRequest.getUserId() == null || updateCartRequest.getUserId() <= 0)
            throw new BadRequestException("El campo userId debe contener un valor mayor a 0");
        if (Objects.isNull(updateCartRequest.getStatus()) || updateCartRequest.getStatus().isBlank())
            throw new BadRequestException("El campo status no puede ser nulo ni vacío");

        Cart cart = cartRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Carrito no encontrado con el id: %d", id)));
        User user = userRepository.findById(updateCartRequest.getUserId())
                .orElseThrow(() -> new NotFoundException("El usuario no existe"));

        cart.setUser(user);
        cart.setStatus(Cart.CartStatus.valueOf(updateCartRequest.getStatus()));
        cart.setUpdatedAt(OffsetDateTime.now());
        cart = cartRepository.save(cart);
        return CartMapper.modelToCartResponse(cart);
    }

    @Override
    public void deleteCart(Integer id) throws Exception {
        if (id == null || id <= 0) throw new BadRequestException("Debe ingresar el id para eliminar");
        if (!cartRepository.existsById(id))
            throw new NotFoundException(String.format("Carrito no encontrado con el id: %d", id));
        cartRepository.deleteById(id);
    }
}
