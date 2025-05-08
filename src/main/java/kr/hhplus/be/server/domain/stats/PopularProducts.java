package kr.hhplus.be.server.domain.stats;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class PopularProducts {
     private List<PopularProduct> products = new ArrayList<>();

     public PopularProducts(List<PopularProduct> products) {
         this.products = products;
     }
 }
