package co.uk.crowdcube.payments.challenge.enums;

public enum CardBrand {

    VISA("Visa"),
    MASTERCARD("Mastercard"),
    AMEX("American Express"),
    DINNERS("Discover & Dinners");

    private final String brand;

    CardBrand(final String brand) {
        this.brand = brand;
    }

    public String getBrand() {
        return brand;
    }
}
