# Redis를 활용한 캐시 보고서

수 많은 주문이 발생하는 경우, 현재 제공되는 '인기 상품 조회' API는 DB에 큰 부하를 발생시킬 수 있습니다.

이에 따라서 Redis를 활용하여 해당 데이터를 캐싱하여 DB를 조회하지 않더라도 레디스 메모리를 활용하도록 하겠습니다.

```java
    @Transactional(readOnly = true)
    @Cacheable(
            cacheNames = "popularProducts",
            key        = "#command.startDate().toString() + '_' + #command.endDate().toString()"
    )
    public PopularProducts getPopularProducts(StatsCommand.PopularProducts command) {
        return new PopularProducts(statsRepository.getPopularProducts(command.startDate(), command.endDate()));
    }
```

해당 데이터의 TTL은 25시간으로 세팅해두었으며, 자정에 해당 데이터들이 업데이트됩니다. 
또한, key세팅을 통하여 로테이션 방식으로 해당 데이터를 캐싱하게됩니다.



```java
    @DisplayName("3일간 가장 판매가 많았던 상품 5개를 조회할 수 있다.")
@Test
@Sql(scripts = "/sql/popularProduct.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
void popularProducts() {
    // given // when
    Objects.requireNonNull(redisCacheManager.getCache("popularProducts")).clear();
    StatsCommand.PopularProducts command = new StatsCommand.PopularProducts(LocalDate.now().minusDays(3), LocalDate.now().minusDays(1));
    PopularProducts popularProducts = statsService.getPopularProducts(command);

    // then
    assertThat(popularProducts.getProducts()).hasSize(5);
}

@DisplayName("3일간 가장 판매가 많았던 상품이 조회되지 않는 경우 빈 배열이 반환된다.")
@Test
void emptyPopularProducts() {
    // given // when
    Objects.requireNonNull(redisCacheManager.getCache("popularProducts")).clear();
    StatsCommand.PopularProducts command = new StatsCommand.PopularProducts(LocalDate.now().minusDays(3), LocalDate.now().minusDays(1));
    PopularProducts popularProducts = statsService.getPopularProducts(command);

    // then
    assertThat(popularProducts.getProducts()).isEmpty();
}
```
캐싱과는 별개로 기존 기능들이 구현되게 하기 위해 캐시 데이터를 클렌징하였습니다.

```java
    @BeforeEach
    void setUp() {
        // 캐시 초기화
        Cache cache = redisCacheManager.getCache("popularProducts");
        if (cache != null) {
            cache.clear();
        }
    }

    @DisplayName("해당 데이터를 최초로 조회하는 경우 캐시 데이터에 저장된다.")
    @Test
    @Sql(scripts = "/sql/popularProduct.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void saveCacheData() {
        // given
        LocalDate end = LocalDate.now().minusDays(1);
        LocalDate start = end.minusDays(2);
        // cache key 계산 (service 메서드의 @Cacheable key 전략과 동일하게)
        String cacheKey = start.toString() + "_" + end.toString();

        // when
        StatsCommand.PopularProducts command = new StatsCommand.PopularProducts(start, end);

        statsService.getPopularProducts(command);

        // 캐시에 데이터가 저장되었는지 확인
        Cache.ValueWrapper wrapper = redisCacheManager
                .getCache("popularProducts")
                .get(cacheKey);
        assertThat(wrapper).isNotNull();
        PopularProducts products = (PopularProducts)wrapper.get();
        assertThat(products).isNotNull();
        assertThat(products.getProducts()).isNotNull();
        assertThat(products.getProducts()).hasSize(5);
    }
```

캐싱 데이터가 실제로 저장되는지 확인하기 위하여 별도의 테스트를 구성하였으며, 정상적으로 통과되는 것을 확인하였습니다.