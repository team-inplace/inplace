package team7.inplace.global.aop;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.task.TaskDecorator;

@Slf4j
public class ThreadContextPropagatingDecorator implements TaskDecorator {

    @Override
    public Runnable decorate(Runnable runnable) {

        var context = ThreadExecutionContext.get();

        return () -> {
            try {
                ThreadExecutionContext.set(context);
                runnable.run();
            } finally {
                context.exit();
            }
        };
    }
}
