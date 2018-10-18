package com.ns.task.product;


import com.ns.task.product.entities.ProductEntity;
import com.ns.task.product.repositories.ProductRepository;
import com.ns.task.product.service.ProductCoreServiceImpl;
import com.ns.task.services.ProductService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;


@RunWith(SpringRunner.class)
public class ProductCoreServiceImpTest {

    @MockBean
    private ProductRepository repository;
    @Mock
    private ModelMapper modelMapper;


    @Test
    public void getProducts() {
        List<ProductEntity> productEntities = new ArrayList<>();
        ProductEntity productEntity = new ProductEntity();
        productEntity.setId(1);
        productEntity.setName("SCREWDRIVER");
        productEntity.setModel("S090");
        productEntity.setPrice(new BigDecimal("12.2"));

        productEntities.add(productEntity);

        ProductService productService = new ProductCoreServiceImpl(modelMapper, repository);
        when(repository.findAll()).thenReturn(productEntities);
        productService.getProducts();
        Assert.assertEquals(productEntities.size(), 1);
    }

    @Test
    public void getNoProducts() {
        List<ProductEntity> productEntities = new ArrayList<>();
        ProductService productService = new ProductCoreServiceImpl(modelMapper, repository);
        when(repository.findAll()).thenReturn(productEntities);
        productService.getProducts();
        Assert.assertEquals(productEntities.size(), 0);
    }
}
