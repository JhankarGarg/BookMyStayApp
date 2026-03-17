import java.util.HashMap;

abstract class Room {
    private String type;
    private int beds;
    private int size;
    private double price;

    public Room(String type, int beds, int size, double price) {
        this.type = type;
        this.beds = beds;
        this.size = size;
        this.price = price;
    }

    public String getType() {
        return type;
    }

    public int getBeds() {
        return beds;
    }

    public int getSize() {
        return size;
    }

    public double getPrice() {
        return price;
    }

    public void displayRoom() {
        System.out.println("Room Type: " + type);
        System.out.println("Beds: " + beds);
        System.out.println("Size: " + size + " sq ft");
        System.out.println("Price: " + price);
    }
}

class SingleRoom extends Room {
    public SingleRoom() {
        super("Single Room", 1, 200, 1500);
    }
}

class DoubleRoom extends Room {
    public DoubleRoom() {
        super("Double Room", 2, 350, 2500);
    }
}

class SuiteRoom extends Room {
    public SuiteRoom() {
        super("Suite Room", 3, 600, 5000);
    }
}

class RoomInventory {

    private HashMap<String, Integer> inventory;

    public RoomInventory() {
        inventory = new HashMap<>();
        inventory.put("Single Room", 5);
        inventory.put("Double Room", 3);
        inventory.put("Suite Room", 2);
    }

    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    public void updateAvailability(String roomType, int count) {
        inventory.put(roomType, count);
    }
}

public class BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("Welcome to the Hotel Booking Management System");
        System.out.println("System initialized successfully.");
        System.out.println();

        Room single = new SingleRoom();
        Room doub = new DoubleRoom();
        Room suite = new SuiteRoom();

        RoomInventory inventory = new RoomInventory();

        single.displayRoom();
        System.out.println("Available: " + inventory.getAvailability(single.getType()));
        System.out.println();

        doub.displayRoom();
        System.out.println("Available: " + inventory.getAvailability(doub.getType()));
        System.out.println();

        suite.displayRoom();
        System.out.println("Available: " + inventory.getAvailability(suite.getType()));
    }
}