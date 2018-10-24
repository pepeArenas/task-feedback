package com.ns.task.services;


import com.ns.task.entities.ProductEntity;
import com.ns.task.model.ProductDTO;
import com.ns.task.repositories.ProductRepository;
import com.ns.task.service.ProductCoreServiceImpl;
import org.assertj.core.api.Assertions;
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
import java.sql.SQLIntegrityConstraintViolationException;
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
    private List<ProductEntity> productEntities;

    @Before
    public void setUp() {
        productService = new ProductCoreServiceImpl(modelMapper, repository);
        productEntities = new ArrayList<>();
    }

    @Test
    public void getProducts() {
        ProductEntity productEntity = new ProductEntity();
        productEntity.setId(1);
        productEntity.setName("SCREWDRIVER");
        productEntity.setModel("S090");
        productEntity.setPrice(new BigDecimal("12.2"));
        productEntities.add(productEntity);
        when(repository.retrieveProducts()).thenReturn(productEntities);
        List<ProductDTO> products = productService.getProducts();
        assertEquals(products.size(), 1);
        assertProduct(products.get(0), productEntities.get(0));
    }

    @Test
    public void getNoProducts() {
        when(repository.retrieveProducts()).thenReturn(productEntities);
        List<ProductDTO> products = productService.getProducts();
        assertEquals(products.size(), 0);
    }

    @Test
    public void insertProduct() throws SQLIntegrityConstraintViolationException {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setName("SCREWDRIVER");
        productDTO.setModel("S090");
        productDTO.setPrice(new BigDecimal("12.20"));
        ProductEntity productEntity = new ProductEntity();
        productEntity.setId(1);
        productEntity.setName("SCREWDRIVER");
        productEntity.setModel("S090");
        productEntity.setPrice(new BigDecimal("12.20"));
        when(repository.saveProduct(any(ProductEntity.class))).thenReturn(productEntity);
        ProductDTO returned = productService.insertProduct(productDTO);
        ArgumentCaptor<ProductEntity> productArguments = ArgumentCaptor.forClass(ProductEntity.class);
        verify(repository, timeout(1)).saveProduct(productArguments.capture());
        verifyNoMoreInteractions(repository);
        assertProduct(returned, productEntity);
    }

    @Test
    public void shouldThrowAnExceptionWhenInsertDuplicateProduct() {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setName("SCREWDRIVER");
        productDTO.setModel("S090");
        productDTO.setPrice(new BigDecimal("12.20"));
        try {
            when(repository.saveProduct(any(ProductEntity.class))).thenThrow(SQLIntegrityConstraintViolationException.class);
            productService.insertProduct(productDTO);
        } catch (SQLIntegrityConstraintViolationException integrity) {
            Assertions.assertThat(integrity instanceof SQLIntegrityConstraintViolationException);
        }

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
