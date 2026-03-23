public class EBook extends Book {
    double fileSize;

    EBook(String title, String author, int year, double fileSize) {
        super(title, author, year);
        this.fileSize = fileSize;
    }

    void downloadTime(double speed) {
        double time = fileSize / speed;
        System.out.println("File Size: " + (int) fileSize + " MB");
        System.out.println("Download Time at " + (int) speed + " MB/s: " + (int) time + " seconds");
    }
}