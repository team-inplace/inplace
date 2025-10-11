package my.inplace.common.aop;

import java.util.LinkedHashMap;
import java.util.Map;
import lombok.Getter;

public class ThreadExecutionContext {

    private static final ThreadLocal<ExecutionTime> threadLocal =
        ThreadLocal.withInitial(ExecutionTime::new);

    public static ExecutionTime get() {
        return threadLocal.get();
    }

    public static void clear() {
        threadLocal.remove();
    }

    public static void set(ExecutionTime context) {
        threadLocal.set(context);
    }

    @Getter
    public static class ExecutionTime {

        private final Map<String, Long> executionTimeMap = new LinkedHashMap<>();

        public void enter(String layer, String method, Long milliseconds) {
            var key = layer + "." + method;
            executionTimeMap.put(key, milliseconds);
        }
    }
}
