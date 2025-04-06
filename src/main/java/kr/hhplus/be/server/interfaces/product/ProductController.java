package kr.hhplus.be.server.interfaces.product;

import kr.hhplus.be.server.interfaces.product.response.ProductResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ProductController implements ProductDocs{

    @GetMapping("/api/v1/products/{productId}")
    public ResponseEntity<ProductResponse> product(@PathVariable Long productId){
        return ResponseEntity.ok(new ProductResponse(productId, "맥북", 50, 100000));
    }

    @GetMapping("/api/v1/products")
    public ResponseEntity<List<ProductResponse>> products(){
        return ResponseEntity.ok(List.of(new ProductResponse(1L, "맥북", 50, 100000)));
    }

}
