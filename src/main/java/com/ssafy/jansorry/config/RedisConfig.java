package com.ssafy.jansorry.config;

import static com.ssafy.jansorry.common.type.RedisDatabaseType.*;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.ssafy.jansorry.favorite.dto.FavoriteDto;
import com.ssafy.jansorry.follow.dto.FollowDto;
import com.ssafy.jansorry.receipt.dto.ReceiptRankDto;

@Configuration
@EnableTransactionManagement
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

		LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory(redisStandaloneConfiguration);
		lettuceConnectionFactory.afterPropertiesSet();

		return lettuceConnectionFactory;
	}

	// 토큰 템플릿
	@Bean
	public RedisTemplate<String, Object> tokenRedisTemplate() {
		RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(createLettuceConnectionFactory(TOKEN_DB_IDX.ordinal()));
		redisTemplate.setKeySerializer(new StringRedisSerializer());
		redisTemplate.setValueSerializer(new StringRedisSerializer());
		return redisTemplate;
	}

	// 팔로우 템플릿
	@Bean
	public RedisTemplate<String, Object> followRedisTemplate() {
		RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(createLettuceConnectionFactory(FOLLOW_DB_IDX.ordinal()));
		redisTemplate.setKeySerializer(new StringRedisSerializer());
		// Value 직렬화를 위한 ObjectMapper 설정
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());
		objectMapper.registerModule(new Jdk8Module());
		objectMapper.registerModule(new ParameterNamesModule());
		// Value 직렬화 설정
		Jackson2JsonRedisSerializer<FollowDto> serializer = new Jackson2JsonRedisSerializer<>(FollowDto.class);
		serializer.setObjectMapper(objectMapper);
		redisTemplate.setValueSerializer(serializer);
		return redisTemplate;
	}

	// 좋아요 템플릿
	@Bean
	public RedisTemplate<String, Object> favoriteRedisTemplate() {
		RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(createLettuceConnectionFactory(FAVORITE_DB_IDX.ordinal()));
		redisTemplate.setKeySerializer(new StringRedisSerializer());
		// Value 직렬화를 위한 ObjectMapper 설정
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());
		objectMapper.registerModule(new Jdk8Module());
		objectMapper.registerModule(new ParameterNamesModule());
		// Value 직렬화 설정
		Jackson2JsonRedisSerializer<FavoriteDto> serializer = new Jackson2JsonRedisSerializer<>(FavoriteDto.class);
		serializer.setObjectMapper(objectMapper);
		redisTemplate.setValueSerializer(serializer);
		return redisTemplate;
	}

	// 팔로우 zset 템플릿
	@Bean
	public RedisTemplate<String, Object> followZSetRedisTemplate() {
		RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(createLettuceConnectionFactory(FOLLOW_DB_IDX.ordinal()));
		redisTemplate.setKeySerializer(new StringRedisSerializer());
		redisTemplate.setValueSerializer(new StringRedisSerializer()); // ZSet 값에 대한 직렬화

		return redisTemplate;
	}

	// 좋아요 zset 템플릿
	@Bean
	public RedisTemplate<String, Object> favoriteZSetRedisTemplate() {
		RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(createLettuceConnectionFactory(FAVORITE_DB_IDX.ordinal()));
		redisTemplate.setKeySerializer(new StringRedisSerializer());
		redisTemplate.setValueSerializer(new StringRedisSerializer()); // ZSet 값에 대한 직렬화

		return redisTemplate;
	}

	// 통계 템플릿

	@Bean
	public RedisTemplate<String, Object> statisticRedisTemplate() {
		RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(createLettuceConnectionFactory(STATISTIC_DB_IDX.ordinal()));
		redisTemplate.setKeySerializer(new StringRedisSerializer());

		// ObjectMapper 설정
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());
		objectMapper.registerModule(new Jdk8Module());
		objectMapper.registerModule(new ParameterNamesModule());

		// Object 타입의 데이터를 처리할 수 있는 Serializer 설정
		Jackson2JsonRedisSerializer<Object> serializer = new Jackson2JsonRedisSerializer<>(Object.class);
		serializer.setObjectMapper(objectMapper);

		redisTemplate.setValueSerializer(serializer);
		return redisTemplate;
	}

	// 통계 zset 템플릿 (영수증 탑 5 용도)
	@Bean
	public RedisTemplate<String, Object> statisticZSetRedisTemplate() {
		RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(createLettuceConnectionFactory(STATISTIC_DB_IDX.ordinal()));
		redisTemplate.setKeySerializer(new StringRedisSerializer());
		// Value 직렬화를 위한 ObjectMapper 설정
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());
		objectMapper.registerModule(new Jdk8Module());
		objectMapper.registerModule(new ParameterNamesModule());
		// Value 직렬화 설정
		Jackson2JsonRedisSerializer<ReceiptRankDto> serializer = new Jackson2JsonRedisSerializer<>(
			ReceiptRankDto.class);
		serializer.setObjectMapper(objectMapper);
		redisTemplate.setValueSerializer(serializer);
		return redisTemplate;
	}

	@Bean
	public PlatformTransactionManager transactionManager() {
		return new JpaTransactionManager();
	}
}
