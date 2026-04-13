public class BankTransferPayment extends Payment {

    private String bankName;
    private String accountNumber;

    public BankTransferPayment(double amount, String bankName, String accountNumber) {
        super(amount);
        this.bankName      = bankName;
        this.accountNumber = accountNumber;
    }

    @Override
    public boolean processPayment() {
        if (accountNumber == null || accountNumber.length() < 6) {
            throw new IllegalStateException("Invalid bank account number: " + accountNumber);
        }
        System.out.printf("  🏦 Transfer to %s account %s confirmed%n", bankName, accountNumber);
        return true;
    }

    @Override
    public String getPaymentMethod() { return "Bank Transfer (" + bankName + ")"; }
}
