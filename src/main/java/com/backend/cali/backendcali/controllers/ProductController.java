package com.backend.cali.backendcali.controllers;

import com.backend.cali.backendcali.entities.Product;
import com.backend.cali.backendcali.services.ProductService;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping ("/products")
@CrossOrigin (originPatterns = "*")
public class ProductController {
    @Autowired
    private ProductService service;

    @GetMapping
    public List<Product> list(){

        return service.findAll();
    }

    @GetMapping("/page/{page}")
    public Page<Product> list(@PathVariable Integer page){
        Pageable pageable= PageRequest.of(page, 8);
        return service.findAll(pageable);
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> show(@PathVariable Long id){
        Optional <Product> productOptional = service.findById(id);
        if (productOptional.isPresent()){
            return ResponseEntity.ok(productOptional.orElseThrow());
        }
        return ResponseEntity.notFound().build();
    }
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody Product product, BindingResult result){
        if (result.hasErrors()){
            return validation(result);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(product));
    }

    @PutMapping("{id}")
    public ResponseEntity<?> update (@Valid @RequestBody Product product, BindingResult result, @PathVariable Long id){
        if (result.hasErrors()){
            return validation(result);
        }
        Optional<Product> o = service.findById(id);
        if (o.isPresent()){
            Product productDb = o.orElseThrow();
            productDb.setDescripcion(product.getDescripcion());
            productDb.setCantidad(product.getCantidad());
            return ResponseEntity.status(HttpStatus.CREATED).body(service.save(productDb));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> remove (@PathVariable Long id){
        Optional<Product> o = service.findById(id);
        if (o.isPresent()){
            service.remove(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
    private ResponseEntity<?> validation (BindingResult result){
        Map<String, String> errors= new HashMap<>();

        result.getFieldErrors().forEach(err -> {
            errors.put(err.getField(), "El campo " + err.getField() + " " + err.getDefaultMessage());

        });
        return ResponseEntity.badRequest().body(errors);

    }
}

