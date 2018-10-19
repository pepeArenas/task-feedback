package com.ns.task.product;


import com.ns.task.model.ProductDTO;
import com.ns.task.product.entities.ProductEntity;
import com.ns.task.product.repositories.ProductRepository;
import com.ns.task.product.service.ProductCoreServiceImpl;
import com.ns.task.services.ProductService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Spy;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;


@RunWith(SpringRunner.class)
public class ProductCoreServiceImpTest {
    @Mock
    private ProductService productService;
    @MockBean
    private ProductRepository repository;
    @Spy
    private ModelMapper modelMapper;

    @Before
    public void setUp() throws Exception {
        productService = new ProductCoreServiceImpl(modelMapper, repository);
    }

    @Test
    public void getProducts() {
        List<ProductEntity> productEntities = new ArrayList<>();
        ProductEntity productEntity = new ProductEntity();
        productEntity.setId(1);
        productEntity.setName("SCREWDRIVER");
        productEntity.setModel("S090");
        productEntity.setPrice(new BigDecimal("12.2"));
        productEntities.add(productEntity);
        when(repository.findAll()).thenReturn(productEntities);
        productService.getProducts();
        assertEquals(productEntities.size(), 1);
    }

    @Test
    public void getNoProducts() {
        List<ProductEntity> productEntities = new ArrayList<>();
        when(repository.findAll()).thenReturn(productEntities);
        productService.getProducts();
        assertEquals(productEntities.size(), 0);
    }

    @Test
    public void insertProduct() {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setName("SCREWDRIVER");
        productDTO.setModel("S090");
        productDTO.setPrice(new BigDecimal("12.20"));
        ProductEntity productEntity = new ProductEntity();
        productEntity.setId(1);
        productEntity.setName("SCREWDRIVER");
        productEntity.setModel("S090");
        productEntity.setPrice(new BigDecimal("12.20"));
        when(repository.save(any(ProductEntity.class))).thenReturn(productEntity);
        ProductDTO returned = productService.insertProduct(productDTO);
        ArgumentCaptor<ProductEntity> productArguments = ArgumentCaptor.forClass(ProductEntity.class);
        verify(repository, timeout(1)).save(productArguments.capture());
        verifyNoMoreInteractions(repository);
        assertProduct(returned, productEntity);
    }


    @Test
    public void whenConvertPostEntityToPostDto_thenCorrect() {
        ProductEntity entity = new ProductEntity();
        entity.setId(1);
        entity.setName("SCREWDRIVER");
        entity.setModel("S090");
        entity.setPrice(new BigDecimal("12.90"));

        ProductDTO postDto = modelMapper.map(entity, ProductDTO.class);
        assertProduct(postDto, entity);
    }

    private void assertProduct(ProductDTO expected, ProductEntity actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getPrice(), actual.getPrice());
    }

}
