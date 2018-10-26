package com.ns.task.service;

import com.ns.task.entities.ProductEntity;
import com.ns.task.exceptions.ProductManagementException;
import com.ns.task.model.ProductDTO;
import com.ns.task.repositories.ProductRepository;
import com.ns.task.services.ProductService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductCoreServiceImpl implements ProductService {

    private final ModelMapper mapper;
    private final ProductRepository repository;
    private static final Logger logger = LogManager.getLogger();


    @Autowired
    public ProductCoreServiceImpl(ModelMapper mapper, ProductRepository repository) {
        this.mapper = mapper;
        this.repository = repository;
    }

    @Override
    public List<ProductDTO> getProducts() {
        List<ProductEntity> products = repository.retrieveProducts();
        logger.debug("Number of returned products from DB {}", products.size());
        return products.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public ProductDTO insertProduct(ProductDTO product) {
        ProductEntity productEntity = convertToEntity(product);
        try {
            logger.debug("Sending data to DB {}", productEntity);
            productEntity = repository.saveProduct(productEntity);
            logger.debug("Getting persisted data from insert to DB {}", productEntity);
        } catch (DataIntegrityViolationException exception) {
            throw new ProductManagementException("Name and model of the product exists already", exception);
        }
        return convertToDto(productEntity);

    }

    private ProductDTO convertToDto(ProductEntity entity) {
        return mapper.map(entity, ProductDTO.class);
    }

    private ProductEntity convertToEntity(ProductDTO dto) {
        return mapper.map(dto, ProductEntity.class);
    }

}
