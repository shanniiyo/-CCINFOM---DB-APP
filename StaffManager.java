import java.util.ArrayList;

public class StaffManager {
    private ArrayList<Staff> staffList = new ArrayList<>();

    public void addStaff(Staff s) {
        staffList.add(s);
        System.out.println("\nStaff added successfully: " + s.getName());
    }

    public Staff findStaff(int staffID) {
        for (Staff s : staffList) {
            if (s.getStaffID() == staffID) {
                return s;
            }
        }
        return null;
    }

    public void viewStaff(int staffID) {
        Staff s = findStaff(staffID);

        if (s == null) {
            System.out.println("Staff not found.");
            return;
        }

        s.displayInfo();
    }

    public void listAllStaff() {
        System.out.println("\n=== STAFF LIST ===");
        for (Staff s : staffList) {
            System.out.println(s.getStaffID() + " - " + s.getName() + 
                " (" + (s.isActive() ? "Active" : "Inactive") + ")");
        }
    }

    public void setStaffStatus(int staffID, boolean status) {
        Staff s = findStaff(staffID);

        if (s == null) {
            System.out.println("Staff not found.");
            return;
        }

        s.setActive(status);
        System.out.println("Updated status for " + s.getName());
    }

    public void assignQuota(int staffID, double quota) {
        Staff s = findStaff(staffID);

        if (s == null) {
            System.out.println("Staff not found.");
            return;
        }

        s.setQuota(quota);
        System.out.println("Quota updated.");
    }

    public void addSalesToStaff(int staffID, double sales) {
        Staff s = findStaff(staffID);

        if (s == null) {
            System.out.println("Staff not found.");
            return;
        }

        s.addSales(sales);
        System.out.println("Sales updated for " + s.getName());
    }

    public void viewStaffSalesPerformance(int staffID) {
        Staff s = findStaff(staffID);

        if (s == null) {
            System.out.println("Staff not found.");
            return;
        }

        s.viewSalesPerformance();
    }
}
