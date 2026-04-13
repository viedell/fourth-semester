public class EWalletPayment extends Payment {

    private String walletName;
    private String phoneNumber;
    private double walletBalance;

    public EWalletPayment(double amount, String walletName, String phoneNumber, double walletBalance) {
        super(amount);
        this.walletName    = walletName;
        this.phoneNumber   = phoneNumber;
        this.walletBalance = walletBalance;
    }

    @Override
    public boolean processPayment() {
        if (walletBalance < amount) {
            throw new IllegalStateException(
                String.format("Insufficient %s balance! Required: Rp%,.0f | Balance: Rp%,.0f",
                    walletName, amount, walletBalance)
            );
        }
        walletBalance -= amount;
        System.out.printf("  📱 %s deducted from %s | Remaining balance: Rp%,.0f%n",
            walletName, phoneNumber, walletBalance);
        return true;
    }

    @Override
    public String getPaymentMethod() { return "E-Wallet (" + walletName + ")"; }
}
