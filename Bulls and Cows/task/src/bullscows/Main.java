package bullscows;

import java.security.SecureRandom;
import java.util.Scanner;

public class Main {

    private static final SecureRandom RANDOM = new SecureRandom();

    public static void main(String[] args) {
        try {
            game(generateSecretCode());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static String generateSecretCode() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please, enter the secret code's length:");
        String input = scanner.nextLine();
        int length;
        int radix;
        try {
            length = Integer.parseInt(input);
            System.out.println("Input the number of possible symbols in the code:");
            input = scanner.nextLine();
            radix = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            throw new NumberFormatException(String.format("Error: \"%s\" isn't a valid number.", input));
        }
        if (length > radix || length <= 0 || radix <= 1) {
            throw new IllegalArgumentException(String
                    .format("Error: it's not possible to generate a code with a length of %d with %d unique symbols.", length, radix));
        }
        if (radix > Character.MAX_RADIX) {
            throw new IllegalArgumentException("Error: maximum number of possible symbols in the code is 36 (0-9, a-z).");
        }
        String secretCode = generateRandomNumberWithUniqueDigits(length, radix);
        System.out.printf("The secret is prepared: %s", "*".repeat(length));
        if (radix <= 10) {
            System.out.printf(" (0-%d).%n", radix - 1);
        } else {
            System.out.printf(" (0-9, a-%c).%n", Character.forDigit(radix - 1, radix));
        }
        return secretCode;
    }

    public static void game(String secretCode) {
        System.out.println("Okay, let's start a game!");
        Scanner scanner = new Scanner(System.in);
        int length = secretCode.length();
        int turnCounter = 0;
        while (true) {
            System.out.printf("Turn %d:%n", turnCounter++);
            String userInput = scanner.next();
            boolean[] isBull = new boolean[length];
            int bulls = 0;
            int cows = 0;
            for (int i = 0; i < secretCode.length() && i < userInput.length(); i++) {
                if (userInput.charAt(i) == secretCode.charAt(i)) {
                    isBull[i] = true;
                    bulls++;
                }
            }
            for (int i = 0; i < secretCode.length() && i < userInput.length(); i++) {
                int index = secretCode.indexOf(userInput.charAt(i));
                if (index != -1 && !isBull[index]) {
                    cows++;
                }
            }
            System.out.println(answerGrade(bulls, cows));
            if (bulls == length) {
                System.out.println("Congratulations! You guessed the secret code.");
                break;
            }
        }
    }

    public static String generateRandomNumberWithUniqueDigits(int length, int radix) {
        StringBuilder randomNumber = new StringBuilder();
        while (randomNumber.length() < length) {
            char digit = Character.forDigit(RANDOM.nextInt(radix), radix);
            if (randomNumber.indexOf(String.valueOf(digit)) == -1) {
                randomNumber.append(digit);
            }

        }
        return randomNumber.toString();
    }

    public static String answerGrade(int bulls, int cows) {
        if (bulls == 0 && cows == 0) {
            return "Grade: None.";
        }
        return String.format("Grade: %d bull(s) and %d cow(s).", bulls, cows);
    }

}
