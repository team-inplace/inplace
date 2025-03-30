package team7.inplace.global.aop;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class ExecutionTimeAspect {

    @Around("@within(org.springframework.web.bind.annotation.RestController)")
    public Object controllerExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        return measureExecutionTime(joinPoint);
    }

    @Around("@within(team7.inplace.global.annotation.Facade)")
    public Object facadeExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        return measureExecutionTime(joinPoint);
    }

    @Around("@within(org.springframework.stereotype.Service)")
    public Object serviceExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        return measureExecutionTime(joinPoint);
    }

    @Around("@within(org.springframework.stereotype.Repository)")
    public Object repositoryExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        return measureExecutionTime(joinPoint);
    }

    @Around("@within(team7.inplace.global.annotation.Client)")
    public Object clientExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        return measureExecutionTime(joinPoint);
    }

    private Object measureExecutionTime(ProceedingJoinPoint joinPoint)
        throws Throwable {
        String layer = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = joinPoint.getSignature().getName();
        long startTime = System.currentTimeMillis();
        try {
            return joinPoint.proceed();
        } finally {
            long endTime = System.currentTimeMillis();
            long executionTime = endTime - startTime;

            ThreadExecutionContext.get().enter(layer, methodName, executionTime);
        }
    }
}
