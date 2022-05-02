package co.seg.mercadolibre.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.lang.reflect.Method;
import java.time.Duration;

/**
 * Redis config
 *
 * @author carlos gomez
 */
@EnableCaching
@Configuration
@AutoConfigureAfter(RedisAutoConfiguration.class)
public class RedisConfig extends CachingConfigurerSupport {

	/**
	 * generados de llaves para la bd
	 *
	 * @return the key generator
	 */
	@Override
	@Bean
	public KeyGenerator keyGenerator() {
		return new KeyGenerator() {
			@Override
			public Object generate(Object target, Method method, Object... params) {
				StringBuilder sb = new StringBuilder();
				sb.append(target.getClass().getName());
				sb.append(method.getName());
				for (Object obj : params) {
					sb.append(obj.toString());
				}
				return sb.toString();
			}
		};
	}

	/**
	 * Establezca el tiempo de caducidad predeterminado del caché, también use la
	 * configuración de Duración, el valor predeterminado es permanente, no almacene
	 * en caché valores vacíos, configure la serialización
	 *
	 * @param connectionFactory connection factory
	 * @return the cache manager
	 */
	@Bean("cacheManager")
	public CacheManager cacheManager(RedisConnectionFactory connectionFactory) {

		Jackson2JsonRedisSerializer<Object> jsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
		StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();

		ObjectMapper om = new ObjectMapper();
		om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
		om.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL,
				JsonTypeInfo.As.WRAPPER_ARRAY);
		jsonRedisSerializer.setObjectMapper(om);

		RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
				.entryTtl(Duration.ofMinutes(60)).disableCachingNullValues()
				.serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(stringRedisSerializer))
				.serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(jsonRedisSerializer));

		return RedisCacheManager.builder(connectionFactory).cacheDefaults(redisCacheConfiguration).build();

	}

	/**
	 * configuraciones para el uso de Redis con Redis template
	 *
	 * @param jedisConnectionFactory jedis connection factory
	 * @return the redis template
	 */
	@Bean
	public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {

		RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();

		RedisSerializer<?> stringSerializer = new StringRedisSerializer();

		Jackson2JsonRedisSerializer<Object> jsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);

		ObjectMapper om = new ObjectMapper();
		om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
		om.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL,
				JsonTypeInfo.As.WRAPPER_ARRAY);
		jsonRedisSerializer.setObjectMapper(om);

		// establecer las conexiones
		redisTemplate.setConnectionFactory(connectionFactory);

		// serialización clave
		redisTemplate.setKeySerializer(stringSerializer);
		// serialización de valores
		redisTemplate.setValueSerializer(jsonRedisSerializer);
		// Serialización de clave hash
		redisTemplate.setHashKeySerializer(stringSerializer);
		// Serialización del valor hash
		redisTemplate.setHashValueSerializer(jsonRedisSerializer);

		redisTemplate.afterPropertiesSet();

		return redisTemplate;
	}

	/**
	 * configuracion general de cache
	 * 
	 * @return
	 */
	@Bean
	public RedisCacheConfiguration cacheConfiguration() {
		return RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ZERO).disableCachingNullValues()
				.serializeValuesWith(SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));
	}

	/**
	 * configuracion de cache personalizada
	 * 
	 * @return
	 */
	@Bean
	public RedisCacheManagerBuilderCustomizer redisCacheManagerBuilderCustomizer() {
		return (builder) -> builder.withCacheConfiguration("CurrencyCache",
				RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ZERO));
	}

}
