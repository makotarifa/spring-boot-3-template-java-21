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

    public RateLimitingFilter(
            @Value("${app.rate-limit.paths:/api/v1/login,/api/v1/register}") String paths,
            @Value("${app.rate-limit.capacity:10}") long capacity,
            @Value("${app.rate-limit.refill-period-seconds:1}") long refillPeriodSeconds) {
        this.protectedPaths = Stream.of(paths.split(",")).map(String::trim).collect(Collectors.toSet());
        this.capacity = capacity;
        this.refillPeriodSeconds = refillPeriodSeconds;
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
            if (now - window.start >= periodMs) {
                window.start = now;
                window.count.set(0);
            }
            long current = window.count.incrementAndGet();
            if (current > capacity) {
                response.setStatus(429);
                response.setHeader("Retry-After", String.valueOf(refillPeriodSeconds));
                return;
            }
        }
        filterChain.doFilter(request, response);
    }

    private String clientKey(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isBlank()) ip = request.getRemoteAddr();
        return ip;
    }

    static class Window {
        volatile long start;
        final AtomicLong count;
        Window(long start, AtomicLong count) { this.start = start; this.count = count; }
    }
}
