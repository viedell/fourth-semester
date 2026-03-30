public class CryptoPayment extends Payment {
    private String walletAddress;

    public CryptoPayment(double amount, String walletAddress) {
        super(amount);
        this.walletAddress = walletAddress;
    }

    @Override
    public void processPayment() {
        System.out.println("Paid $" + amount + " using Cryptocurrency");
        System.out.println("Wallet Address: " + walletAddress);
    }
}