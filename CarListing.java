public class CarListing {
    private String vehicleId;
    private String category;
    private String vehicleName;
    private double dailyRate;
    private String availableFrom;
    private String availableTo;

    public CarListing(String vehicleId, String category, String vehicleName,
                      double dailyRate, String availableFrom, String availableTo) {
        this.vehicleId = vehicleId;
        this.category = category;
        this.vehicleName = vehicleName;
        this.dailyRate = dailyRate;
        this.availableFrom = availableFrom;
        this.availableTo = availableTo;
    }

    public String getVehicleId() { return vehicleId; }
    public String getCategory() { return category; }
    public String getVehicleName() { return vehicleName; }
    public double getDailyRate() { return dailyRate; }
    public String getAvailableFrom() { return availableFrom; }
    public String getAvailableTo() { return availableTo; }

    public String toFileString() {
        return vehicleId + "," + category + "," + vehicleName + ","
               + dailyRate + "," + availableFrom + "," + availableTo;
    }

    public void displayListing() {
        System.out.println("  Vehicle ID:     " + vehicleId);
        System.out.println("  Vehicle Name:   " + vehicleName);
        System.out.println("  Category:       " + category);
        System.out.println("  Daily Rate:     $" + String.format("%.2f", dailyRate));
        System.out.println("  Available From: " + availableFrom);
        System.out.println("  Available To:   " + availableTo);
        System.out.println("  ----------------------------------------");
    }
}
