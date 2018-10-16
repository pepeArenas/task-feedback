package com.ns.task;

import com.mx.task.model.ProductDTO;
import com.ns.task.product.ProductController;
import com.ns.task.product.ProductService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.isA;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;


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
        doNothing().when(service).insertProduct(isA(ProductDTO.class));
        this.mockMvc.perform(post("/product")).andDo(print()).andExpect(status().
                isOk()).andExpect(view().name("productAdded"));
    }

    @Test
    public void trySaveProductWithNindingError() throws Exception {
        mockMvc.perform(post("/product")
                .param("name", "")
                .param("model", "a"))
                .andExpect(view().name("addProduct"))
                .andExpect(model().errorCount(2));
    }

    @Test
    public void showProducts() throws Exception {
        this.mockMvc.perform(get("/products")).andDo(print()).andExpect(status().
                isOk()).andExpect(view().name("products"));
    }

}

