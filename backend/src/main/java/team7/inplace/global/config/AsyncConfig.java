package team7.inplace.global.config;

import java.util.concurrent.Executor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import team7.inplace.global.aop.ThreadContextPropagatingDecorator;

@Configuration
public class AsyncConfig {

    @Bean("externalApiExecutor")
    public Executor externalApiExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);
        executor.setMaxPoolSize(20);
        executor.setQueueCapacity(50);
        executor.setThreadNamePrefix("external-api-");
        executor.setTaskDecorator(new ThreadContextPropagatingDecorator());
        executor.initialize();

        return executor;
    }

}
