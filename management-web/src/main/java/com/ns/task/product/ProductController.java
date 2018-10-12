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

    @Autowired
    ProductService productService;


    @GetMapping("product")
    public String showHomePage(ModelMap model) {
        model.put("product", new ProductDTO());

        return "product";
    }

    @GetMapping("/products")
    public String showProductsPage(ModelMap model) {
        model.put("products", productService.getProducts());
        return "products";
    }

    @PostMapping("/product")
    public String showAddedProductPage(ModelMap model, @Valid ProductDTO product, BindingResult result) {
        if (result.hasErrors()) {
            return "product";
        }
        productService.insertProduct(product);
        return "menu";
    }


}
