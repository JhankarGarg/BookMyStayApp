import java.util.*;

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
        private Map<String, Integer> roomAvailability = new HashMap<>();

        public void addRoom(String type, int count) {
            roomAvailability.put(type, count);
        }

        public int getAvailability(String type) {
            return roomAvailability.getOrDefault(type, 0);
        }

        public void increaseAvailability(String type) {
            roomAvailability.put(type, getAvailability(type) + 1);
        }
    }

    static class BookingService {
        private int roomCounter = 1;
        private Map<String, Reservation> confirmedBookings = new HashMap<>();
        private Stack<String> rollbackStack = new Stack<>();

        public Reservation bookRoom(String guestName, String roomType, Inventory inventory) {
            if (guestName == null || guestName.isEmpty()) {
                System.out.println("Booking Failed: Guest name cannot be empty");
                return null;
            }
            if (inventory.getAvailability(roomType) <= 0) {
                System.out.println("Booking Failed: No rooms available for type: " + roomType);
                return null;
            }
            String reservationId = roomType + "-" + roomCounter++;
            Reservation r = new Reservation(reservationId, guestName, roomType);
            confirmedBookings.put(reservationId, r);
            rollbackStack.push(reservationId);
            // Reduce availability since booking confirmed
            inventory.addRoom(roomType, inventory.getAvailability(roomType) - 1);
            System.out.println("Booking Confirmed: " + reservationId + " for " + guestName);
            return r;
        }

        public void cancelBooking(String reservationId, Inventory inventory) {
            if (!confirmedBookings.containsKey(reservationId)) {
                System.out.println("Cancellation Failed: Reservation not found: " + reservationId);
                return;
            }
            Reservation r = confirmedBookings.remove(reservationId);
            rollbackStack.remove(reservationId);
            inventory.increaseAvailability(r.roomType);

            System.out.println("\nBooking Cancellation");
            System.out.println("Booking cancelled successfully. Inventory restored for room type: " + r.roomType);
            System.out.println("\nRollback History (Most Recent First):");
            ListIterator<String> iter = rollbackStack.listIterator(rollbackStack.size());
            while (iter.hasPrevious()) {
                System.out.println("Released Reservation ID: " + iter.previous());
            }
            if (rollbackStack.isEmpty()) {
                System.out.println("No released reservations remaining.");
            }
            System.out.println("\nUpdated " + r.roomType + " Room Availability: " + inventory.getAvailability(r.roomType));
        }

        public void viewBookings() {
            if (confirmedBookings.isEmpty()) {
                System.out.println("No confirmed bookings.");
                return;
            }
            System.out.println("\nConfirmed Bookings:");
            for (Reservation r : confirmedBookings.values()) {
                System.out.println(r.reservationId + " | " + r.guestName + " | " + r.roomType);
            }
        }
    }

    public static void main(String[] args) {

        Inventory inventory = new Inventory();
        inventory.addRoom("Single", 5);
        inventory.addRoom("Double", 1);

        BookingService bookingService = new BookingService();

        Reservation r1 = bookingService.bookRoom("Alice", "Single", inventory);
        Reservation r2 = bookingService.bookRoom("Bob", "Double", inventory);
        Reservation r3 = bookingService.bookRoom("Charlie", "Single", inventory);

        bookingService.viewBookings();

        bookingService.cancelBooking(r1.reservationId, inventory);

        bookingService.viewBookings();
    }
}