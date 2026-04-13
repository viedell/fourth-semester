public class PushNotification implements Notifiable {

    private String deviceToken;

    public PushNotification(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    @Override
    public void sendNotification(Transaction transaction) {
        System.out.printf("  🔔 PUSH   → device [%s]%n", deviceToken);
        System.out.printf("     Title  : Payment %s!%n", transaction.getStatus());
        System.out.printf("     Body   : Rp%,.0f | %s | %s%n",
            transaction.getAmount(),
            transaction.getPaymentMethod(),
            transaction.getTransactionId());
    }

    @Override
    public String getChannelName() { return "Push Notification"; }
}
