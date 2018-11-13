package com.ns.task.controllers;

import com.ns.task.model.ProductDTO;
import com.ns.task.model.ProductDTOBuilder;
import com.ns.task.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.UUID;

@Profile("kafka")
@Controller
public class ProductKafkaController {
    private final ProductService productService;

    @Autowired
    public ProductKafkaController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/product")
    public String showAddProduct(ModelMap model) {
        final String suuid = UUID.randomUUID().toString();
        model.put("suuid", suuid);
        model.addAttribute("active", "active");
        model.put("product", new ProductDTOBuilder().createProductDTO());
        model.put("activeAdd", "active");
        return "addKafkaProduct";
    }

    @GetMapping("/products")
    public String showProducts(ModelMap model) {
        model.put("products", productService.getProducts());
        model.put("activeGet", "active");
        return "products";
    }

    @PostMapping("/product")
    public String saveProduct(@ModelAttribute("product") @Valid ProductDTO product, BindingResult result, Model model) {
        String view = "productKafkaAdded";
        if (result.hasErrors()) {
            view = "addKafkaProduct";
        } else {
            productService.insertProduct(product);
            model.addAttribute("suuid", product.getUUID());
        }
        return view;
    }
}
