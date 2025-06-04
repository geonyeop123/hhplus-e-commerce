package kr.hhplus.be.domain.product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ProductRepository {

    Optional<Product> find(Long id);
    Optional<Product> findByIdForUpdate(Long id);
    Page<Product> findAll(Pageable pageable);
    Product save(Product product);
}
