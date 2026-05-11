package co.edu.usbcali.ecommerceusb.controller;

import co.edu.usbcali.ecommerceusb.dto.CreateProductCategoryRequest;
import co.edu.usbcali.ecommerceusb.dto.UpdateProductCategoryRequest;
import co.edu.usbcali.ecommerceusb.dto.ProductCategoryResponse;
import co.edu.usbcali.ecommerceusb.service.ProductCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product-category")
public class ProductCategoryController {

    @Autowired
    private ProductCategoryService productCategoryService;

    @GetMapping("/all")
    public List<ProductCategoryResponse> getAll() {
        return productCategoryService.getProductCategories();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductCategoryResponse> getById(@PathVariable Integer id) throws Exception {
        return new ResponseEntity<>(
                productCategoryService.getProductCategoryById(id),
                HttpStatus.OK
        );
    }

    @PostMapping
    public ResponseEntity<ProductCategoryResponse> createProductCategory(
            @RequestBody CreateProductCategoryRequest createProductCategoryRequest) throws Exception {
        return new ResponseEntity<>(
                productCategoryService.createProductCategory(createProductCategoryRequest),
                HttpStatus.CREATED
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateProductCategory(
            @PathVariable Integer id,
            @RequestBody UpdateProductCategoryRequest updateProductCategoryRequest) {
        return new ResponseEntity<>(
                "No es posible actualizar una relación producto-categoría. " +
                "La combinación product_id + category_id es única. " +
                "Si necesita cambiarla, elimine este registro y cree uno nuevo con POST.",
                HttpStatus.BAD_REQUEST
        );
    }
}
