public abstract class Payment {

    protected double amount;

    public Payment(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Payment amount must be greater than zero.");
        }
        this.amount = amount;
    }

    public double getAmount() {
        return amount;
    }

    public abstract boolean processPayment();

    public abstract String getPaymentMethod();
}
