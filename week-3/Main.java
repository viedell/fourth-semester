public class Main {
    public static void main(String[] args) {

        // PrintedBook objects
        PrintedBook pb1 = new PrintedBook("Java Programming", "James Smith", 2022, 250);
        PrintedBook pb2 = new PrintedBook("Clean Code", "Robert Martin", 2008, 431);

        // EBook objects
        EBook eb1 = new EBook("AI Basics", "Sarah Lee", 2023, 40);
        EBook eb2 = new EBook("Data Science 101", "Emily Clark", 2021, 75);

        // Display info for all books
        System.out.println("===== PRINTED BOOKS =====");

        pb1.displayInfo();
        pb1.readingTime();

        System.out.println();

        pb2.displayInfo();
        pb2.readingTime();

        System.out.println("\n===== EBOOKS =====");

        eb1.displayInfo();
        eb1.downloadTime(10);

        System.out.println();

        eb2.displayInfo();
        eb2.downloadTime(25);
    }
}