package com.ns.task.product;

import com.mx.task.model.ProductDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
public class ProductController {


    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/addProductForm")
    public String showAddProductForm(ModelMap model) {
        model.put("product", new ProductDTO());

        return "addProductForm";
    }

    @GetMapping("/products")
    public String showProductsPage(ModelMap model) {
        model.put("products", productService.getProducts());
        return "products";
    }

    @PostMapping("/addProductForm")
    public String showAddedProductPage(@Valid ProductDTO product, BindingResult result) {
        if (result.hasErrors()) {
            return "addProductForm";
        }
        productService.insertProduct(product);
        return "productAdded";
    }


}
