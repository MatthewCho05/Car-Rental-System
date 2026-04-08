# Car Rental System

A console-based car rental management system built in Java. Customers can search for vehicles, make reservations, and cancel bookings. Administrators can manage car listings and view rental data.

## Requirements

- Java JDK 17 or higher

## How to Compile and Run

1. Place all `.java` files and `.txt` data files in the same directory.
2. Open a terminal in that directory and compile:
   ```
   javac *.java
   ```
3. Run the program:
   ```
   java CarRentalSystem
   ```

## Test Accounts

| Role     | Email                  | Password    |
|----------|------------------------|-------------|
| Admin    | admin123@gmail.com     | password123 |
| Customer | customer123@gmail.com  | password123 |

You can also create a new customer account using the Sign Up option.

## Project Files

### Source Code
- `CarRentalSystem.java` — Main entry point (login, sign up, routing)
- `CustomerPortal.java` — Customer menu (search, reserve, cancel, view reservations)
- `AdminDashboard.java` — Admin menu (view rentals, add/remove car listings)
- `CarListing.java` — Car listing data model
- `CarListingManager.java` — Loads listings and checks availability
- `Rental.java` — Rental data model
- `RentalManager.java` — Handles rental CRUD operations
- `UserManager.java` — Handles authentication and registration

### Data Files
- `users.txt` — Stores registered user credentials
- `car_listings.txt` — Stores available vehicle inventory
- `rentals.txt` — Stores rental/reservation records
