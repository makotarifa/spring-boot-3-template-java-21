package com.angelmorando.template.util.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class RateLimitingFilterTest {

    @Test
    void rateLimit_exceeded_returns429WithBody() throws ServletException, IOException {
        RateLimitingFilter filter = new RateLimitingFilter("/api/v1/login", 1, 10, false);
        MockHttpServletRequest req = new MockHttpServletRequest("POST", "/api/v1/login");
        req.setRemoteAddr("127.0.0.1");
        MockHttpServletResponse resp1 = new MockHttpServletResponse();
        MockHttpServletResponse resp2 = new MockHttpServletResponse();
        FilterChain chain = new FilterChain() {
            @Override
            public void doFilter(jakarta.servlet.ServletRequest request, jakarta.servlet.ServletResponse response) {}
        };
        filter.doFilterInternal(req, resp1, chain);
        filter.doFilterInternal(req, resp2, chain);
        assertEquals(429, resp2.getStatus());
        assertEquals("application/problem+json", resp2.getContentType());
        assertTrue(resp2.getContentAsString().contains("Too Many Requests"));
    }
}
