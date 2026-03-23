public class PrintedBook extends Book {
    int pages;

    PrintedBook(String title, String author, int year, int pages) {
        super(title, author, year);
        this.pages = pages;
    }

    void readingTime() {
        int time = pages * 2;
        System.out.println("Pages: " + pages);
        System.out.println("Reading Time: " + time + " minutes");
    }
}