package com.ssafy.jansorry.config;

import static com.ssafy.jansorry.common.type.RedisDatabaseType.*;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
public class RedisConfig {

	@Value("${spring.data.redis.host}")
	private String host;

	@Value("${spring.data.redis.password}")
	private String password;

	@Value("${spring.data.redis.port}")
	private int port;

	private LettuceConnectionFactory createLettuceConnectionFactory(int database) {
		RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
		redisStandaloneConfiguration.setHostName(host);
		redisStandaloneConfiguration.setPort(port);
		redisStandaloneConfiguration.setPassword(password);
		redisStandaloneConfiguration.setDatabase(database);

		return new LettuceConnectionFactory(redisStandaloneConfiguration);
	}

	@Bean
	public RedisTemplate<String, Object> tokenRedisTemplate() {
		RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(createLettuceConnectionFactory(TOKEN_DB_IDX.ordinal()));
		return redisTemplate;
	}

	@Bean
	public RedisTemplate<String, Object> favoriteRedisTemplate() {
		RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(createLettuceConnectionFactory(FOLLOW_DB_IDX.ordinal()));
		return redisTemplate;
	}

	@Bean
	public RedisTemplate<String, Object> followRedisTemplate() {
		RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(createLettuceConnectionFactory(FAVORITE_DB_IDX.ordinal()));
		return redisTemplate;
	}
}