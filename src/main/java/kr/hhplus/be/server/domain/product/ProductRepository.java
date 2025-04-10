package kr.hhplus.be.server.domain.product;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository {

    public Optional<Product> find(Long productId);
    public long findProductCount();
    public List<Product> findAll(ProductCommand command);
    public Product save(Product product);

}
