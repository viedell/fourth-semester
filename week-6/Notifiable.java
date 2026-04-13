public interface Notifiable {

    void sendNotification(Transaction transaction);

    String getChannelName();
}
