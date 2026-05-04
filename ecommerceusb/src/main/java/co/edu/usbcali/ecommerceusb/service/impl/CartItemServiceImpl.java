package co.edu.usbcali.ecommerceusb.service.impl;

import co.edu.usbcali.ecommerceusb.dto.CartItemResponse;
import co.edu.usbcali.ecommerceusb.dto.CreateCartItemRequest;
import co.edu.usbcali.ecommerceusb.mapper.CartItemMapper;
import co.edu.usbcali.ecommerceusb.model.Cart;
import co.edu.usbcali.ecommerceusb.model.CartItem;
import co.edu.usbcali.ecommerceusb.model.Product;
import co.edu.usbcali.ecommerceusb.repository.CartItemRepository;
import co.edu.usbcali.ecommerceusb.repository.CartRepository;
import co.edu.usbcali.ecommerceusb.repository.ProductRepository;
import co.edu.usbcali.ecommerceusb.service.CartItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartItemServiceImpl implements CartItemService {

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductRepository productRepository;

    @Override
    public List<CartItemResponse> getCartItems() {
        List<CartItem> cartItems = cartItemRepository.findAll();

        if (cartItems.isEmpty()) {
            return List.of();
        }

        return CartItemMapper.modelToCartItemResponseList(cartItems);
    }

    @Override
    public CartItemResponse getCartItemById(Integer id) throws Exception {

        if (id == null || id <= 0) {
            throw new Exception("Debe ingresar el id para buscar");
        }

        CartItem cartItem = cartItemRepository.findById(id)
                .orElseThrow(() ->
                        new Exception(
                                String.format("Item de carrito no encontrado con el id: %d", id)));

        return CartItemMapper.modelToCartItemResponse(cartItem);
    }

    @Override
    public CartItemResponse createCartItem(CreateCartItemRequest createCartItemRequest) throws Exception {

        // Validar que el campo cartId no sea nulo ni <= 0
        if (createCartItemRequest.getCartId() == null || createCartItemRequest.getCartId() <= 0) {
            throw new Exception("El campo cartId debe contener un valor mayor a 0");
        }

        // Validar que el campo productId no sea nulo ni <= 0
        if (createCartItemRequest.getProductId() == null || createCartItemRequest.getProductId() <= 0) {
            throw new Exception("El campo productId debe contener un valor mayor a 0");
        }

        // Validar que el campo quantity no sea nulo ni <= 0
        if (createCartItemRequest.getQuantity() == null || createCartItemRequest.getQuantity() <= 0) {
            throw new Exception("El campo quantity debe contener un valor mayor a 0");
        }

        // Validar que el carrito existe
        Cart cart = cartRepository.findById(createCartItemRequest.getCartId().longValue())
                .orElseThrow(() -> new Exception("El carrito no existe"));

        // Validar que el producto existe
        Product product = productRepository.findById(createCartItemRequest.getProductId().longValue())
                .orElseThrow(() -> new Exception("El producto no existe"));

        CartItem cartItem = CartItemMapper.createCartItemRequestToCartItem(
                cart, product, createCartItemRequest.getQuantity());

        cartItem = cartItemRepository.save(cartItem);
        return CartItemMapper.modelToCartItemResponse(cartItem);
    }
}
