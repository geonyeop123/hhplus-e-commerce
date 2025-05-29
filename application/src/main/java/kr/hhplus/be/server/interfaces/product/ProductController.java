package kr.hhplus.be.server.interfaces.product;

import kr.hhplus.be.domain.common.PageResult;
import kr.hhplus.be.domain.product.ProductCommand;
import kr.hhplus.be.domain.product.ProductInfo;
import kr.hhplus.be.domain.product.ProductService;
import kr.hhplus.be.server.interfaces.common.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ProductController implements ProductDocs{

    private final ProductService productService;

    @GetMapping("/api/v1/products/{productId}")
    public ResponseEntity<ProductResponse> product(@PathVariable Long productId){
        ProductInfo product = productService.find(productId);
        return ResponseEntity.ok(ProductResponse.from(product));
    }

    @GetMapping("/api/v1/products")
    public ResponseEntity<PageResponse<ProductResponse>> products(ProductRequest.Products request) {
        ProductCommand.FindAll command = request.toCommand();

        PageResult<ProductInfo> result = productService.findAll(command);

        PageResponse<ProductResponse> response = new PageResponse<>(
                result.content().stream().map(ProductResponse::from).toList()
                , result.page(), result.size(), result.totalCount(), result.totalPages());

        return ResponseEntity.ok(response);
    }

}
