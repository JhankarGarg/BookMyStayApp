import java.util.*;
import java.util.concurrent.*;

public class BookMyStayApp {

    static class Reservation {
        String reservationId;
        String guestName;
        String roomType;

        public Reservation(String reservationId, String guestName, String roomType) {
            this.reservationId = reservationId;
            this.guestName = guestName;
            this.roomType = roomType;
        }
    }

    static class Inventory {
        private final Map<String, Integer> roomAvailability = new ConcurrentHashMap<>();

        public void addRoom(String type, int count) {
            roomAvailability.put(type, count);
        }

        public synchronized int getAvailability(String type) {
            return roomAvailability.getOrDefault(type, 0);
        }

        public synchronized boolean reduceAvailability(String type) {
            int available = getAvailability(type);
            if (available > 0) {
                roomAvailability.put(type, available - 1);
                return true;
            }
            return false;
        }

        public Map<String, Integer> getAllAvailability() {
            return new HashMap<>(roomAvailability);
        }
    }

    static class BookingService {
        private int roomCounter = 1;
        private final Map<String, Reservation> confirmedBookings = new ConcurrentHashMap<>();
        private final Queue<BookingRequest> bookingQueue = new ConcurrentLinkedQueue<>();

        static class BookingRequest {
            String guestName;
            String roomType;

            BookingRequest(String guestName, String roomType) {
                this.guestName = guestName;
                this.roomType = roomType;
            }
        }

        public void submitBookingRequest(String guestName, String roomType) {
            bookingQueue.offer(new BookingRequest(guestName, roomType));
        }

        public void processRequests(Inventory inventory) {
            while (!bookingQueue.isEmpty()) {
                BookingRequest request = bookingQueue.poll();
                if (request != null) {
                    synchronized (this) {
                        if (inventory.reduceAvailability(request.roomType)) {
                            String reservationId = request.roomType + "-" + roomCounter++;
                            Reservation reservation = new Reservation(reservationId, request.guestName, request.roomType);
                            confirmedBookings.put(reservationId, reservation);
                            System.out.println("Booking confirmed for Guest: " + request.guestName + ", Room ID: " + reservationId);
                        }
                    }
                }
            }
        }

        public void printRemainingInventory(Inventory inventory) {
            System.out.println("\nRemaining Inventory:");
            Map<String, Integer> avail = inventory.getAllAvailability();
            for (String type : Arrays.asList("Single", "Double", "Suite")) {
                System.out.println(type + ": " + avail.getOrDefault(type, 0));
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println("Concurrent Booking Simulation");

        Inventory inventory = new Inventory();
        inventory.addRoom("Single", 4);
        inventory.addRoom("Double", 2);
        inventory.addRoom("Suite", 1);

        BookingService bookingService = new BookingService();

        Runnable user1 = () -> bookingService.submitBookingRequest("Abhi", "Single");
        Runnable user2 = () -> bookingService.submitBookingRequest("Vanmathi", "Double");
        Runnable user3 = () -> bookingService.submitBookingRequest("Kural", "Suite");
        Runnable user4 = () -> bookingService.submitBookingRequest("Subha", "Single");

        ExecutorService executor = Executors.newFixedThreadPool(4);
        executor.execute(user1);
        executor.execute(user2);
        executor.execute(user3);
        executor.execute(user4);

        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);

        Thread processor = new Thread(() -> bookingService.processRequests(inventory));
        processor.start();
        processor.join();

        bookingService.printRemainingInventory(inventory);
    }
}