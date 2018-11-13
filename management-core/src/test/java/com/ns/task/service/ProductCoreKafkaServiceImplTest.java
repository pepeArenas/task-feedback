package com.ns.task.service;

import com.ns.task.config.properties.CommonProperties;
import com.ns.task.entities.ProductEntity;
import com.ns.task.model.ProductDTO;
import com.ns.task.model.ProductDTOBuilder;
import com.ns.task.repositories.ProductRepository;
import com.ns.task.services.ProductService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Spy;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class ProductCoreKafkaServiceImplTest {
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
        final ProductEntity productEntity = new ProductEntity();
        productEntity.setId(1);
        productEntity.setName("SCREWDRIVER");
        productEntity.setModel("S090");
        productEntity.setPrice(new BigDecimal("12.2"));
        productEntities.add(productEntity);
        when(repository.retrieveProducts()).thenReturn(productEntities);
        final List<ProductDTO> products = productService.getProducts();
        assertEquals(products.size(), 1);
        assertProduct(products.get(0), productEntities.get(0));
    }

    @Test
    public void getNoProducts() {
        when(repository.retrieveProducts()).thenReturn(productEntities);
        final List<ProductDTO> products = productService.getProducts();
        assertEquals(products.size(), 0);
    }

    @Test
    public void insertProduct() throws DataIntegrityViolationException {
        final ProductDTO productDTO = new ProductDTOBuilder()
                .setName("SCREWDRIVER")
                .setPrice(new BigDecimal("12.20"))
                .setModel("S090")
                .createProductDTO();
        final ProductEntity productEntity = new ProductEntity();
        productEntity.setId(1);
        productEntity.setName("SCREWDRIVER");
        productEntity.setModel("S090");
        productEntity.setPrice(new BigDecimal("12.20"));
        when(repository.saveProduct(any(ProductEntity.class))).thenReturn(productEntity);
        final ProductDTO returned = productService.insertProduct(productDTO);
        final ArgumentCaptor<ProductEntity> productArguments = ArgumentCaptor.forClass(ProductEntity.class);
        verify(repository, timeout(1)).saveProduct(productArguments.capture());
        verifyNoMoreInteractions(repository);
        assertProduct(returned, productEntity);
    }

    @Test
    public void shouldThrowAnExceptionWhenInsertDuplicateProduct() {
        final ProductDTO productDTO = new ProductDTOBuilder()
                .setName("SCREWDRIVER")
                .setModel("S090")
                .setPrice(new BigDecimal("12.20"))
                .createProductDTO();
        when(repository.saveProduct(any(ProductEntity.class))).thenThrow(DataIntegrityViolationException.class);
        final ProductDTO product = productService.insertProduct(productDTO);
        assertEquals(CommonProperties.DUPLICATE_PRODUCT, product.getMessage());

    }

    @Test
    public void whenConvertPostEntityToPostDto_thenCorrect() {
        final ProductEntity entity = new ProductEntity();
        entity.setId(1);
        entity.setName("SCREWDRIVER");
        entity.setModel("S090");
        entity.setPrice(new BigDecimal("12.90"));
        final ProductDTO postDto = modelMapper.map(entity, ProductDTO.class);
        assertProduct(postDto, entity);
    }

    private void assertProduct(ProductDTO expected, ProductEntity actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getPrice(), actual.getPrice());
    }

}