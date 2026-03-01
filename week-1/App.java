import java.util.Scanner;

public class App {

    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);

        System.out.print("Enter number 1: ");
        double num1 = input.nextDouble();

        System.out.print("Enter number 2: ");
        double num2 = input.nextDouble();

        System.out.print("Enter number 3: ");
        double num3 = input.nextDouble();

        Math math = new Math(num1, num2, num3);

        System.out.println("Addition result: " + math.add());
        System.out.println("Division result: " + math.divide());
        System.out.println("Multiplication result: " + math.multiple());

        input.close();
    }
}