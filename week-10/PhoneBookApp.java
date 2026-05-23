import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PhoneBookApp {

    // Define the file name as a constant
    private static final String FILE_NAME = "phone.txt";
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        ensureFileExists();
        boolean running = true;

        while (running) {
            System.out.println("\n=== Phone Contact Manager ===");
            System.out.println("1. Add Contact");
            System.out.println("2. View Contacts");
            System.out.println("3. Search Contact");
            System.out.println("4. Delete Contact");
            System.out.println("5. Exit");
            System.out.print("Choose an option (1-5): ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    addContact();
                    break;
                case "2":
                    viewContacts();
                    break;
                case "3":
                    searchContact();
                    break;
                case "4":
                    deleteContact();
                    break;
                case "5":
                    System.out.println("Exiting application. Goodbye!");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice! Please select a number between 1 and 5.");
            }
        }
    }

    /**
     * Checks if the file exists; if not, creates a new blank file.
     */
    private static void ensureFileExists() {
        try {
            File file = new File(FILE_NAME);
            if (file.createNewFile()) {
                System.out.println("Created new contact file: " + FILE_NAME);
            }
        } catch (IOException e) {
            System.out.println("Error initializing file: " + e.getMessage());
        }
    }

    /**
     * Adds a new contact to the file. Includes duplicate checking.
     */
    private static void addContact() {
        System.out.print("Enter Name: ");
        String name = scanner.nextLine().trim();
        
        System.out.print("Enter Phone Number: ");
        String phone = scanner.nextLine().trim();

        // Bonus: Validate input
        if (name.isEmpty() || phone.isEmpty()) {
            System.out.println("Error: Name and Phone number cannot be empty.");
            return;
        }

        // Bonus: Prevent duplicate phone numbers
        if (isDuplicatePhone(phone)) {
            System.out.println("Error: The phone number '" + phone + "' is already saved!");
            return;
        }

        // Write to file using try-with-resources (auto-closes resources)
        // The 'true' parameter in FileWriter enables append mode
        try (FileWriter fw = new FileWriter(FILE_NAME, true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
             
            out.println(name + " - " + phone);
            System.out.println("Contact successfully added!");
            
        } catch (IOException e) {
            System.out.println("Error saving contact: " + e.getMessage());
        }
    }

    /**
     * Reads and displays all contacts from the text file.
     */
    private static void viewContacts() {
        System.out.println("\n--- All Contacts ---");
        boolean hasContacts = false;

        try (FileReader fr = new FileReader(FILE_NAME);
             BufferedReader br = new BufferedReader(fr)) {
             
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
                hasContacts = true;
            }
            
            if (!hasContacts) {
                System.out.println("No contacts found. The phonebook is empty.");
            }
            
        } catch (IOException e) {
            System.out.println("Error reading contacts: " + e.getMessage());
        }
        System.out.println("--------------------");
    }

    /**
     * Searches for a contact by name or phone number.
     */
    private static void searchContact() {
        System.out.print("Enter search keyword (Name or Phone): ");
        String keyword = scanner.nextLine().toLowerCase().trim();
        boolean found = false;

        System.out.println("\n--- Search Results ---");
        try (FileReader fr = new FileReader(FILE_NAME);
             BufferedReader br = new BufferedReader(fr)) {
             
            String line;
            while ((line = br.readLine()) != null) {
                // Case-insensitive search
                if (line.toLowerCase().contains(keyword)) {
                    System.out.println(line);
                    found = true;
                }
            }
            
            if (!found) {
                System.out.println("No contacts matched your search.");
            }
            
        } catch (IOException e) {
            System.out.println("Error searching contacts: " + e.getMessage());
        }
        System.out.println("----------------------");
    }

    /**
     * Deletes a contact by keyword. It reads all lines, skips the one matching
     * the keyword, and writes the rest back to the file.
     */
    private static void deleteContact() {
        System.out.print("Enter the exact Name or Phone of the contact to delete: ");
        String keyword = scanner.nextLine().toLowerCase().trim();
        
        List<String> updatedContacts = new ArrayList<>();
        boolean contactFound = false;

        // Step 1: Read all contacts and filter out the one to delete
        try (FileReader fr = new FileReader(FILE_NAME);
             BufferedReader br = new BufferedReader(fr)) {
             
            String line;
            while ((line = br.readLine()) != null) {
                if (line.toLowerCase().contains(keyword)) {
                    contactFound = true;
                    System.out.println("Deleted: " + line);
                    // We skip adding this line to the updated list
                } else {
                    updatedContacts.add(line);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading file during deletion: " + e.getMessage());
            return;
        }

        // Step 2: Rewrite the file if a contact was found and removed
        if (contactFound) {
            try (FileWriter fw = new FileWriter(FILE_NAME, false); // 'false' overwrites the file
                 BufferedWriter bw = new BufferedWriter(fw);
                 PrintWriter out = new PrintWriter(bw)) {
                 
                for (String contact : updatedContacts) {
                    out.println(contact);
                }
                
            } catch (IOException e) {
                System.out.println("Error updating file: " + e.getMessage());
            }
        } else {
            System.out.println("No matching contact found to delete.");
        }
    }

    /**
     * Helper function to check if a phone number already exists in the file.
     */
    private static boolean isDuplicatePhone(String phoneToCheck) {
        try (FileReader fr = new FileReader(FILE_NAME);
             BufferedReader br = new BufferedReader(fr)) {
             
            String line;
            while ((line = br.readLine()) != null) {
                // Split the line by " - " to isolate the phone number part
                String[] parts = line.split(" - ");
                if (parts.length == 2) {
                    String existingPhone = parts[1].trim();
                    if (existingPhone.equals(phoneToCheck)) {
                        return true;
                    }
                }
            }
        } catch (IOException e) {
            // If file doesn't exist yet, it can't be a duplicate
        }
        return false;
    }
}