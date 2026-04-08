import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class UserManager {
    private static final String USERS_FILE = "users.txt";
    private static final String ADMIN_EMAIL = "admin123@gmail.com";

    // Check if email and password match an existing account
    public static boolean authenticate(String email, String password) {
        List<String[]> users = loadUsers();
        for (String[] user : users) {
            if (user[0].equalsIgnoreCase(email) && user[1].equals(password)) {
                return true;
            }
        }
        return false;
    }

    // Check if an email is already registered
    public static boolean emailExists(String email) {
        List<String[]> users = loadUsers();
        for (String[] user : users) {
            if (user[0].equalsIgnoreCase(email)) {
                return true;
            }
        }
        return false;
    }

    // Register a new user account
    public static boolean registerUser(String email, String password) {
        if (emailExists(email)) {
            return false;
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(USERS_FILE, true))) {
            writer.write(email + "," + password);
            writer.newLine();
            return true;
        } catch (IOException e) {
            System.out.println("Error saving user: " + e.getMessage());
            return false;
        }
    }

    // Check if the logged-in user is the admin
    public static boolean isAdmin(String email) {
        return email.equalsIgnoreCase(ADMIN_EMAIL);
    }

    // Load all users from the file
    private static List<String[]> loadUsers() {
        List<String[]> users = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(USERS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                String[] parts = line.split(",", 2);
                if (parts.length == 2) {
                    users.add(new String[]{parts[0].trim(), parts[1].trim()});
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Users file not found. A new one will be created.");
        } catch (IOException e) {
            System.out.println("Error reading users file: " + e.getMessage());
        }
        return users;
    }
}
