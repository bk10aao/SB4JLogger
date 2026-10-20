package sb4jLogger;

import org.springframework.stereotype.Service;

@Service
public class TestService {

    @Log(action = "PROCESS_PAYMENT", mask = {"pinNumber", "creditCard"})
    public String processPayment(String username, String pinNumber, PaymentRequest paymentRequest) {
        return username + "_" + pinNumber;
    }
}
