package kr.hhplus.be.server.application.order;

import kr.hhplus.be.domain.point.Point;
import kr.hhplus.be.domain.product.Product;
import kr.hhplus.be.domain.product.ProductRepository;
import kr.hhplus.be.domain.user.User;
import kr.hhplus.be.infra.order.JpaOrderProductRepository;
import kr.hhplus.be.infra.order.JpaOrderRepository;
import kr.hhplus.be.infra.payment.JpaPaymentRepository;
import kr.hhplus.be.infra.point.JpaPointRepository;
import kr.hhplus.be.infra.product.JpaProductRepository;
import kr.hhplus.be.infra.user.JpaUserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.cache.RedisCacheManager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class OrderFacadeTest {

    @Autowired
    private OrderFacade orderFacade;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private JpaProductRepository jpaProductRepository;

    @Autowired
    private JpaUserRepository jpaUserRepository;

    @Autowired
    private JpaPointRepository jpaPointRepository;

    @Autowired
    private JpaPaymentRepository jpaPaymentRepository;

    @Autowired
    private JpaOrderRepository jpaOrderRepository;

    @Autowired
    private JpaOrderProductRepository jpaOrderProductRepository;

    @Autowired
    private RedisCacheManager redisCacheManager;


    @AfterEach
    void tearDown() {
        jpaPaymentRepository.deleteAllInBatch();
        jpaProductRepository.deleteAllInBatch();
        jpaOrderProductRepository.deleteAllInBatch();
        jpaOrderRepository.deleteAllInBatch();
        jpaPointRepository.deleteAllInBatch();
        jpaUserRepository.deleteAllInBatch();
        jpaProductRepository.deleteAllInBatch();
        Collection<String> cacheNames = redisCacheManager.getCacheNames();
        cacheNames.forEach(cacheName -> Objects.requireNonNull(redisCacheManager.getCache(cacheName)).clear());
    }

    @DisplayName("동시에 동일한 상품으로 주문을 해도 정상적으로 재고가 차감된다.")
    @Test
    void concurrencyOrderByProductDeduct() throws InterruptedException {
        // given
        Product product1 = productRepository.save(Product.create("사과", 10, 10));
        Product product2 = productRepository.save(Product.create("배", 10, 10));


        int threadCount = 12;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);
        AtomicInteger successCnt = new AtomicInteger();
        List<User> users = new ArrayList<>();
        for (int i = 0; i < threadCount; i++) {
            User user = jpaUserRepository.save(User.create("user" + i));
            jpaPointRepository.save(Point.create(user, 1000));
            users.add(user);
        }

        // when
        for (int i = 1; i <= threadCount; i++) {
            Long userId = users.get(i - 1).getId();
            executorService.submit(() -> {
                try{
                    User user = jpaUserRepository.findById(userId).orElseThrow();
                    OrderCriteria.Create criteria = new OrderCriteria.Create(user, null, List.of(
                            new OrderCriteria.Create.OrderItem(product1.getId(), 1),
                            new OrderCriteria.Create.OrderItem(product2.getId(), 1))
                    );
                    orderFacade.order(criteria);
                    successCnt.getAndIncrement();
                }catch(Exception e){
                    e.printStackTrace();
                }finally {
                    latch.countDown();
                }
            });
        }

        latch.await(); // 모든 작업이 끝날 때까지 대기

        // then
        assertThat(successCnt.get()).isNotZero();
        List<Product> products = jpaProductRepository.findAll();
        int leftStock = product1.getStock() - successCnt.get();
        assertThat(products).extracting(Product::getStock)
                .containsOnly(leftStock, leftStock);
    }

    @DisplayName("동일한 유저가 별도의 상품을 주문하더라도 포인트 사용 시 사용된 포인트만 차감된다.")
    @Test
    void concurrencyOrderByPoint() throws InterruptedException {
        // given
        int threadCount = 12;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);
        AtomicInteger successCnt = new AtomicInteger();
        List<Product> products = new ArrayList<>();
        for (int i = 0; i < threadCount; i++) {
            Product product = jpaProductRepository.save(Product.create("product" + i, 10, 10));
            products.add(product);
        }

        User savedUser = jpaUserRepository.save(User.create("user"));
        jpaPointRepository.save(Point.create(savedUser, 100));
        Long userId = savedUser.getId();

        // when
        for (int i = 1; i <= threadCount; i++) {
            Product product = products.get(i - 1);
            executorService.submit(() -> {
                try{
                    User user = jpaUserRepository.findById(userId).orElseThrow();

                    OrderCriteria.Create criteria = new OrderCriteria.Create(user, null
                            , List.of( new OrderCriteria.Create.OrderItem(product.getId(), 1)));
                    orderFacade.order(criteria);
                    successCnt.getAndIncrement();
                }catch(Exception e){
                    e.printStackTrace();
                }finally {
                    latch.countDown();
                }
            });
        }

        latch.await(); // 모든 작업이 끝날 때까지 대기

        // then
        assertThat(successCnt.get()).isNotZero();
        Point point = jpaPointRepository.findByUserId(userId).orElseThrow();
        assertThat(point.getBalance()).isEqualTo(0);
    }

}