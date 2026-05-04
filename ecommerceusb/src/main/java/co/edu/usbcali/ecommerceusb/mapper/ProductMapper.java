package co.edu.usbcali.ecommerceusb.mapper;

import co.edu.usbcali.ecommerceusb.dto.CreateProductRequest;
import co.edu.usbcali.ecommerceusb.dto.ProductResponse;
import co.edu.usbcali.ecommerceusb.model.Product;

import java.time.OffsetDateTime;
import java.util.List;

public class ProductMapper {

    public static ProductResponse modelToProductResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .available(product.getAvailable())
                .build();
    }

    public static List<ProductResponse> modelToProductResponseList(List<Product> products) {
        return products.stream().map(ProductMapper::modelToProductResponse).toList();
    }

    public static Product createProductRequestToProduct(CreateProductRequest createProductRequest) {
        OffsetDateTime datetime = OffsetDateTime.now();
        return Product.builder()
                .name(createProductRequest.getName())
                .description(createProductRequest.getDescription())
                .price(createProductRequest.getPrice())
                .available(createProductRequest.getAvailable())
                .createdAt(datetime)
                .updatedAt(datetime)
                .build();
    }
}
