package kr.hhplus.be;

import jakarta.annotation.PreDestroy;
import org.springframework.context.annotation.Configuration;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.utility.DockerImageName;

@Configuration
class TestcontainersConfiguration {

	public static MySQLContainer<?> MYSQL_CONTAINER;
	private static GenericContainer<?> KAFKA_CONTAINER;

	static {
		setKafka();
	}

	private static void setKafka() {
		KAFKA_CONTAINER = new GenericContainer<>(DockerImageName.parse("apache/kafka:4.0.0"))
				.withExposedPorts(9092);  // KRaft 모드 기본 리스너 포트
		KAFKA_CONTAINER.start();

		String bootstrapServers =
				KAFKA_CONTAINER.getHost() + ":" + KAFKA_CONTAINER.getFirstMappedPort();
		System.setProperty("spring.kafka.bootstrap-servers", bootstrapServers);
	}

	private static void setMySQL() {
		MYSQL_CONTAINER = new MySQLContainer<>(DockerImageName.parse("mysql:8.0"))
			.withDatabaseName("hhplus")
			.withUsername("test")
			.withPassword("test");
		MYSQL_CONTAINER.start();

		System.setProperty("spring.datasource.url", MYSQL_CONTAINER.getJdbcUrl() + "?characterEncoding=UTF-8&serverTimezone=UTC");
		System.setProperty("spring.datasource.username", MYSQL_CONTAINER.getUsername());
		System.setProperty("spring.datasource.password", MYSQL_CONTAINER.getPassword());
	}

	@PreDestroy
	public void preDestroy() {
		if (MYSQL_CONTAINER.isRunning()) {
			MYSQL_CONTAINER.stop();
		}

		if(KAFKA_CONTAINER.isRunning()) {
			KAFKA_CONTAINER.stop();
		}
	}
}