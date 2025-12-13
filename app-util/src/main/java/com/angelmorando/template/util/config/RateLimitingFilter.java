package com.angelmorando.template.util.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class RateLimitingFilter extends OncePerRequestFilter {

    private final Set<String> protectedPaths;
    private final long capacity;
    private final long refillPeriodSeconds;
    private final Map<String, Window> windows = new ConcurrentHashMap<>();
    private final boolean trustXForwardedFor;

    public RateLimitingFilter(
            @Value("${app.rate-limit.paths:/api/v1/login,/api/v1/register}") String paths,
            @Value("${app.rate-limit.capacity:10}") long capacity,
            @Value("${app.rate-limit.refill-period-seconds:1}") long refillPeriodSeconds,
            @Value("${app.rate-limit.trust-x-forwarded-for:false}") boolean trustXForwardedFor) {
        this.protectedPaths = Stream.of(paths.split(",")).map(String::trim).collect(Collectors.toSet());
        this.capacity = capacity;
        this.refillPeriodSeconds = refillPeriodSeconds;
        this.trustXForwardedFor = trustXForwardedFor;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String path = request.getRequestURI();
        if (protectedPaths.contains(path)) {
            String key = path + ":" + clientKey(request);
            Window window = windows.computeIfAbsent(key, k -> new Window(System.currentTimeMillis(), new AtomicLong(0)));
            long now = System.currentTimeMillis();
            long periodMs = refillPeriodSeconds * 1000L;
            long startSnapshot = window.start.get();
            if (now - startSnapshot >= periodMs) {
                if (window.start.compareAndSet(startSnapshot, now)) {
                    window.count.set(0);
                }
            }
            if (now - window.start.get() > periodMs * 60) {
                windows.remove(key);
            }
            long current = window.count.incrementAndGet();
            if (current > capacity) {
                response.setStatus(429);
                response.setHeader("Retry-After", String.valueOf(refillPeriodSeconds));
                response.setContentType("application/problem+json");
                String body = "{\"type\":\"about:blank\",\"title\":\"Too Many Requests\",\"status\":429,\"detail\":\"Rate limit exceeded\",\"retryAfter\":" + refillPeriodSeconds + "}";
                response.getWriter().write(body);
                return;
            }
        }
        filterChain.doFilter(request, response);
    }

    private String clientKey(HttpServletRequest request) {
        String ip = request.getRemoteAddr();
        if (trustXForwardedFor) {
            String header = request.getHeader("X-Forwarded-For");
            if (header != null && !header.isBlank()) {
                ip = header.split(",")[0].trim();
            }
        }
        return ip;
    }

    static class Window {
        final java.util.concurrent.atomic.AtomicLong start;
        final AtomicLong count;
        Window(long start, AtomicLong count) { this.start = new java.util.concurrent.atomic.AtomicLong(start); this.count = count; }
    }
}
