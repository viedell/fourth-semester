public class SMSNotification implements Notifiable {

    private String phoneNumber;

    public SMSNotification(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public void sendNotification(Transaction transaction) {
        System.out.printf("  💬 SMS    → %s%n", phoneNumber);
        System.out.printf("     Msg    : [%s] Rp%,.0f via %s. Ref: %s%n",
            transaction.getStatus(),
            transaction.getAmount(),
            transaction.getPaymentMethod(),
            transaction.getTransactionId());
    }

    @Override
    public String getChannelName() { return "SMS"; }
}
