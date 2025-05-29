package kr.hhplus.be.infra.product;

import kr.hhplus.be.domain.product.Product;
import kr.hhplus.be.domain.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepository {

    private final JpaProductRepository jpaProductRepository;

    @Override
    public Optional<Product> find(Long id) {
        return jpaProductRepository.findById(id);
    }

    @Override
    public Optional<Product> findByIdForUpdate(Long id) {
        return jpaProductRepository.findByIdForUpdate(id);
    }

    @Override
    public Page<Product> findAll(Pageable pageable) {
        return jpaProductRepository.findAll(pageable);
    }

    @Override
    public Product save(Product product) {
        return jpaProductRepository.save(product);
    }
}
