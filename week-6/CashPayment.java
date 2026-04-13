public class CashPayment extends Payment {

    private double cashGiven;

    public CashPayment(double amount, double cashGiven) {
        super(amount);
        this.cashGiven = cashGiven;
    }

    @Override
    public boolean processPayment() {
        if (cashGiven < amount) {
            throw new IllegalStateException(
                String.format("Insufficient cash! Required: Rp%,.0f | Given: Rp%,.0f", amount, cashGiven)
            );
        }
        double change = cashGiven - amount;
        System.out.printf("  💵 Cash received: Rp%,.0f | Change: Rp%,.0f%n", cashGiven, change);
        return true;
    }

    @Override
    public String getPaymentMethod() { return "Cash"; }
}
