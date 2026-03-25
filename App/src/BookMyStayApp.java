import java.util.*;

public class BookMyStayApp {

    static class Reservation {
        String guestName;
        String roomType;

        public Reservation(String guestName, String roomType) {
            this.guestName = guestName;
            this.roomType = roomType;
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

        public void reduceAvailability(String type) {
            roomAvailability.put(type, getAvailability(type) - 1);
        }
    }

    static class BookingService {

        private Set<String> allocatedRoomIds = new HashSet<>();
        private Map<String, Set<String>> roomAllocations = new HashMap<>();
        private int roomCounter = 1;

        public void processBookings(Queue<Reservation> queue, Inventory inventory) {

            System.out.println("\nProcessing Booking Requests...\n");

            while (!queue.isEmpty()) {

                Reservation r = queue.poll(); // FIFO

                int available = inventory.getAvailability(r.roomType);

                if (available > 0) {

                    String roomId = r.roomType + "-" + roomCounter++;

                    if (!allocatedRoomIds.contains(roomId)) {

                        allocatedRoomIds.add(roomId);

                        roomAllocations
                                .computeIfAbsent(r.roomType, k -> new HashSet<>())
                                .add(roomId);

                        inventory.reduceAvailability(r.roomType);

                        System.out.println("Booking Confirmed!");
                        System.out.println("Guest: " + r.guestName);
                        System.out.println("Room Type: " + r.roomType);
                        System.out.println("Room ID: " + roomId);
                        System.out.println("----------------------------");

                    }
                } else {
                    System.out.println("Booking Failed for " + r.guestName +
                            " (No " + r.roomType + " rooms available)");
                }
            }
        }
    }

    public static void main(String[] args) {

        Inventory inventory = new Inventory();
        inventory.addRoom("Single", 2);
        inventory.addRoom("Double", 1);
        inventory.addRoom("Suite", 1);

        Queue<Reservation> queue = new LinkedList<>();
        queue.offer(new Reservation("Alice", "Single"));
        queue.offer(new Reservation("Bob", "Suite"));
        queue.offer(new Reservation("Charlie", "Single"));
        queue.offer(new Reservation("David", "Single")); // will fail

        BookingService bookingService = new BookingService();
        bookingService.processBookings(queue, inventory);
    }
}