import java.util.Arrays;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        POSSystem pos = new POSSystem("Warung Kopi Viedell");

        List<Notifiable> allChannels = Arrays.asList(
            new EmailNotification("customer@email.com"),
            new SMSNotification("+62812345678"),
            new PushNotification("DEVICE-TOKEN-XYZ")
        );

        System.out.println("\n>>> SCENARIO 1: Cash Payment (success)");
        Payment cash = new CashPayment(45_000, 50_000);   // Payment is abstract type
        pos.checkout(cash, allChannels);

        System.out.println("\n>>> SCENARIO 2: E-Wallet Payment (success)");
        Payment gopay = new EWalletPayment(75_000, "GoPay", "08111222333", 200_000);
        pos.checkout(gopay, Arrays.asList(
            new EmailNotification("customer@email.com"),
            new PushNotification("DEVICE-TOKEN-XYZ")
        ));

        System.out.println("\n>>> SCENARIO 3: Bank Transfer (success)");
        Payment transfer = new BankTransferPayment(120_000, "BCA", "1234567890");
        pos.checkout(transfer, Arrays.asList(new SMSNotification("+62898765432")));

        System.out.println("\n>>> SCENARIO 4: Cash Payment (insufficient cash — will fail)");
        Payment failedCash = new CashPayment(100_000, 50_000);
        pos.checkout(failedCash, allChannels);


        System.out.println("\n>>> SCENARIO 5: E-Wallet (insufficient balance — will fail)");
        Payment failedOvo = new EWalletPayment(500_000, "OVO", "08222333444", 10_000);
        pos.checkout(failedOvo, allChannels);


        pos.printTransactionHistory();
    }
}
