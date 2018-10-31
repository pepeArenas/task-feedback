package com.ns.task;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ns.task.model.ProductDTO;

import java.io.IOException;

public class Delete {


    public static void main(String[] args) {
        String JSON = "{\"id\":null,\"model\":\"ererw\",\"name\":\"asdsda\",\"price\":34342,\"message\":null,\"complete\":false}";


        ObjectMapper mapper = new ObjectMapper();
        try {
            ProductDTO productDTO = mapper.readValue(JSON, ProductDTO.class);
            System.out.println(productDTO);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
