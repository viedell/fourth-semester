public class PayPalPayment extends Payment {
    private String email;

    public PayPalPayment(double amount, String email) {
        super(amount);
        this.email = email;
    }

    @Override
    public void processPayment() {
        System.out.println("Paid $" + amount + " using PayPal");
        System.out.println("Account Email: " + email);
    }
}