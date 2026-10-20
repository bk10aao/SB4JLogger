package sb4jLogger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@EnableAspectJAutoProxy
@ComponentScan(basePackages = "sb4jLogger")
public class LoggingTestConfig {

    @Bean
    public LogAspect logAspect() {
        return new LogAspect();
    }

    @Bean
    public TestService testService() {
        return new TestService();
    }
}