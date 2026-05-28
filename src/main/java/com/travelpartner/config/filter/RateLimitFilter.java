package com.travelpartner.config.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.travelpartner.common.response.ApiResponse;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
public class RateLimitFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper;

    private final Map<String, Bucket> cache = new ConcurrentHashMap<>();

    private Bucket createNewBucket() {

        Bandwidth limit = Bandwidth.classic(
                100,
                Refill.greedy(100, Duration.ofMinutes(1))
        );

        return Bucket.builder()
                .addLimit(limit)
                .build();
    }

    private Bucket resolveBucket(String ip) {
        return cache.computeIfAbsent(ip, k -> createNewBucket());
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {

        String path = request.getServletPath();

        return path.startsWith("/health")
                || path.startsWith("/swagger-ui")
                || path.startsWith("/v3/api-docs");
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String ip = request.getRemoteAddr();

        String path = request.getRequestURI();

        Bucket bucket;

        /*
         * Different limits for different APIs
         */

        if (path.startsWith("/api/auth")) {

            bucket = cache.computeIfAbsent(
                    "AUTH_" + ip,
                    k -> Bucket.builder()
                            .addLimit(Bandwidth.classic(
                                    10,
                                    Refill.greedy(10, Duration.ofMinutes(1))
                            ))
                            .build()
            );

        } else {

            bucket = resolveBucket(ip);
        }

        if (bucket.tryConsume(1)) {

            filterChain.doFilter(request, response);

        } else {

            response.setStatus(429);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);

            ApiResponse<Object> apiResponse =
                    ApiResponse.error(
                            429,
                            "Too many requests. Please try again later."
                    );

            objectMapper.writeValue(
                    response.getOutputStream(),
                    apiResponse
            );
        }
    }
}