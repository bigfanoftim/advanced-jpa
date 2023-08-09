package com.bigfanoftim.advancedjpa.product.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ProductTest {
    
    @Autowired ProductRepository productRepository;
    
    @Test
    public void save() throws Exception {
        Product product = new Product("A");
        productRepository.save(product);
    }
}