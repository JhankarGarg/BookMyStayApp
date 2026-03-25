import java.util.*;
public class BookMyStayApp {

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

    static class SearchService {

        public void searchAvailableRooms(Inventory inventory, Map<String, Room> roomCatalog) {

            System.out.println("\nAvailable Rooms:\n");

            for (String type : inventory.getAllRoomTypes()) {

                int availableCount = inventory.getAvailability(type);

                if (availableCount > 0) {

                    Room room = roomCatalog.get(type);

                    if (room != null) {
                        room.displayDetails();
                        System.out.println("Available  Count: " + availableCount);
                        System.out.println();
                    }
                }
            }
        }
    }

    public static void main(String[] args) {

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
    }
}