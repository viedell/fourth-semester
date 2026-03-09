public class Main {
    public static void main(String[] args) {
        Product product1 = new Product(101, "Wireless Mouse", 25.99, 50, "Electronics");

        product1.displayDetails();

        product1.addStock(20);

        product1.sellProduct(15);

        product1.applyDiscount(10);

        product1.checkStock();

        product1.displayDetails();
    }
}