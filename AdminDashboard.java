import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class AdminDashboard {
    private Scanner scanner;

    public AdminDashboard(Scanner scanner) {
        this.scanner = scanner;
    }

    public void start() {
        System.out.println("\n========================================");
        System.out.println("       Administrator Dashboard");
        System.out.println("========================================");

        boolean inDashboard = true;
        while (inDashboard) {
            System.out.println("\nWhat would you like to do?");
            System.out.println("1. Active Rentals");
            System.out.println("2. Upcoming Rentals");
            System.out.println("3. All Rental Data");
            System.out.println("4. Add a Car Listing");
            System.out.println("5. Remove a Car Listing");
            System.out.println("6. View All Car Listings");
            System.out.println("7. Back to Main Menu");
            System.out.print("Enter your choice: ");
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    viewActiveRentals();
                    break;
                case "2":
                    viewUpcomingRentals();
                    break;
                case "3":
                    viewAllRentalData();
                    break;
                case "4":
                    addCarListing();
                    break;
                case "5":
                    removeCarListing();
                    break;
                case "6":
                    viewAllCarListings();
                    break;
                case "7":
                    inDashboard = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void viewActiveRentals() {
        System.out.println("\n========================================");
        System.out.println("          Active Rentals");
        System.out.println("========================================");

        List<Rental> activeRentals = RentalManager.getRentalsByStatus("Active");

        if (activeRentals.isEmpty()) {
            System.out.println("No active rentals found.");
            return;
        }

        System.out.println("Total Active Rentals: " + activeRentals.size());
        System.out.println("----------------------------------------");
        int count = 1;
        for (Rental r : activeRentals) {
            System.out.println("Rental #" + count + ":");
            r.displayDetails();
            count++;
        }
    }

    private void viewUpcomingRentals() {
        System.out.println("\n========================================");
        System.out.println("         Upcoming Rentals");
        System.out.println("========================================");

        List<Rental> upcomingRentals = RentalManager.getRentalsByStatus("Upcoming");

        if (upcomingRentals.isEmpty()) {
            System.out.println("No upcoming rentals found.");
            return;
        }

        System.out.println("Total Upcoming Rentals: " + upcomingRentals.size());
        System.out.println("----------------------------------------");
        int count = 1;
        for (Rental r : upcomingRentals) {
            System.out.println("Rental #" + count + ":");
            r.displayDetails();
            count++;
        }
    }

    private void viewAllRentalData() {
        System.out.println("\n========================================");
        System.out.println("         All Rental Data");
        System.out.println("========================================");

        List<Rental> allRentals = RentalManager.loadRentals();

        if (allRentals.isEmpty()) {
            System.out.println("No rental data found.");
            return;
        }

        System.out.println("Total Rentals in System: " + allRentals.size());
        System.out.println("----------------------------------------");

        int active = 0, upcoming = 0, past = 0;
        for (Rental r : allRentals) {
            switch (r.getStatus()) {
                case "Active": active++; break;
                case "Upcoming": upcoming++; break;
                case "Past": past++; break;
            }
        }
        System.out.println("  Active:   " + active);
        System.out.println("  Upcoming: " + upcoming);
        System.out.println("  Past:     " + past);
        System.out.println("----------------------------------------");

        int count = 1;
        for (Rental r : allRentals) {
            System.out.println("Rental #" + count + ":");
            r.displayDetails();
            count++;
        }
    }

    private void viewAllCarListings() {
        System.out.println("\n========================================");
        System.out.println("        All Car Listings");
        System.out.println("========================================");

        List<CarListing> listings = CarListingManager.loadListings();
        if (listings.isEmpty()) {
            System.out.println("No car listings found.");
            return;
        }

        int count = 1;
        for (CarListing car : listings) {
            System.out.println(count + ".");
            car.displayListing();
            count++;
        }
    }

    private void addCarListing() {
        System.out.println("\n--- Add a New Car Listing ---");

        System.out.print("Vehicle ID (e.g., VEH601): ");
        String vehicleId = scanner.nextLine().trim();
        if (vehicleId.isEmpty()) { System.out.println("Vehicle ID cannot be empty."); return; }

        System.out.print("Category (SUV, Sedan, Truck, Van, Luxury): ");
        String category = scanner.nextLine().trim();

        System.out.print("Vehicle Name (e.g., Toyota Corolla): ");
        String vehicleName = scanner.nextLine().trim();

        System.out.print("Daily Rate (e.g., 45.00): ");
        double dailyRate;
        try {
            dailyRate = Double.parseDouble(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid rate. Listing not added.");
            return;
        }

        System.out.print("Available From (yyyy-MM-dd): ");
        String availFrom = scanner.nextLine().trim();

        System.out.print("Available To (yyyy-MM-dd): ");
        String availTo = scanner.nextLine().trim();

        CarListing newListing = new CarListing(vehicleId, category, vehicleName, dailyRate, availFrom, availTo);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("car_listings.txt", true))) {
            writer.write(newListing.toFileString());
            writer.newLine();
            System.out.println("Car listing added successfully!");
        } catch (IOException e) {
            System.out.println("Error adding car listing: " + e.getMessage());
        }
    }

    private void removeCarListing() {
        System.out.println("\n--- Remove a Car Listing ---");

        List<CarListing> listings = CarListingManager.loadListings();
        if (listings.isEmpty()) {
            System.out.println("No car listings to remove.");
            return;
        }

        int count = 1;
        for (CarListing car : listings) {
            System.out.println(count + ". " + car.getVehicleName() + " (" + car.getVehicleId() + ")");
            count++;
        }

        System.out.print("Enter the number of the listing to remove (or 0 to cancel): ");
        String input = scanner.nextLine().trim();
        try {
            int selection = Integer.parseInt(input);
            if (selection == 0) return;
            if (selection >= 1 && selection <= listings.size()) {
                CarListing toRemove = listings.get(selection - 1);
                System.out.print("Remove " + toRemove.getVehicleName() + "? (yes/no): ");
                String confirm = scanner.nextLine().trim().toLowerCase();
                if (confirm.equals("yes") || confirm.equals("y")) {
                    listings.remove(selection - 1);
                    try (BufferedWriter writer = new BufferedWriter(new FileWriter("car_listings.txt"))) {
                        for (CarListing car : listings) {
                            writer.write(car.toFileString());
                            writer.newLine();
                        }
                    }
                    System.out.println("Car listing removed successfully.");
                } else {
                    System.out.println("Removal cancelled.");
                }
            } else {
                System.out.println("Invalid selection.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
        } catch (IOException e) {
            System.out.println("Error saving listings: " + e.getMessage());
        }
    }
}
