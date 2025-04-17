package kr.hhplus.be.server.interfaces.product;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.hhplus.be.server.domain.common.PageResult;
import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.product.ProductCommand;
import kr.hhplus.be.server.domain.product.ProductService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
@ExtendWith(MockitoExtension.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ProductService productService;

    @Test
    @DisplayName("productId에 해당하는 상품이 있는 경우 해당 상품을 반환한다.")
    void get_api_v1_products_have_id_200() throws Exception {
        //given
        Long productId = 1L;
        Product product = Product.create("사과",5000,50);
        when(productService.find(productId)).thenReturn(product);

        //when //then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/products/{productId}", productId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productId").value(product.getId()))
                .andExpect(jsonPath("$.name").value(product.getName()))
                .andExpect(jsonPath("$.stock").value(product.getStock()))
                .andExpect(jsonPath("$.price").value(product.getPrice()))
            ;
    }

    @DisplayName("모든 상품 목록을 반환한다.")
    @Test
    void get_api_v1_products_200() throws Exception{
        // given
        ProductRequest.Products req = new ProductRequest.Products(1, 10);
        ProductCommand.FindAll command = req.toCommand();
        Product product = Product.create("사과",5000,50);
        PageResult<Product> result = PageResult.create(List.of(product), command.pageNo(), command.pageSize(), 1);
        when(productService.findAll(command)).thenReturn(result);

        //when //then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/products")
                        .queryParam("pageNo", String.valueOf(req.pageNo()))
                        .queryParam("pageSize", String.valueOf(req.pageSize())))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.page").value(req.pageNo()))
                .andExpect(jsonPath("$.size").value(req.pageSize()))
                .andExpect(jsonPath("$.totalCount").value(result.totalCount()))
                .andExpect(jsonPath("$.totalPages").value(result.totalPages()))
        ;
    }
}