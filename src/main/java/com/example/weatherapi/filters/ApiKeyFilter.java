package com.example.weatherapi.filters;

import com.example.weatherapi.exception.UnauthorizedException;
import com.example.weatherapi.repository.ApiKeyDao;
import com.example.weatherapi.repository.ApiKeyRedisDao;
import com.example.weatherapi.repository.RateLimiterDao;
import com.example.weatherapi.repository.RateLimiterRedisDao;
import com.example.weatherapi.utils.RateBuckets;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class ApiKeyFilter implements WebFilter {

    private final String headerName = "X-api-key";

    private final ApiKeyRedisDao apiKeyRedisDao;
    private final RateLimiterRedisDao rateLimiterRedisDao;
    private final RateLimiterDao rateLimiterDao;
    private final ApiKeyDao apiKeyDao;
    private final RateBuckets buckets;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {

        String path = exchange.getRequest().getURI().getPath();
        List<String> filterUri = List.of("/api/get-api-key", "/api/register", "/api/login");
        boolean b = filterUri.contains(path);
        String actualApiKeyValue = exchange.getRequest().getHeaders().getFirst(headerName);
        if (b) {
            if (buckets.usersLimits.containsKey(actualApiKeyValue)) {
                if (checkBuckets(exchange, actualApiKeyValue))
                    return Mono.error(new UnauthorizedException("request limit exceeded"));
            } else {
                log.info("Creating new bucket...");
                buckets.usersLimits.put(actualApiKeyValue, buckets.getDefaultBucket());
            }
            return chain.filter(exchange);
        } else {
            return apiKeyRedisDao.findByKey(actualApiKeyValue)
                    .switchIfEmpty(Mono.error(new UnauthorizedException("the api-key is missing or outdated")))
                    .flatMap(apiKey -> {
                        if (buckets.usersLimits.containsKey(actualApiKeyValue)) {
                            if (checkBuckets(exchange, actualApiKeyValue))
                                return Mono.error(new UnauthorizedException("request limit exceeded"));
                        } else {
                            log.info("Creating new bucket...");
                            rateLimiterRedisDao.findByApiKey(actualApiKeyValue).switchIfEmpty(
                                    rateLimiterDao.findByKey(actualApiKeyValue))
                                            .map(limiter -> {
                                                rateLimiterRedisDao.save(apiKey, limiter).subscribe();
                                                buckets.usersLimits.put(actualApiKeyValue, buckets.getBucket(limiter.getBucketCapacity(), limiter.getRefillGreedyTokens(), limiter.getRefillGreedyDurationSeconds()));
                                                return limiter;
                                            })
                                            .subscribe();
                        }
                        return chain.filter(exchange);
                    });
        }
    }

    private boolean checkBuckets(ServerWebExchange exchange, String actualApiKeyValue) {
        log.info("Available tokens pop: " + buckets.usersLimits.get(actualApiKeyValue).getAvailableTokens());
        if (!buckets.usersLimits.get(actualApiKeyValue).tryConsume(1)) {
            exchange.getResponse().setStatusCode(HttpStatus.BANDWIDTH_LIMIT_EXCEEDED);
            log.warn("BANDWIDTH_LIMIT_EXCEEDED");
            return true;
        }
        return false;
    }
}