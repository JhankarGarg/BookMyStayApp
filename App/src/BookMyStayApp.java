import java.util.*;

public class BookMyStayApp {

    // Room (Domain Model)
    static class Room {
        private String type;
        private double price;
        private List<String> amenities;

        public Room(String type, double price, List<String> amenities) {
            this.type = type;
            this.price = price;
            this.amenities = amenities;
        }

        public String getType() {
            return type;
        }

        public double getPrice() {
            return price;
        }

        public List<String> getAmenities() {
            return amenities;
        }

        public void displayDetails() {
            System.out.println("Room Type: " + type);
            System.out.println("Price: ₹" + price);
            System.out.println("Amenities: " + String.join(", ", amenities));
            System.out.println("----------------------------");
        }
    }

    // Inventory (State Holder)
    static class Inventory {
        private Map<String, Integer> roomAvailability = new HashMap<>();

        public void addRoom(String type, int count) {
            roomAvailability.put(type, count);
        }

        public int getAvailability(String type) {
            return roomAvailability.getOrDefault(type, 0);
        }

        public Set<String> getAllRoomTypes() {
            return roomAvailability.keySet();
        }
    }

    // Search Service (Read-only)
    static class SearchService {

        public void searchAvailableRooms(Inventory inventory, Map<String, Room> roomCatalog) {

            System.out.println("\nAvailable Rooms:\n");

            for (String type : inventory.getAllRoomTypes()) {

                int availableCount = inventory.getAvailability(type);

                if (availableCount > 0) {

                    Room room = roomCatalog.get(type);

                    if (room != null) {
                        room.displayDetails();
                        System.out.println("Available Count: " + availableCount);
                        System.out.println();
                    }
                }
            }
        }
    }

    // Reservation (Booking Request)
    static class Reservation {
        private String guestName;
        private String roomType;

        public Reservation(String guestName, String roomType) {
            this.guestName = guestName;
            this.roomType = roomType;
        }

        public void display() {
            System.out.println("Guest: " + guestName + " | Requested Room: " + roomType);
        }
    }

    // Booking Request Queue (FIFO)
    static class BookingRequestQueue {
        private Queue<Reservation> queue = new LinkedList<>();

        // Add request
        public void addRequest(Reservation reservation) {
            queue.offer(reservation);
            System.out.println("Request added for " + reservation.guestName);
        }

        // View requests
        public void viewRequests() {
            System.out.println("\nBooking Requests (FIFO Order):\n");

            if (queue.isEmpty()) {
                System.out.println("No pending requests.");
                return;
            }

            for (Reservation r : queue) {
                r.display();
            }
        }
    }

    // Main Method
    public static void main(String[] args) {

        // -------- Use Case 4: Search --------
        Inventory inventory = new Inventory();
        inventory.addRoom("Single", 3);
        inventory.addRoom("Double", 0);
        inventory.addRoom("Suite", 2);

        Map<String, Room> roomCatalog = new HashMap<>();

        roomCatalog.put("Single",
                new Room("Single", 1500, Arrays.asList("WiFi", "TV")));

        roomCatalog.put("Double",
                new Room("Double", 2500, Arrays.asList("WiFi", "TV", "AC")));

        roomCatalog.put("Suite",
                new Room("Suite", 5000, Arrays.asList("WiFi", "TV", "AC", "Mini Bar")));

        SearchService searchService = new SearchService();
        searchService.searchAvailableRooms(inventory, roomCatalog);

        // -------- Use Case 5: Booking Requests --------
        BookingRequestQueue requestQueue = new BookingRequestQueue();

        requestQueue.addRequest(new Reservation("Alice", "Single"));
        requestQueue.addRequest(new Reservation("Bob", "Suite"));
        requestQueue.addRequest(new Reservation("Charlie", "Double"));

        requestQueue.viewRequests();
    }
}