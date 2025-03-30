package team7.inplace.security.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tags;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;
import team7.inplace.global.aop.ThreadExecutionContext;

@Slf4j
@RequiredArgsConstructor
public class ApiExecutionMetricsFilter extends OncePerRequestFilter {

    private final MeterRegistry meterRegistry;
    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain
    ) throws ServletException, IOException {

        long requestStart = System.currentTimeMillis();

        try {
            filterChain.doFilter(request, response);
        } finally {
            long totalTime = System.currentTimeMillis() - requestStart;
            String path = request.getRequestURI();
            String requestId = UUID.randomUUID().toString();

            var context = ThreadExecutionContext.get();
            List<Map<String, Object>> records = new ArrayList<>();

            for (Map.Entry<String, Long> entry : context.getExecutionTimeMap().entrySet()) {
                Map<String, Object> record = new HashMap<>();
                record.put("key", entry.getKey());
                record.put("time", entry.getValue());
                records.add(record);
            }

            String layersJson = toJson(records);
            meterRegistry.gauge("api.layer.execution.time",
                Tags.of("requestId", requestId, "path", path, "layers", layersJson),
                totalTime
            );
            ThreadExecutionContext.clear();
        }
    }

    private String toJson(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            return "[]";
        }
    }
}