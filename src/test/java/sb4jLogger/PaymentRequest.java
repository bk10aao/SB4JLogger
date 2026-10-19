public class PaymentRequest {

    private String transactionId;
    private String pinNumber;
    private BillingAddress address;

    public PaymentRequest(String transactionId, String pinNumber, BillingAddress address) {
        this.transactionId = transactionId;
        this.pinNumber = pinNumber;
        this.address = address;
    }

    private class BillingAddress {
        private String city;
        private String creditCard;

        public BillingAddress(String city, String creditCard) {
            this.city = city;
            this.creditCard = creditCard;
        }
    }
}


