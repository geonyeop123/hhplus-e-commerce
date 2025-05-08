package kr.hhplus.be.server.support.config.redis;

import org.springframework.context.annotation.Configuration;

@Configuration
public class RedisConfig {

//    @Bean
//    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
//        RedisTemplate<String, Object> template = new RedisTemplate<>();
//        template.setConnectionFactory(factory);
//
//        // 키는 문자열
//        template.setKeySerializer(new StringRedisSerializer());
//        template.setHashKeySerializer(new StringRedisSerializer());
//
//        // 값은 JSON 직렬화
//        RedisSerializer<Object> java = RedisSerializer.java();
//        template.setValueSerializer(java);
//        template.setHashValueSerializer(java);
//
//        template.afterPropertiesSet();
//        return template;
//    }
}