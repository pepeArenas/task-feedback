package com.ns.task.controllers;

import com.ns.task.model.ProductDTO;
import com.ns.task.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
    public String saveProduct(@ModelAttribute("product") @Valid ProductDTO product, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "addProduct";
        }
        ProductDTO productDTO = productService.insertProduct(product);
        if (productDTO != null && productDTO.getMessage() != null) {
            model.addAttribute("messageException", productDTO.getMessage());
            return "managementError";
        }
        if (productDTO != null) {
            model.addAttribute("name", productDTO.getName());
            model.addAttribute("model", productDTO.getModel());
        }
        return "productAdded";
    }

    @GetMapping("/products")
    public String showProducts(ModelMap model) {
        model.put("products", productService.getProducts());
        return "products";
    }


}
