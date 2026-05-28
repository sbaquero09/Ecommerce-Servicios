package co.edu.usbcali.ecommerceusb.service.impl;

import co.edu.usbcali.ecommerceusb.dto.CreateProductRequest;
import co.edu.usbcali.ecommerceusb.dto.ProductResponse;
import co.edu.usbcali.ecommerceusb.dto.UpdateProductRequest;
import co.edu.usbcali.ecommerceusb.exception.BadRequestException;
import co.edu.usbcali.ecommerceusb.exception.NotFoundException;
import co.edu.usbcali.ecommerceusb.mapper.ProductMapper;
import co.edu.usbcali.ecommerceusb.model.Product;
import co.edu.usbcali.ecommerceusb.repository.ProductRepository;
import co.edu.usbcali.ecommerceusb.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public List<ProductResponse> getProducts() {
        List<Product> products = productRepository.findAll();
        if (products.isEmpty()) return List.of();
        return ProductMapper.modelToProductResponseList(products);
    }

    @Override
    public ProductResponse getProductById(Integer id) throws Exception {
        if (id == null || id <= 0)
            throw new BadRequestException("Debe ingresar el id para buscar");

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Producto no encontrado con el id: %d", id)));

        return ProductMapper.modelToProductResponse(product);
    }

    @Override
    public ProductResponse createProduct(CreateProductRequest createProductRequest) throws Exception {
        if (Objects.isNull(createProductRequest.getName()) || createProductRequest.getName().isBlank())
            throw new BadRequestException("El campo name no puede ser nulo ni vacío");
        if (Objects.isNull(createProductRequest.getPrice()))
            throw new BadRequestException("El campo price no puede ser nulo");
        if (createProductRequest.getPrice().signum() < 0)
            throw new BadRequestException("El campo price no puede ser negativo");

        Product product = ProductMapper.createProductRequestToProduct(createProductRequest);
        product = productRepository.save(product);
        return ProductMapper.modelToProductResponse(product);
    }

    @Override
    public ProductResponse updateProduct(Integer id, UpdateProductRequest updateProductRequest) throws Exception {
        if (id == null || id <= 0)
            throw new BadRequestException("Debe ingresar el id para actualizar");
        if (Objects.isNull(updateProductRequest.getName()) || updateProductRequest.getName().isBlank())
            throw new BadRequestException("El campo name no puede ser nulo ni vacío");
        if (Objects.isNull(updateProductRequest.getPrice()))
            throw new BadRequestException("El campo price no puede ser nulo");
        if (updateProductRequest.getPrice().signum() < 0)
            throw new BadRequestException("El campo price no puede ser negativo");

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Producto no encontrado con el id: %d", id)));

        product.setName(updateProductRequest.getName());
        product.setDescription(updateProductRequest.getDescription());
        product.setPrice(updateProductRequest.getPrice());
        product.setAvailable(updateProductRequest.getAvailable());
        product.setUpdatedAt(OffsetDateTime.now());

        product = productRepository.save(product);
        return ProductMapper.modelToProductResponse(product);
    }

    @Override
    public void deleteProduct(Integer id) throws Exception {
        if (id == null || id <= 0)
            throw new BadRequestException("Debe ingresar el id para eliminar");
        if (!productRepository.existsById(id))
            throw new NotFoundException(String.format("Producto no encontrado con el id: %d", id));
        productRepository.deleteById(id);
    }
}
