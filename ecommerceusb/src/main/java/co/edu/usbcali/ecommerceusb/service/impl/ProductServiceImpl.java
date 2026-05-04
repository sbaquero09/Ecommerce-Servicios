package co.edu.usbcali.ecommerceusb.service.impl;

import co.edu.usbcali.ecommerceusb.dto.CreateProductRequest;
import co.edu.usbcali.ecommerceusb.dto.ProductResponse;
import co.edu.usbcali.ecommerceusb.mapper.ProductMapper;
import co.edu.usbcali.ecommerceusb.model.Product;
import co.edu.usbcali.ecommerceusb.repository.ProductRepository;
import co.edu.usbcali.ecommerceusb.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public List<ProductResponse> getProducts() {
        List<Product> products = productRepository.findAll();

        if (products.isEmpty()) {
            return List.of();
        }

        return ProductMapper.modelToProductResponseList(products);
    }

    @Override
    public ProductResponse getProductById(Integer id) throws Exception {

        if (id == null || id <= 0) {
            throw new Exception("Debe ingresar el id para buscar");
        }

        Product product = productRepository.findById(id.longValue())
                .orElseThrow(() ->
                        new Exception(
                                String.format("Producto no encontrado con el id: %d", id)));

        return ProductMapper.modelToProductResponse(product);
    }

    @Override
    public ProductResponse createProduct(CreateProductRequest createProductRequest) throws Exception {

        // Validar que el campo name no sea nulo ni vacío
        if (Objects.isNull(createProductRequest.getName()) ||
                createProductRequest.getName().isBlank()) {
            throw new Exception("El campo name no puede ser nulo ni vacío");
        }

        // Validar que el campo price no sea nulo
        if (Objects.isNull(createProductRequest.getPrice())) {
            throw new Exception("El campo price no puede ser nulo");
        }

        // Validar que price no sea negativo
        if (createProductRequest.getPrice().signum() < 0) {
            throw new Exception("El campo price no puede ser negativo");
        }

        Product product = ProductMapper.createProductRequestToProduct(createProductRequest);

        product = productRepository.save(product);
        return ProductMapper.modelToProductResponse(product);
    }
}
