package sb4jLogger;

public class BillingAddress {
    private String city;
    private String creditCard;

    public BillingAddress(String city, String creditCard) {
        this.city = city;
        this.creditCard = creditCard;
    }

    public String getCity() {
        return city;
    }

    public String getCreditCard() {
        return creditCard;
    }
}
