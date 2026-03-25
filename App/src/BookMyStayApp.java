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

        public void display() {
            System.out.println("Reservation ID: " + reservationId);
            System.out.println("Guest Name: " + guestName);
            System.out.println("Room Type: " + roomType);
            System.out.println("----------------------------");
        }
    }

    static class BookingHistory {

        private List<Reservation> history = new ArrayList<>();

        public void addReservation(Reservation reservation) {
            history.add(reservation);
            System.out.println("Added to history: " + reservation.reservationId);
        }

        public List<Reservation> getAllReservations() {
            return Collections.unmodifiableList(history); // read-only
        }

        public void generateReport() {
            System.out.println("\n--- Booking History Report ---\n");
            if (history.isEmpty()) {
                System.out.println("No bookings found.");
                return;
            }
            for (Reservation r : history) {
                r.display();
            }
            System.out.println("Total Bookings: " + history.size());
        }
    }

    public static void main(String[] args) {

        BookingHistory bookingHistory = new BookingHistory();

        Reservation r1 = new Reservation("R101", "Alice", "Single");
        Reservation r2 = new Reservation("R102", "Bob", "Suite");
        Reservation r3 = new Reservation("R103", "Charlie", "Double");

        bookingHistory.addReservation(r1);
        bookingHistory.addReservation(r2);
        bookingHistory.addReservation(r3);

        bookingHistory.generateReport();

        List<Reservation> allReservations = bookingHistory.getAllReservations();
        System.out.println("\nAdmin View (Read-Only List):");
        for (Reservation r : allReservations) {
            System.out.println("- " + r.reservationId + " | " + r.guestName + " | " + r.roomType);
        }
    }
}