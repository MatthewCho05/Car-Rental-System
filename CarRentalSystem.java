import java.util.Scanner;

public class CarRentalSystem {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        System.out.println("========================================");
        System.out.println("   Welcome to the Car Rental System");
        System.out.println("========================================");

        while (running) {
            System.out.println("\nPlease select an option:");
            System.out.println("1. Login");
            System.out.println("2. Sign Up");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    handleLogin(scanner);
                    break;
                case "2":
                    handleSignUp(scanner);
                    break;
                case "3":
                    running = false;
                    System.out.println("Thank you for using the Car Rental System. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }

        scanner.close();
    }

    private static void handleLogin(Scanner scanner) {
        System.out.println("\n--- Login ---");
        System.out.print("Enter your email: ");
        String email = scanner.nextLine().trim();
        System.out.print("Enter your password: ");
        String password = scanner.nextLine().trim();

        if (UserManager.authenticate(email, password)) {
            System.out.println("Login successful!");

            if (UserManager.isAdmin(email)) {
                // Admin goes to the admin dashboard
                AdminDashboard adminDashboard = new AdminDashboard(scanner);
                adminDashboard.start();
            } else {
                // Regular customer goes to customer portal
                CustomerPortal customerPortal = new CustomerPortal(scanner, email);
                customerPortal.start();
            }
        } else {
            System.out.println("Login failed. Please try again. If you don't have an account, make a new one.");
        }
    }

    private static void handleSignUp(Scanner scanner) {
        System.out.println("\n--- Sign Up ---");
        System.out.print("Enter your email: ");
        String email = scanner.nextLine().trim();

        if (email.isEmpty()) {
            System.out.println("Email cannot be empty.");
            return;
        }

        if (!email.contains("@") || !email.contains(".")) {
            System.out.println("Invalid email format. Please enter a valid email address.");
            return;
        }

        if (UserManager.emailExists(email)) {
            System.out.println("An account with this email already exists. Please login instead.");
            return;
        }

        System.out.print("Enter a password: ");
        String password = scanner.nextLine().trim();

        if (password.isEmpty()) {
            System.out.println("Password cannot be empty.");
            return;
        }

        System.out.print("Confirm your password: ");
        String confirmPassword = scanner.nextLine().trim();

        if (!password.equals(confirmPassword)) {
            System.out.println("Passwords do not match. Please try again.");
            return;
        }

        if (UserManager.registerUser(email, password)) {
            System.out.println("Account created successfully! You can now login.");
        } else {
            System.out.println("Failed to create account. Please try again.");
        }
    }
}
