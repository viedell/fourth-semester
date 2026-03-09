public class Product {
    private int productId;
    private String name;
    private double price;
    private int stock;
    private String category;

    public Product(int productId, String name, double price, int stock, String category) {
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.category = category;
    }

    public void addStock(int quantity) {
        this.stock += quantity;
        System.out.println(quantity + " units added. New stock: " + this.stock);
    }

    public void sellProduct(int quantity) {
        if (quantity <= this.stock) {
            this.stock -= quantity;
            System.out.println(quantity + " units sold. Remaining stock: " + this.stock);
        } else {
            System.out.println("Not enough stock available!");
        }
    }

    public void applyDiscount(double discountPercentage) {
        double discountAmount = this.price * (discountPercentage / 100);
        this.price -= discountAmount;
        System.out.printf("Discount applied. New price: $%.2f%n", this.price);
    }

    public void checkStock() {
        System.out.println("Stock available: " + this.stock);
    }

    public void displayDetails() {
        System.out.println("Product ID: " + this.productId);
        System.out.println("Name: " + this.name);
        System.out.println("Category: " + this.category);
        System.out.printf("Price: $%.2f%n", this.price);
        System.out.println("Stock: " + this.stock);
        System.out.println("---------------------------");
    }
}