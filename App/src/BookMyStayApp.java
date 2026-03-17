public class BookMyStayApp {
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

    public class BookMyStayApp {
        public static void main(String[] args) {

            System.out.println("Welcome to the Hotel Booking Management System");
            System.out.println("System initialized successfully.");
        }
        System.out.println();

        Room single = new SingleRoom();
        Room doub = new DoubleRoom();
        Room suite = new SuiteRoom();

        int singleAvailability = 5;
        int doubleAvailability = 3;
        int suiteAvailability = 2;

        single.displayRoom();
        System.out.println("Available: " + singleAvailability);
        System.out.println();

        doub.displayRoom();
        System.out.println("Available: " + doubleAvailability);
        System.out.println();

        suite.displayRoom();
        System.out.println("Available: " + suiteAvailability);
    }
}