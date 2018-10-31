package com.ns.task;

import com.ns.task.controllers.ProductController;
import com.ns.task.model.ProductDTO;
import com.ns.task.services.ProductService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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
@WebMvcTest(ProductController.class)
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService service;

    @Test
    public void showAddProduct() throws Exception {
        this.mockMvc.perform(get("/product")).andDo(print()).andExpect(status().
                isOk()).andExpect(view().name("addProduct"));
    }

    @Test
    public void saveProduct() throws Exception {
        ProductDTO productDTO = new ProductDTO();
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
                andExpect(view().name("productAdded"));
    }

    @Test
    public void respondWithErrorPageWhenTryingSave() throws Exception {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setName("SCREWDRIVER");
        productDTO.setModel("SO90");
        productDTO.setPrice(new BigDecimal("12.90"));
        productDTO.setMessage("Name and model already exists");
        when(service.insertProduct(isA(ProductDTO.class))).thenReturn(productDTO);
        this.mockMvc.perform(post("/product")
                .param("name", "SCREWDRIVER")
                .param("model", "S019")
                .param("price", String.valueOf(new BigDecimal("12.3").toString()))).
                andDo(print()).
                andExpect(status().isOk()).
                andExpect(view().name("managementError"));
    }

    @Test
    public void trySaveProductWithNullValues() throws Exception {
        when(service.insertProduct(new ProductDTO())).thenReturn(new ProductDTO());
        this.mockMvc.perform(post("/product")).
                andDo(print()).
                andExpect(status().isOk()).
                andExpect(view().name("addProduct"));
    }

    @Test
    public void trySaveProductWithSizeErrors() throws Exception {
        mockMvc.perform(post("/product")
                .param("name", "")
                .param("model", "a")
                .param("price", String.valueOf(new BigDecimal("12.3").toString())))
                .andExpect(view().name("addProduct"))
                .andExpect(model().errorCount(2));
    }

    @Test
    public void showProducts() throws Exception {
        this.mockMvc.perform(get("/products")).andDo(print()).andExpect(status().
                isOk()).andExpect(view().name("products"));
    }

}

