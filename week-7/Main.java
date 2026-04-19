import java.util.List;

public class Main {

    public static void main(String[] args) {

        OrderRepository<Integer, Double> foodRepo = new OrderRepository<>();

        foodRepo.addOrder(new Order<>(101, "Nasi Goreng Special", "Food", 45.99));
        foodRepo.addOrder(new Order<>(102, "Ayam Bakar",          "Food", 38.50));
        foodRepo.addOrder(new Order<>(103, "Es Teh Manis",        "Food",  8.00));
        foodRepo.addOrder(new Order<>(104, "Mie Goreng Seafood",  "Food", 52.75));

        List<Order<Integer, Double>> foodOrders = foodRepo.getAllOrders();

        System.out.println("╔══════════════════════════════════════════════════════════╗");
        System.out.println("║              FOOD ORDERS  (ID: Integer, Price: Double)   ║");
        System.out.println("╚══════════════════════════════════════════════════════════╝");
        foodRepo.displayAll();
        System.out.printf("%n  ✔  Total price  : Rp %.2f%n",  OrderRepository.calculateTotal(foodOrders));
        System.out.printf("  ✔  Most expensive: %s%n%n",      OrderRepository.findMostExpensive(foodOrders));


        OrderRepository<String, Integer> electronicsRepo = new OrderRepository<>();

        electronicsRepo.addOrder(new Order<>("ELEC-001", "Laptop ASUS ROG",     "Electronics", 15_000_000));
        electronicsRepo.addOrder(new Order<>("ELEC-002", "Samsung Galaxy S25",  "Electronics",  8_500_000));
        electronicsRepo.addOrder(new Order<>("ELEC-003", "Mechanical Keyboard", "Electronics",    750_000));
        electronicsRepo.addOrder(new Order<>("ELEC-004", "USB-C Hub 7-in-1",    "Electronics",    320_000));

        List<Order<String, Integer>> elecOrders = electronicsRepo.getAllOrders();

        System.out.println("╔══════════════════════════════════════════════════════════╗");
        System.out.println("║         ELECTRONICS ORDERS  (ID: String, Price: Integer) ║");
        System.out.println("╚══════════════════════════════════════════════════════════╝");
        electronicsRepo.displayAll();
        System.out.printf("%n  ✔  Total price  : Rp %,.0f%n", OrderRepository.calculateTotal(elecOrders));
        System.out.printf("  ✔  Most expensive: %s%n%n",      OrderRepository.findMostExpensive(elecOrders));


        OrderRepository<String, Double> serviceRepo = new OrderRepository<>();

        serviceRepo.addOrder(new Order<>("SVC-A1", "Website Development", "Service", 3_500_000.0));
        serviceRepo.addOrder(new Order<>("SVC-A2", "Logo Design",         "Service",   850_000.0));
        serviceRepo.addOrder(new Order<>("SVC-A3", "SEO Optimization",    "Service", 1_200_000.0));

        List<Order<String, Double>> svcOrders = serviceRepo.getAllOrders();

        System.out.println("╔══════════════════════════════════════════════════════════╗");
        System.out.println("║           SERVICE ORDERS  (ID: String, Price: Double)    ║");
        System.out.println("╚══════════════════════════════════════════════════════════╝");
        serviceRepo.displayAll();
        System.out.printf("%n  ✔  Total price  : Rp %,.2f%n", OrderRepository.calculateTotal(svcOrders));
        System.out.printf("  ✔  Most expensive: %s%n%n",      OrderRepository.findMostExpensive(svcOrders));


        System.out.println("╔══════════════════════════════════════════════════════════╗");
        System.out.println("║                     GRAND SUMMARY                        ║");
        System.out.println("╚══════════════════════════════════════════════════════════╝");
        System.out.printf("  Food total          : Rp %,.2f%n", OrderRepository.calculateTotal(foodOrders));
        System.out.printf("  Electronics total   : Rp %,.0f%n", OrderRepository.calculateTotal(elecOrders));
        System.out.printf("  Service total       : Rp %,.2f%n", OrderRepository.calculateTotal(svcOrders));
    }
}