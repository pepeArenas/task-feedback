package com.ns.task.services;

import com.ns.task.exceptions.ProductManagementException;
import com.ns.task.model.ProductDTO;

import java.util.List;


public interface ProductService {
    List<ProductDTO> getProducts();

    ProductDTO insertProduct(ProductDTO product) throws ProductManagementException;
}
