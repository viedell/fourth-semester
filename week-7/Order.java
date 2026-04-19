public class Order<I, P extends Number> {

    private final I id;
    private final String itemName;
    private final String category;
    private final P price;

    public Order(I id, String itemName, String category, P price) {
        this.id       = id;
        this.itemName = itemName;
        this.category = category;
        this.price    = price;
    }

    public I      getId()       { return id; }
    public String getItemName() { return itemName; }
    public String getCategory() { return category; }
    public P      getPrice()    { return price; }

    @Override
    public String toString() {
        return String.format("Order{id=%-6s | %-22s | %-12s | price=%s}",
                id, itemName, category, price);
    }
}