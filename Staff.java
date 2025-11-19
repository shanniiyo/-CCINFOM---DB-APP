public class Staff {

    private int staffID;          
    private String name;
    private String credentials;     
    private boolean active;     
    private double salesQuota;  
    private double totalSales;  

    // Constructor
    public Staff(int staffID, String name, String credentials, boolean active) {
        this.staffID = staffID;
        this.name = name;
        this.credentials = credentials;
        this.active = active;
        this.salesQuota = 2000.0;    
        this.totalSales = 0.0;
    }

    // Getters
    public int getStaffID() {
        return staffID;
    }

    public String getName() {
        return name;
    }

    public String getCredentials() {
        return credentials;
    }

    public boolean isActive() {
        return active;
    }

    public double getSalesQuota() {
        return salesQuota;
    }

    public double getTotalSales() {
        return totalSales;
    }

    // Setters
    public void setActive(boolean active) {
        this.active = active;
    }

    public void setQuota(double quota) {
        this.salesQuota = quota;
    }

    // Add sales to staff total
    public void addSales(double sales) {
        this.totalSales += sales;
    }

    // Display Staff Info
    public void displayInfo() {
        System.out.println("\n=== STAFF INFO ===");
        System.out.println("ID: " + staffID);
        System.out.println("Name: " + name);
        System.out.println("Credentials: " + credentials);
        System.out.println("Status: " + (active ? "Active" : "Inactive"));
        System.out.println("Sales Quota: ₱" + salesQuota);
        System.out.println("Total Sales: ₱" + totalSales);
    }

    // Performance Report
    public void viewSalesPerformance() {
        System.out.println("\n===== SALES PERFORMANCE =====");
        System.out.println("Staff: " + name + " (" + staffID + ")");
        System.out.println("Monthly Sales: ₱" + totalSales);
        System.out.println("Sales Quota: ₱" + salesQuota);

        if (totalSales >= salesQuota) {
            System.out.println("STATUS: Quota Achieved!");
        } else {
            double lacking = salesQuota - totalSales;
            System.out.println("STATUS: Quota NOT met. Lacking: ₱" + lacking);
        }
    }
}
