import java.util.*;

public class BookMyStayApp {

    // Reservation (Represents confirmed booking)
    static class Reservation {
        String reservationId;
        String guestName;

        public Reservation(String reservationId, String guestName) {
            this.reservationId = reservationId;
            this.guestName = guestName;
        }
    }

    // Add-On Service
    static class AddOnService {
        String serviceName;
        double price;

        public AddOnService(String serviceName, double price) {
            this.serviceName =  serviceName;
            this.price = price;
        }
    }

    // Add-On Service Manager
    static class AddOnServiceManager {

        // Map: Reservation ID → List of Services
        private Map<String, List<AddOnService>> serviceMap = new HashMap<>();

        // Add service to reservation
        public void addService(String reservationId, AddOnService service) {
            serviceMap
                    .computeIfAbsent(reservationId, k -> new ArrayList<>())
                    .add(service);

            System.out.println("Added service: " + service.serviceName +
                    " to Reservation ID: " + reservationId);
        }

        // View services
        public void viewServices(String reservationId) {

            List<AddOnService> services = serviceMap.get(reservationId);

            if (services == null || services.isEmpty()) {
                System.out.println("No add-on services for Reservation ID: " + reservationId);
                return;
            }

            System.out.println("\nServices for Reservation ID: " + reservationId);

            for (AddOnService s : services) {
                System.out.println("- " + s.serviceName + " (₹" + s.price + ")");
            }
        }

        // Calculate total cost
        public double calculateTotalCost(String reservationId) {

            List<AddOnService> services = serviceMap.get(reservationId);

            double total = 0;

            if (services != null) {
                for (AddOnService s : services) {
                    total += s.price;
                }
            }

            return total;
        }
    }

    // Main Method
    public static void main(String[] args) {

        // Sample Reservations (already confirmed)
        Reservation r1 = new Reservation("R101", "Alice");
        Reservation r2 = new Reservation("R102", "Bob");

        // Add-On Services
        AddOnService wifi = new AddOnService("Premium WiFi", 200);
        AddOnService breakfast = new AddOnService("Breakfast", 300);
        AddOnService spa = new AddOnService("Spa Access", 800);

        // Manager
        AddOnServiceManager manager = new AddOnServiceManager();

        // Add services
        manager.addService(r1.reservationId, wifi);
        manager.addService(r1.reservationId, breakfast);
        manager.addService(r2.reservationId, spa);

        // View services
        manager.viewServices(r1.reservationId);
        manager.viewServices(r2.reservationId);

        // Calculate cost
        System.out.println("\nTotal Add-On Cost for " + r1.reservationId +
                ": ₹" + manager.calculateTotalCost(r1.reservationId));

        System.out.println("Total Add-On Cost for " + r2.reservationId +
                ": ₹" + manager.calculateTotalCost(r2.reservationId));
    }
}