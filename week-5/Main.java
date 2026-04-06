public class Main {
    public static void main(String[] args) {
        Product p = new Product(1, "Laptop", 1000, 10);

        p.setPrice(900);
        p.addStock(5);
        p.display();

        Electronics e = new Electronics(2, "Phone", 500, 20);
        e.showStock();
    }
}