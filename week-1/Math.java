public class Math {

    private double number1;
    private double number2;
    private double number3;

    // Constructor
    public Math(double number1, double number2, double number3) {
        this.number1 = number1;
        this.number2 = number2;
        this.number3 = number3;
    }

    // Add method
    public double add() {
        return number1 + number2 + number3;
    }

    // Divide method
    public double divide() {
        return (number1 + number2) / number3;
    }

    // Multiple method
    public double multiple() {
        return number1 * number2 * number3;
    }
}