import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class CarListingManager {
    private static final String LISTINGS_FILE = "car_listings.txt";
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    // Load all car listings from the file
    public static List<CarListing> loadListings() {
        List<CarListing> listings = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(LISTINGS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                String[] parts = line.split(",");
                if (parts.length == 6) {
                    CarListing listing = new CarListing(
                        parts[0].trim(),
                        parts[1].trim(),
                        parts[2].trim(),
                        Double.parseDouble(parts[3].trim()),
                        parts[4].trim(),
                        parts[5].trim()
                    );
                    listings.add(listing);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Car listings file not found.");
        } catch (IOException e) {
            System.out.println("Error reading car listings file: " + e.getMessage());
        }
        return listings;
    }

    // Search for available cars based on requested rental dates
    // A car is available if its availability window fully covers the requested dates
    // AND it is not already booked (in rentals.txt) for overlapping dates
    public static List<CarListing> searchAvailableCars(String startDateStr, String endDateStr) {
        LocalDate requestedStart = LocalDate.parse(startDateStr, DATE_FORMAT);
        LocalDate requestedEnd = LocalDate.parse(endDateStr, DATE_FORMAT);

        List<CarListing> allListings = loadListings();
        List<Rental> allRentals = RentalManager.loadRentals();
        List<CarListing> available = new ArrayList<>();

        for (CarListing listing : allListings) {
            LocalDate availFrom = LocalDate.parse(listing.getAvailableFrom(), DATE_FORMAT);
            LocalDate availTo = LocalDate.parse(listing.getAvailableTo(), DATE_FORMAT);

            // Check if the car's availability window covers the requested dates
            if (!requestedStart.isBefore(availFrom) && !requestedEnd.isAfter(availTo)) {
                // Check if the car is already booked for overlapping dates
                boolean isBooked = false;
                for (Rental rental : allRentals) {
                    if (rental.getVehicleId().equalsIgnoreCase(listing.getVehicleId())) {
                        String status = rental.getStatus();
                        if (status.equalsIgnoreCase("Active") || status.equalsIgnoreCase("Upcoming")) {
                            LocalDate rentalStart = LocalDate.parse(rental.getStartDate(), DATE_FORMAT);
                            LocalDate rentalEnd = LocalDate.parse(rental.getEndDate(), DATE_FORMAT);
                            // Check for date overlap
                            if (!requestedEnd.isBefore(rentalStart) && !requestedStart.isAfter(rentalEnd)) {
                                isBooked = true;
                                break;
                            }
                        }
                    }
                }
                if (!isBooked) {
                    available.add(listing);
                }
            }
        }
        return available;
    }
}
