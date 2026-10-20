package sb4jLogger;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Aspect
@Component
public class LogAspect {

    private static final ObjectMapper JSON_WRITER = new ObjectMapper();
    private static final Masker MASKER = new Masker();
    private static final ParameterNameDiscoverer PARAM_DISCOVERER = new DefaultParameterNameDiscoverer();

    @Around("@annotation(logAnnotation)")
    public Object handleLogging(ProceedingJoinPoint joinPoint, Log logAnnotation) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String[] parameterNames = signature.getParameterNames();
        if (parameterNames == null)
            parameterNames = PARAM_DISCOVERER.getParameterNames(signature.getMethod());
        Object[] parameterValues = joinPoint.getArgs();
        List<String> targetsToMask = Arrays.asList(logAnnotation.mask());
        Map<String, Object> loggedPayload = new LinkedHashMap<>();
        if (parameterNames != null)
            for(int i = 0; i < parameterNames.length; i++) {
                String name = parameterNames[i];
                Object value = parameterValues[i];
                if(targetsToMask.contains(name))
                    loggedPayload.put(name, UniversalMasker.obfuscate(value));
                else
                    loggedPayload.put(name, MASKER.maskObject(value, targetsToMask));
            }
        else
            loggedPayload.put("error", "Unable to resolve method parameter names.");
        System.out.println(JSON_WRITER.writeValueAsString(Map.of(
                "action", logAnnotation.action(),
                "method", signature.getMethod().getName(),
                "payload", loggedPayload
        )));
        return joinPoint.proceed();
    }
}