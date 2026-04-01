package com.example.paymentsystem.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Instant;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RateLimitFilter extends OncePerRequestFilter {

    private static final int MAX_REQUESTS = 100;
    private static final long WINDOW_SIZE_MS = 60_000; // 1 minute

    private static class RequestInfo {
        int count;
        long startTime;

        RequestInfo(int count, long startTime) {
            this.count = count;
            this.startTime = startTime;
        }
    }

    private final ConcurrentHashMap<String, RequestInfo> requests = new ConcurrentHashMap<>();

    @Override
    protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse res,
                                    FilterChain chain)
            throws ServletException, IOException {

        String ip = req.getRemoteAddr();
        long now = Instant.now().toEpochMilli();

        requests.compute(ip, (key, value) -> {
            if (value == null) {
                return new RequestInfo(1, now);
            }

            // Reset window if expired
            if (now - value.startTime > WINDOW_SIZE_MS) {
                return new RequestInfo(1, now);
            }

            value.count++;
            return value;
        });

        RequestInfo info = requests.get(ip);

        if (info.count > MAX_REQUESTS) {
            res.setStatus(429);
            res.getWriter().write("Too many requests - try again later");
            return;
        }

        chain.doFilter(req, res);
    }
}