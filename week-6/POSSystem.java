import java.util.ArrayList;
import java.util.List;

public class POSSystem {

    private final String storeName;

    private final List<Transaction> transactionHistory = new ArrayList<>();

    public POSSystem(String storeName) {
        this.storeName = storeName;
    }

    public Transaction checkout(Payment payment, List<Notifiable> notifiers) {

        System.out.println("\n" + "═".repeat(55));
        System.out.printf("  🏪  %s — POS Checkout%n", storeName);
        System.out.println("═".repeat(55));
        System.out.printf("  Method  : %s%n", payment.getPaymentMethod());
        System.out.printf("  Amount  : Rp%,.0f%n", payment.getAmount());
        System.out.println("─".repeat(55));

        Transaction transaction = new Transaction(payment.getPaymentMethod(), payment.getAmount());

        try {
            boolean success = payment.processPayment();   
            transaction.setStatus(success ? Transaction.Status.SUCCESS : Transaction.Status.FAILED);
        } catch (IllegalStateException e) {
            transaction.setStatus(Transaction.Status.FAILED);
            System.out.println("  ❌ Payment failed: " + e.getMessage());
        }

        System.out.println("─".repeat(55));
        System.out.println("  📨 Sending notifications...");

        for (Notifiable notifier : notifiers) {
            notifier.sendNotification(transaction);       
        }

        transactionHistory.add(transaction);

        System.out.println("─".repeat(55));
        System.out.printf("  Result  : %s | ID: %s%n",
            transaction.getStatus(), transaction.getTransactionId());
        System.out.println("═".repeat(55));

        return transaction;
    }

    public void printTransactionHistory() {
        System.out.println("\n" + "═".repeat(55));
        System.out.printf("  📋  Transaction History — %s (%d records)%n",
            storeName, transactionHistory.size());
        System.out.println("═".repeat(55));
        if (transactionHistory.isEmpty()) {
            System.out.println("  (no transactions yet)");
        } else {
            for (Transaction t : transactionHistory) {
                System.out.println("  " + t);
            }
        }
        System.out.println("═".repeat(55));
    }
}
