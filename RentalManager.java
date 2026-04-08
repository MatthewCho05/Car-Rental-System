import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class RentalManager {
    private static final String RENTALS_FILE = "rentals.txt";

    // Read all rentals from the file
    public static List<Rental> loadRentals() {
        List<Rental> rentals = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(RENTALS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                String[] parts = line.split(",");
                if (parts.length == 7) {
                    Rental rental = new Rental(
                        parts[0].trim(), // vehicleId
                        parts[1].trim(), // category
                        parts[2].trim(), // vehicleName
                        parts[3].trim(), // customerEmail
                        parts[4].trim(), // startDate
                        parts[5].trim(), // endDate
                        parts[6].trim()  // status
                    );
                    rentals.add(rental);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Rentals file not found. A new one will be created.");
        } catch (IOException e) {
            System.out.println("Error reading rentals file: " + e.getMessage());
        }
        return rentals;
    }

    // Save all rentals back to the file
    public static void saveRentals(List<Rental> rentals) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(RENTALS_FILE))) {
            for (Rental rental : rentals) {
                writer.write(rental.toFileString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving rentals file: " + e.getMessage());
        }
    }

    // Add a new rental and save
    public static void addRental(Rental rental) {
        List<Rental> rentals = loadRentals();
        rentals.add(rental);
        saveRentals(rentals);
    }

    // Get all rentals for a specific customer email
    public static List<Rental> getRentalsByCustomer(String email) {
        List<Rental> all = loadRentals();
        List<Rental> customerRentals = new ArrayList<>();
        for (Rental r : all) {
            if (r.getCustomerEmail().equalsIgnoreCase(email)) {
                customerRentals.add(r);
            }
        }
        return customerRentals;
    }

    // Get rentals filtered by status
    public static List<Rental> getRentalsByStatus(String status) {
        List<Rental> all = loadRentals();
        List<Rental> filtered = new ArrayList<>();
        for (Rental r : all) {
            if (r.getStatus().equalsIgnoreCase(status)) {
                filtered.add(r);
            }
        }
        return filtered;
    }

    // Remove a rental by matching vehicleId, email, and start date
    public static void removeRental(String vehicleId, String email, String startDate) {
        List<Rental> rentals = loadRentals();
        rentals.removeIf(r ->
            r.getVehicleId().equalsIgnoreCase(vehicleId) &&
            r.getCustomerEmail().equalsIgnoreCase(email) &&
            r.getStartDate().equals(startDate)
        );
        saveRentals(rentals);
    }
}
