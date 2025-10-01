package com.codewithmosh.store.products;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-10-01T17:05:25+0200",
    comments = "version: 1.6.3, compiler: javac, environment: Java 17.0.16 (Amazon.com Inc.)"
)
@Component
public class ProductMapperImpl implements ProductMapper {

    @Override
    public ProductDto toDto(Product product) {
        if ( product == null ) {
            return null;
        }

        ProductDto productDto = new ProductDto();

        productDto.setCategoryId( productCategoryId( product ) );
        productDto.setId( product.getId() );
        productDto.setName( product.getName() );
        productDto.setDescription( product.getDescription() );
        productDto.setPrice( product.getPrice() );

        return productDto;
    }

    @Override
    public Product toEntity(ProductDto productDto) {
        if ( productDto == null ) {
            return null;
        }

        Product.ProductBuilder product = Product.builder();

        product.id( productDto.getId() );
        product.name( productDto.getName() );
        product.description( productDto.getDescription() );
        product.price( productDto.getPrice() );

        return product.build();
    }

    @Override
    public void toUpdate(ProductDto productDto, Product product) {
        if ( productDto == null ) {
            return;
        }

        product.setName( productDto.getName() );
        product.setDescription( productDto.getDescription() );
        product.setPrice( productDto.getPrice() );
    }

    private Byte productCategoryId(Product product) {
        Category category = product.getCategory();
        if ( category == null ) {
            return null;
        }
        return category.getId();
    }
}
