import java.util.Scanner;
 
public class SentenceAnalyzer {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
 
        System.out.print("Enter a sentence: ");
        String sentence = scanner.nextLine();
 
        // 1. Total number of characters (excluding spaces)
        int totalChars = sentence.replace(" ", "").length();
 
        // 2. Total number of words
        String[] words = sentence.trim().split("\\s+");
        int totalWords = words.length;
 
        // 3. Uppercase
        String uppercase = sentence.toUpperCase();
 
        // 4. Lowercase
        String lowercase = sentence.toLowerCase();
 
        // 5. Reverse the sentence (character by character)
        String reversed = new StringBuilder(sentence).reverse().toString();
 
        // 6. Count vowels
        int vowels = 0;
        for (char c : sentence.toLowerCase().toCharArray()) {
            if ("aeiou".indexOf(c) != -1) {
                vowels++;
            }
        }
 
        // 7. Count consonants
        int consonants = 0;
        for (char c : sentence.toLowerCase().toCharArray()) {
            if (Character.isLetter(c) && "aeiou".indexOf(c) == -1) {
                consonants++;
            }
        }
 
        // 8. Palindrome check (ignore spaces and case)
        String cleaned = sentence.toLowerCase().replace(" ", "");
        String cleanedReversed = new StringBuilder(cleaned).reverse().toString();
        String isPalindrome = cleaned.equals(cleanedReversed) ? "Yes" : "No";
 
        // Display results
        System.out.println("\n===== Sentence Analysis =====");
        System.out.println("Total Characters : " + totalChars);
        System.out.println("Total Words      : " + totalWords);
        System.out.println("Uppercase        : " + uppercase);
        System.out.println("Lowercase        : " + lowercase);
        System.out.println("Reverse          : " + reversed);
        System.out.println("Vowels           : " + vowels);
        System.out.println("Consonants       : " + consonants);
        System.out.println("Palindrome       : " + isPalindrome);
 
        scanner.close();
    }
}
