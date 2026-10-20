package sb4jLogger;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = {sb4jLogger.LoggingTestConfig.class})
public class MaskingAspectTest {

    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @Autowired
    private TestService testService;

    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(outputStream));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }

    @Test
    void testMethodArgumentsAreNotMutatedByLogging() {
        String username = "john_doe";
        String pinNumber = "9921";
        PaymentRequest request = new PaymentRequest("tx-123", "9921", new BillingAddress("London", "1234-5678"));
        String businessResult = testService.processPayment(username, pinNumber, request);
        originalOut.println("CAPTURED JSON LOG: " + businessResult);

        assertEquals("john_doe_9921", businessResult);
        assertEquals("9921", request.getPinNumber());
    }

    @Test
    void testLoggingActuallyObfuscatesSensitiveFields() {
        String username = "john_doe";
        String pinNumber = "9921";
        PaymentRequest request = new PaymentRequest("tx-123", "9921", new BillingAddress("London", "1234-5678"));
        testService.processPayment(username, pinNumber, request);
        String logOutput = outputStream.toString();

        originalOut.println("CAPTURED JSON LOG: " + logOutput);
        assertTrue(logOutput.contains("\"pinNumber\":\"****\""), "Pin number should be obfuscated");
        assertTrue(logOutput.contains("\"creditCard\":\"*********\""), "Nested credit card should be obfuscated");
    }

    @Test
    void testNullValueReturnsNullString() {
        assertEquals("null", UniversalMasker.obfuscate(null));
        assertEquals("null", UniversalMasker.obfuscate(null, 3));
    }

    @Test
    void testFullObfuscationSingleArgument() {
        assertEquals("****", UniversalMasker.obfuscate("9921"));
        assertEquals("*****************", UniversalMasker.obfuscate("secretPassword123"));
    }

    @Test
    void testPartialObfuscationCustomCharacters() {
        assertEquals("***ret123", UniversalMasker.obfuscate("secret123", 3));
        assertEquals("****-5678", UniversalMasker.obfuscate("1234-5678", 4));
    }

    @Test
    void testCharactersExceedingStringLengthClampsSafely() {
        assertEquals("****", UniversalMasker.obfuscate("9921", 10));
    }

    @Test
    void testNegativeCharacterCountClampsToZero() {
        assertThrows(IllegalArgumentException.class, () -> UniversalMasker.obfuscate("secret123", -5));
    }

    @Test
    void testZeroCharacterCountLeavesStringUntouched() {
        assertEquals("secret123", UniversalMasker.obfuscate("secret123", 0));
    }
}
