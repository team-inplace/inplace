package team7.inplace.security.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;
import team7.inplace.global.aop.ThreadExecutionContext;
import team7.inplace.global.aop.ThreadExecutionContext.ExecutionNode;

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

            List<Map<String, Object>> layerRecords = new ArrayList<>();
            for (ExecutionNode node : ThreadExecutionContext.get().getTopLevelNodes()) {
                collectLayerRecordsAsJson(node, layerRecords);
            }

            String layersJson = toJson(layerRecords);

            meterRegistry.summary("api.layer.execution.summary",
                "path", path,
                "layers", layersJson
            ).record(totalTime);
            log.info("{}", layerRecords);
            ThreadExecutionContext.clear();
        }
    }

    private void collectLayerRecordsAsJson(ExecutionNode node, List<Map<String, Object>> records) {
        Map<String, Object> entry = new HashMap<>();
        entry.put("layer", node.getLayer());
        entry.put("method", node.getMethod());
        entry.put("time", node.getExecutionTime());
        records.add(entry);
        for (ExecutionNode child : node.getChildren()) {
            collectLayerRecordsAsJson(child, records);
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