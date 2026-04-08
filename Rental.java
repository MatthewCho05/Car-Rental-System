public class Rental {
    private String vehicleId;
    private String category;
    private String vehicleName;
    private String customerEmail;
    private String startDate;
    private String endDate;
    private String status;

    public Rental(String vehicleId, String category, String vehicleName,
                  String customerEmail, String startDate, String endDate, String status) {
        this.vehicleId = vehicleId;
        this.category = category;
        this.vehicleName = vehicleName;
        this.customerEmail = customerEmail;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
    }

    public String getVehicleId() { return vehicleId; }
    public String getCategory() { return category; }
    public String getVehicleName() { return vehicleName; }
    public String getCustomerEmail() { return customerEmail; }
    public String getStartDate() { return startDate; }
    public String getEndDate() { return endDate; }
    public String getStatus() { return status; }

    public void setStatus(String status) { this.status = status; }

    // Format for writing back to rentals.txt
    public String toFileString() {
        return vehicleId + "," + category + "," + vehicleName + ","
               + customerEmail + "," + startDate + "," + endDate + "," + status;
    }

    // Display full details of a rental
    public void displayDetails() {
        System.out.println("  Vehicle ID:     " + vehicleId);
        System.out.println("  Vehicle Name:   " + vehicleName);
        System.out.println("  Category:       " + category);
        System.out.println("  Customer Email: " + customerEmail);
        System.out.println("  Start Date:     " + startDate);
        System.out.println("  End Date:       " + endDate);
        System.out.println("  Status:         " + status);
        System.out.println("  ----------------------------------------");
    }
}
