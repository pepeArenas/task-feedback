package com.ns.task.services;

import com.ns.task.model.ProductDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface
ProductService {
    List<ProductDTO> getProducts();

    ProductDTO insertProduct(ProductDTO product);
}
