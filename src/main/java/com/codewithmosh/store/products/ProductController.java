package com.codewithmosh.store.products;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;
import java.util.List;
import static java.util.Objects.*;

@RestController
@AllArgsConstructor
@RequestMapping("/products")
public class ProductController {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper productMapper;

    @GetMapping
    public List<ProductDto> getProducts(@RequestParam(name = "categoryId", required = false) Byte categoryId) {
        List<Product> products;
        if (nonNull(categoryId)) {
            products = productRepository.findAllByCategoryId(categoryId);
        } else {
            products = productRepository.findAllWithCategory();
        }
        return products.stream().map(productMapper::toDto).toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProduct(@PathVariable Long id) {
        Product product = productRepository.findById(id).orElse(null);
        if (isNull(product)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(productMapper.toDto(product));
    }

    @PostMapping
    public ResponseEntity<ProductDto> createProduct(@RequestBody ProductDto request, UriComponentsBuilder uriBuilder) {
        Category category = categoryRepository.findById(request.getCategoryId()).orElse(null);
        if (isNull(category)) {
            return ResponseEntity.notFound().build();
        }
        Product product = productMapper.toEntity(request);
        product.setCategory(category);
        productRepository.save(product);
        ProductDto productDto = productMapper.toDto(product);
        return ResponseEntity.created(uriBuilder.path("/products/{id}").buildAndExpand(productDto.getId()).toUri()).body(productDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> createProduct(@PathVariable Long id, @RequestBody ProductDto productDto) {
        Product product = productRepository.findById(id).orElse(null);
        if (isNull(product)) {
            return ResponseEntity.notFound().build();
        }
        productMapper.toUpdate(productDto, product);
        if(!product.getCategory().getId().equals(productDto.getCategoryId())){
            Category category = categoryRepository.findById(productDto.getCategoryId()).orElse(null);
            if (isNull(category)) {
                return ResponseEntity.notFound().build();
            }
            product.setCategory(category);
        }
        productRepository.save(product);
        return ResponseEntity.ok(productMapper.toDto(product));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ProductDto> deleteProduct(@PathVariable Long id) {
        Product product = productRepository.findById(id).orElse(null);
        if (isNull(product)) {
            return ResponseEntity.notFound().build();
        }
        productRepository.delete(product);
        return ResponseEntity.noContent().build();
    }
}
