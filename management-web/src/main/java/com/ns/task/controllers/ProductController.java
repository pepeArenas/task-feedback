package com.ns.task.product;

import com.ns.task.model.ProductDTO;
import com.ns.task.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
public class ProductController {


    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/product")
    public String showAddProduct(ModelMap model) {
        model.put("product", new ProductDTO());

        return "addProduct";
    }

    @PostMapping("/product")
    public String saveProduct(@ModelAttribute("product") @Valid ProductDTO product, BindingResult result) {
        if (result.hasErrors()) {

            return "addProduct";
        }
        productService.insertProduct(product);
        return "productAdded";
    }

    @GetMapping("/products")
    public String showProducts(ModelMap model) {
        model.put("products", productService.getProducts());
        return "products";
    }


}