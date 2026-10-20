package sb4jLogger;

public class PaymentRequest {

    private String transactionId;
    private String pinNumber;
    private BillingAddress address;

    public PaymentRequest(String transactionId, String pinNumber, BillingAddress address) {
        this.transactionId = transactionId;
        this.pinNumber = pinNumber;
        this.address = address;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public String getPinNumber() {
        return pinNumber;
    }

    public BillingAddress getAddress() {
        return address;
    }
}