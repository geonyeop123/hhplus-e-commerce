package kr.hhplus.be.server.domain.common;

public interface GlobalEventPublisher {
    void publish(String topic, Object key, Object payload);
    void publish(String topic, Object payload);
}
