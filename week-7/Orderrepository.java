import java.util.ArrayList;
import java.util.List;

public class OrderRepository<I, P extends Number> {

    private final List<Order<I, P>> orders = new ArrayList<>();

    public void addOrder(Order<I, P> order) {
        orders.add(order);
    }

    public List<Order<I, P>> getAllOrders() {
        return List.copyOf(orders);
    }

    public void displayAll() {
        if (orders.isEmpty()) {
            System.out.println("  (no orders)");
            return;
        }
        orders.forEach(o -> System.out.println("  " + o));
    }

    public static <X, P2 extends Number> double calculateTotal(List<Order<X, P2>> orderList) {
        double total = 0.0;
        for (Order<X, P2> o : orderList) {
            total += o.getPrice().doubleValue();
        }
        return total;
    }

    public static <X, P2 extends Number> Order<X, P2> findMostExpensive(List<Order<X, P2>> orderList) {
        if (orderList.isEmpty()) return null;

        Order<X, P2> maxOrder = orderList.get(0);
        for (Order<X, P2> o : orderList) {
            if (o.getPrice().doubleValue() > maxOrder.getPrice().doubleValue()) {
                maxOrder = o;
            }
        }
        return maxOrder;
    }
}