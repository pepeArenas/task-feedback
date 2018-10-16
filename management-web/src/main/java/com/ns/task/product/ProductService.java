package com.ns.task.product;

import com.mx.task.model.ProductDTO;

import java.util.List;


public interface ProductService {
    List<ProductDTO> getProducts();

    void insertProduct(ProductDTO product);
}
