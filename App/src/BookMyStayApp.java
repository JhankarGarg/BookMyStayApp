import java.util.*;

public class BookMyStayApp {

    static class InvalidBookingException extends Exception {
        public InvalidBookingException(String message) {
            super(message);
        }
    }

    static class Inventory {
        private Map<String, Integer> roomAvailability = new HashMap<>();

        public void addRoom(String type, int count) {
            if (type == null || type.isEmpty() || count < 0) {
                throw new IllegalArgumentException("Invalid room type or count");
            }
            roomAvailability.put(type, count);
        }

        public int getAvailability(String type) throws InvalidBookingException {
            if (!roomAvailability.containsKey(type)) {
                throw new InvalidBookingException("Room type not found: " + type);
            }
            return roomAvailability.get(type);
        }

        public void reduceAvailability(String type) throws InvalidBookingException {
            int available = getAvailability(type);
            if (available <= 0) {
                throw new InvalidBookingException("No rooms available for type: " + type);
            }
            roomAvailability.put(type, available - 1);
        }
    }

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

    static class BookingService {

        private int roomCounter = 1;

        public Reservation bookRoom(String guestName, String roomType, Inventory inventory)
                throws InvalidBookingException {

            if (guestName == null || guestName.isEmpty()) {
                throw new InvalidBookingException("Guest name cannot be empty");
            }

            inventory.reduceAvailability(roomType);

            String reservationId = roomType + "-" + roomCounter++;
            System.out.println("Booking Confirmed: " + reservationId + " for " + guestName);
            return new Reservation(reservationId, guestName, roomType);
        }
    }

    public static void main(String[] args) {

        Inventory inventory = new Inventory();
        inventory.addRoom("Single", 2);
        inventory.addRoom("Double", 1);

        BookingService bookingService = new BookingService();

        List<Reservation> confirmedBookings = new ArrayList<>();

        String[][] bookingRequests = {
                {"Alice", "Single"},
                {"Bob", "Suite"},
                {"", "Single"},
                {"Charlie", "Single"},
                {"David", "Double"},
                {"Eve", "Double"}
        };

        for (String[] request : bookingRequests) {
            try {
                Reservation r = bookingService.bookRoom(request[0], request[1], inventory);
                confirmedBookings.add(r);
            } catch (InvalidBookingException | IllegalArgumentException e) {
                System.out.println("Booking Failed: " + e.getMessage());
            }
        }

        System.out.println("\nConfirmed Bookings:");
        for (Reservation r : confirmedBookings) {
            System.out.println(r.reservationId + " | " + r.guestName + " | " + r.roomType);
        }
    }
}