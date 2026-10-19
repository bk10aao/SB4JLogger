package sb4jLogger;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = {sb4jLogger.LoggingTestConfig.class})
public class MaskingAspectTest {

    @Autowired
    private TestService testService;

    @Test
    void testMethodArgumentsAreNotMutatedByLogging() {
        // Arrange
        String username = "john_doe";
        String pinNumber = "9921";
        PaymentRequest request = new PaymentRequest("tx-123", "9921", new BillingAddress("London", "1234-5678"));

        // Act - Call the method
        String businessResult = testService.processPayment(username, pinNumber, request);

        // Assert: Verify business logic received raw, unmutated arguments
        assertEquals("john_doe_9921", businessResult);
        // Verify the argument instance itself wasn't mutated inside the method parameters
        assertEquals("9921", request.getPinNumber());
    }
}
