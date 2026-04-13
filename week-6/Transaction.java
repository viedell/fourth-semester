import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class Transaction {

    public enum Status {
        PENDING, SUCCESS, FAILED
    }

    private final String transactionId;    
    private final String paymentMethod;
    private final double amount;
    private Status status;                  
    private final LocalDateTime timestamp;

    public Transaction(String paymentMethod, double amount) {
        this.transactionId = "TXN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        this.paymentMethod = paymentMethod;
        this.amount        = amount;
        this.status        = Status.PENDING;
        this.timestamp     = LocalDateTime.now();
    }

    public String getTransactionId()  { return transactionId; }
    public String getPaymentMethod()  { return paymentMethod; }
    public double getAmount()         { return amount; }
    public Status getStatus()         { return status; }
    public LocalDateTime getTimestamp() { return timestamp; }


    public void setStatus(Status status) { this.status = status; }

    @Override
    public String toString() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return String.format(
            "[%s] ID: %s | Method: %-15s | Amount: Rp%,.0f | Status: %s",
            timestamp.format(fmt), transactionId, paymentMethod, amount, status
        );
    }
}
