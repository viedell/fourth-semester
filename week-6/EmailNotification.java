public class EmailNotification implements Notifiable {

    private String emailAddress;

    public EmailNotification(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    @Override
    public void sendNotification(Transaction transaction) {
        System.out.printf("  📧 EMAIL  → %s%n", emailAddress);
        System.out.printf("     Subject : Payment %s - %s%n",
            transaction.getStatus(), transaction.getTransactionId());
        System.out.printf("     Body    : Your payment of Rp%,.0f via %s was %s.%n",
            transaction.getAmount(),
            transaction.getPaymentMethod(),
            transaction.getStatus() == Transaction.Status.SUCCESS ? "successful" : "failed");
    }

    @Override
    public String getChannelName() { return "Email"; }
}
