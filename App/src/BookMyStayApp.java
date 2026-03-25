import java.io.*;
import java.util.*;
import java.util.concurrent.*;

public class BookMyStayApp implements Serializable {

    private static final long serialVersionUID = 1L;

    static class Reservation implements Serializable {
        private static final long serialVersionUID = 1L;
        String reservationId;
        String guestName;
        String roomType;

        public Reservation(String reservationId, String guestName, String roomType) {
            this.reservationId = reservationId;
            this.guestName = guestName;
            this.roomType = roomType;
        }
    }

    static class Inventory implements Serializable {
        private static final long serialVersionUID = 1L;
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

        public synchronized void increaseAvailability(String type) {
            roomAvailability.put(type, getAvailability(type) + 1);
        }
    }

    static class BookingService implements Serializable {
        private static final long serialVersionUID = 1L;

        private int roomCounter = 1;
        private final Map<String, Reservation> confirmedBookings = new ConcurrentHashMap<>();
        private final Queue<BookingRequest> bookingQueue = new ConcurrentLinkedQueue<>();

        static class BookingRequest implements Serializable {
            private static final long serialVersionUID = 1L;
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
                        } else {
                            System.out.println("Booking Failed: No rooms available for type: " + request.roomType + " (Guest: " + request.guestName + ")");
                        }
                    }
                }
            }
        }

        public Map<String, Reservation> getConfirmedBookings() {
            return confirmedBookings;
        }
    }

    static class PersistenceService {

        public static void saveState(String filename, Inventory inventory, BookingService bookingService) {
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
                oos.writeObject(inventory);
                oos.writeObject(bookingService);
                System.out.println("\nSystem state saved to file: " + filename);
            } catch (IOException e) {
                System.err.println("Error saving state: " + e.getMessage());
            }
        }

        public static Object[] loadState(String filename) {
            File file = new File(filename);
            if (!file.exists()) {
                System.out.println("No saved state file found. Starting fresh.");
                return null;
            }
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
                Inventory inventory = (Inventory) ois.readObject();
                BookingService bookingService = (BookingService) ois.readObject();
                System.out.println("System state loaded from file: " + filename);
                return new Object[]{inventory, bookingService};
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Error loading state: " + e.getMessage());
                return null;
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        final String stateFile = "booking_system_state.dat";

        Object[] loadedState = PersistenceService.loadState(stateFile);
        Inventory inventory;
        BookingService bookingService;

        if (loadedState == null) {
            inventory = new Inventory();
            bookingService = new BookingService();

            inventory.addRoom("Single", 4);
            inventory.addRoom("Double", 2);
            inventory.addRoom("Suite", 1);

            bookingService.submitBookingRequest("Abhi", "Single");
            bookingService.submitBookingRequest("Vanmathi", "Double");
            bookingService.submitBookingRequest("Kural", "Suite");
            bookingService.submitBookingRequest("Subha", "Single");
        } else {
            inventory = (Inventory) loadedState[0];
            bookingService = (BookingService) loadedState[1];
        }

        Thread processor = new Thread(() -> bookingService.processRequests(inventory));
        processor.start();
        processor.join();

        System.out.println("\nConfirmed Bookings After Processing:");
        for (Reservation r : bookingService.getConfirmedBookings().values()) {
            System.out.println(r.reservationId + " | " + r.guestName + " | " + r.roomType);
        }

        System.out.println("\nInventory Status:");
        Map<String, Integer> avail = inventory.getAllAvailability();
        for (String type : Arrays.asList("Single", "Double", "Suite")) {
            System.out.println(type + ": " + avail.getOrDefault(type, 0));
        }

        PersistenceService.saveState(stateFile, inventory, bookingService);
    }
}