package com.backend.cali.backendcali.services;

import com.backend.cali.backendcali.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    List<Product> findAll();

    Page<Product> findAll(Pageable pageable);

    Optional <Product> findById (Long id);

    Product save(Product product);

    void remove (Long id);
}
