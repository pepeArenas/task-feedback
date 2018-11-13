package com.ns.task.controllers;

import com.ns.task.config.properties.CommonProperties;
import com.ns.task.model.ProductDTO;
import com.ns.task.model.ProductDTOBuilder;
import com.ns.task.services.ProductService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(ProductKafkaController.class)
@ActiveProfiles("kafka")
public class ProductKafkaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService service;

    @Test
    public void showAddProduct() throws Exception {
        this.mockMvc.perform(get("/product")).andDo(print()).andExpect(status().
                isOk()).andExpect(view().name("addKafkaProduct"));
    }

    @Test
    public void showProducts() throws Exception {
        this.mockMvc.perform(get("/products")).andDo(print()).andExpect(status().
                isOk()).andExpect(view().name("products"));
    }

    @Test
    public void saveProduct() throws Exception {
        final ProductDTO productDTO = new ProductDTOBuilder().createProductDTO();
        productDTO.setName("SCREWDRIVER");
        productDTO.setModel("SO90");
        productDTO.setPrice(new BigDecimal("12.90"));
        when(service.insertProduct(isA(ProductDTO.class))).thenReturn(productDTO);
        this.mockMvc.perform(post("/product")
                .param("name", "SCREWDRIVER")
                .param("model", "S019")
                .param("price", String.valueOf(new BigDecimal("12.3").toString()))).
                andDo(print()).
                andExpect(status().isOk()).
                andExpect(view().name("productKafkaAdded"));
    }

    @Test
    public void respondWithErrorPageWhenTryingSave() throws Exception {
        final ProductDTO productDTO = new ProductDTOBuilder().createProductDTO();
        productDTO.setName("SCREWDRIVER");
        productDTO.setModel("SO90");
        productDTO.setPrice(new BigDecimal("12.90"));
        productDTO.setMessage(CommonProperties.DUPLICATE_PRODUCT);
        when(service.insertProduct(isA(ProductDTO.class))).thenReturn(productDTO);
        this.mockMvc.perform(post("/product")
                .param("name", "SCREWDRIVER")
                .param("model", "S019")
                .param("price", String.valueOf(new BigDecimal("12.3").toString()))).
                andDo(print()).
                andExpect(status().isOk()).
                andExpect(view().name("productKafkaAdded"));
    }

    @Test
    public void trySaveProductWithNullValues() throws Exception {
        when(service.insertProduct(isA(ProductDTO.class))).thenReturn(new ProductDTOBuilder().createProductDTO());
        this.mockMvc.perform(post("/product"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().errorCount(3))
                .andExpect(view().name("addKafkaProduct"));
    }

    @Test
    public void trySaveProductWithSizeErrors() throws Exception {
        when(service.insertProduct(isA(ProductDTO.class))).thenReturn(new ProductDTOBuilder().createProductDTO());
        mockMvc.perform(post("/product")
                .param("name", "")
                .param("model", "a")
                .param("price", String.valueOf(new BigDecimal("12.3").toString())))
                .andExpect(view().name("addKafkaProduct"))
                .andExpect(model().errorCount(2));
    }
}