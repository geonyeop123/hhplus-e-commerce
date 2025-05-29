package kr.hhplus.be.support.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedissonConfig {

    @Bean(destroyMethod="shutdown")
    public RedissonClient redissonClient() {
        Config config = new Config();
        // 단일 서버 모드 설정
        config.useSingleServer()
                .setAddress("redis://localhost:6379")
                .setConnectionPoolSize(64)
                .setConnectionMinimumIdleSize(24)
                .setSubscriptionConnectionPoolSize(50)
                .setIdleConnectionTimeout(10000)
                .setConnectTimeout(10000)
                .setTimeout(3000)
                .setRetryAttempts(3)
                .setRetryInterval(1500);

        return Redisson.create(config);
    }
}