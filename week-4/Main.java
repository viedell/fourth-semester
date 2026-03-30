import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {

        ArrayList<Payment> payments = new ArrayList<>();

        Payment p1 = new CreditCardPayment(100.0, "1234-5678-9012-3456", "John Doe");
        Payment p2 = new PayPalPayment(50.0, "john@example.com");
        Payment p3 = new CryptoPayment(200.0, "0xABC123XYZ");

        payments.add(p1);
        payments.add(p2);
        payments.add(p3);

        for (Payment p : payments) {
            p.processPayment();
            System.out.println("-------------------");
        }
    }
}