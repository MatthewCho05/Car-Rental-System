import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CustomerPortal {
    private Scanner scanner;
    private String customerEmail;
    private static final LocalDate CURRENT_DATE = LocalDate.now();
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public CustomerPortal(Scanner scanner, String email) {
        this.scanner = scanner;
        this.customerEmail = email;
    }

    public void start() {
        System.out.println("\n========================================");
        System.out.println("         Customer Portal");
        System.out.println("========================================");
        System.out.println("Welcome, " + customerEmail + "!");

        boolean inPortal = true;
        while (inPortal) {
            System.out.println("\nWhat would you like to do?");
            System.out.println("1. Search & Reserve a Vehicle");
            System.out.println("2. View Reservations");
            System.out.println("3. Cancel a Reservation");
            System.out.println("4. Back to Main Menu");
            System.out.print("Enter your choice: ");
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                case "Search & Reserve a Vehicle":
                    searchAndReserve();
                    break;
                case "2":
                case "View Reservations":
                    viewReservations();
                    break;
                case "3":
                case "Cancel a Reservation":
                    cancelReservation();
                    break;
                case "4":
                case "Back to Main Menu":
                    inPortal = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void searchAndReserve() {
        System.out.println("\n--- Search Available Vehicles ---");

        String startDateStr = "";
        String endDateStr = "";
        LocalDate startDate = null;
        LocalDate endDate = null;

        // Get and validate start date
        while (startDate == null) {
            System.out.print("Enter the start date for rental (yyyy-MM-dd): ");
            startDateStr = scanner.nextLine().trim();
            try {
                startDate = LocalDate.parse(startDateStr, DATE_FORMAT);
                if (startDate.isBefore(CURRENT_DATE)) {
                    System.out.println("Start date cannot be in the past. Please try again.");
                    startDate = null;
                }
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Please use yyyy-MM-dd.");
            }
        }

        // Get and validate end date
        while (endDate == null) {
            System.out.print("Enter the end date for rental (yyyy-MM-dd): ");
            endDateStr = scanner.nextLine().trim();
            try {
                endDate = LocalDate.parse(endDateStr, DATE_FORMAT);
                if (endDate.isBefore(startDate)) {
                    System.out.println("End date cannot be before start date. Try again.");
                    endDate = null;
                } else if (endDate.isEqual(startDate)) {
                    System.out.println("End date must be after start date. Try again.");
                    endDate = null;
                }
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Please use yyyy-MM-dd.");
            }
        }

        // Search for available cars
        List<CarListing> availableCars = CarListingManager.searchAvailableCars(startDateStr, endDateStr);

        if (availableCars.isEmpty()) {
            System.out.println("\nNo vehicles are available for the selected dates.");
            return;
        }

        long rentalDays = ChronoUnit.DAYS.between(startDate, endDate);

        System.out.println("\n========================================");
        System.out.println("     Available Vehicles");
        System.out.println("     " + startDateStr + " to " + endDateStr + " (" + rentalDays + " days)");
        System.out.println("========================================");

        int count = 1;
        for (CarListing car : availableCars) {
            double totalCost = car.getDailyRate() * rentalDays;
            System.out.println(count + ". " + car.getVehicleName() + " (" + car.getCategory() + ")");
            System.out.println("   Vehicle ID: " + car.getVehicleId());
            System.out.println("   Daily Rate: $" + String.format("%.2f", car.getDailyRate()));
            System.out.println("   Total Cost: $" + String.format("%.2f", totalCost));
            System.out.println("   ----------------------------------------");
            count++;
        }

        // Ask if they want to reserve
        System.out.print("\nWould you like to reserve a vehicle? (yes/no): ");
        String answer = scanner.nextLine().trim().toLowerCase();

        if (answer.equals("yes") || answer.equals("y")) {
            System.out.print("Enter the number of the vehicle you want to reserve: ");
            String numStr = scanner.nextLine().trim();
            try {
                int selection = Integer.parseInt(numStr);
                if (selection >= 1 && selection <= availableCars.size()) {
                    CarListing selected = availableCars.get(selection - 1);

                    // Determine status
                    String status;
                    if (!startDate.isAfter(CURRENT_DATE) && !endDate.isBefore(CURRENT_DATE)) {
                        status = "Active";
                    } else if (startDate.isAfter(CURRENT_DATE)) {
                        status = "Upcoming";
                    } else {
                        status = "Past";
                    }

                    Rental newRental = new Rental(
                        selected.getVehicleId(),
                        selected.getCategory(),
                        selected.getVehicleName(),
                        customerEmail,
                        startDateStr,
                        endDateStr,
                        status
                    );

                    RentalManager.addRental(newRental);

                    double totalCost = selected.getDailyRate() * rentalDays;
                    System.out.println("\nReservation confirmed!");
                    System.out.println("Vehicle: " + selected.getVehicleName() + " (" + selected.getVehicleId() + ")");
                    System.out.println("Dates: " + startDateStr + " to " + endDateStr);
                    System.out.println("Total Cost: $" + String.format("%.2f", totalCost));
                    System.out.println("Status: " + status);
                } else {
                    System.out.println("Invalid selection.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Reservation cancelled.");
            }
        } else {
            System.out.println("Reservation cancelled.");
        }
    }

    private void viewReservations() {
        System.out.println("\n--- Your Reservations ---");

        List<Rental> myRentals = RentalManager.getRentalsByCustomer(customerEmail);

        if (myRentals.isEmpty()) {
            System.out.println("There are currently no reservations under this account.");
            return;
        }

        boolean hasPast = false;
        boolean hasFuture = false;

        // Display upcoming / active reservations
        System.out.println("\n>> Upcoming & Active Reservations:");
        for (Rental r : myRentals) {
            LocalDate endDate = LocalDate.parse(r.getEndDate(), DATE_FORMAT);
            if (!endDate.isBefore(CURRENT_DATE)) {
                hasFuture = true;
                r.displayDetails();
            }
        }
        if (!hasFuture) {
            System.out.println("  No upcoming or active reservations.");
        }

        // Display past reservations
        System.out.println("\n>> Past Reservations:");
        for (Rental r : myRentals) {
            LocalDate endDate = LocalDate.parse(r.getEndDate(), DATE_FORMAT);
            if (endDate.isBefore(CURRENT_DATE)) {
                hasPast = true;
                r.displayDetails();
            }
        }
        if (!hasPast) {
            System.out.println("  No past reservations.");
        }
    }

    private void cancelReservation() {
        System.out.println("\n--- Cancel a Reservation ---");

        List<Rental> myRentals = RentalManager.getRentalsByCustomer(customerEmail);
        List<Rental> cancellable = new ArrayList<>();

        for (Rental r : myRentals) {
            if (r.getStatus().equalsIgnoreCase("Active") || r.getStatus().equalsIgnoreCase("Upcoming")) {
                cancellable.add(r);
            }
        }

        if (cancellable.isEmpty()) {
            System.out.println("You have no active or upcoming reservations to cancel.");
            return;
        }

        System.out.println("Your cancellable reservations:");
        int count = 1;
        for (Rental r : cancellable) {
            System.out.println(count + ". " + r.getVehicleName() + " (" + r.getVehicleId() + ") | "
                    + r.getStartDate() + " to " + r.getEndDate() + " | Status: " + r.getStatus());
            count++;
        }

        System.out.print("\nEnter the number of the reservation to cancel (or 0 to go back): ");
        String input = scanner.nextLine().trim();
        try {
            int selection = Integer.parseInt(input);
            if (selection == 0) return;
            if (selection >= 1 && selection <= cancellable.size()) {
                Rental toCancel = cancellable.get(selection - 1);
                System.out.print("Are you sure you want to cancel the reservation for "
                        + toCancel.getVehicleName() + "? (yes/no): ");
                String confirm = scanner.nextLine().trim().toLowerCase();
                if (confirm.equals("yes") || confirm.equals("y")) {
                    RentalManager.removeRental(toCancel.getVehicleId(), toCancel.getCustomerEmail(),
                            toCancel.getStartDate());
                    System.out.println("Reservation cancelled successfully.");
                } else {
                    System.out.println("Cancellation aborted.");
                }
            } else {
                System.out.println("Invalid selection.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
        }
    }
}
