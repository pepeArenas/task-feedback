package com.ns.task.controllers;

import com.ns.task.model.ProductDTO;
import com.ns.task.model.ProductDTOBuilder;
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
        model.addAttribute("active", "active");
        model.put("product", new ProductDTOBuilder().createProductDTO());
        model.put("activeAdd", "active");
        return "addProduct";
    }

    @GetMapping("/products")
    public String showProducts(ModelMap model) {
        model.put("products", productService.getProducts());
        model.put("activeGet", "active");
        return "products";
    }

    @PostMapping("/product")
    public String saveProduct(@ModelAttribute("product") @Valid ProductDTO product, BindingResult result, Model model) {
        String view = "productAdded";
        if (result.hasErrors()) {
            view = "addProduct";
        } else {
            final ProductDTO productDTO = productService.insertProduct(product);
                if (productDTO.getMessage() != null) {
                model.addAttribute("messageException", productDTO.getMessage());
                view = "managementError";
            }
            model.addAttribute("name", productDTO.getName());
            model.addAttribute("model", productDTO.getModel());
        }

        return view;
    }
}
