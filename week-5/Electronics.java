class Electronics extends Product {
    public Electronics(int id, String name, double price, int stock) {
        super(id, name, price, stock);
    }

    public void showStock() {
        // Can access protected variable
        System.out.println("Stock (from subclass): " + stock);
    }
}