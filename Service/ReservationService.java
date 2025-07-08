package Service;

import model.Customer;
import model.IRoom;
import model.Reservation;
import java.util.*;

public class ReservationService {
    private static final ReservationService INSTANCE = new ReservationService();
    private final Map<String, IRoom> roomMap = new HashMap<>();
    private final Map<Customer, Collection<Reservation>> reservationMap = new HashMap<>();

    private ReservationService(){}

    public static ReservationService getInstance(){
        return INSTANCE;
    }

    public void addRoom(IRoom room){
        roomMap.put(room.getRoomNumber(), room);
    }

    public IRoom getARoom(String roomID){
        return roomMap.get(roomID);
    }

    public Reservation reserveARoom(Customer customer, IRoom room, Date checkInDate, Date checkOutDate) {
        for (Collection<Reservation> reservations : reservationMap.values()) {
            for (Reservation res : reservations) {
                if (res.getRoom().equals(room) &&
                        !(checkOutDate.before(res.getCheckInDate()) || checkInDate.after(res.getCheckOutDate()))) {
                    System.out.println("Room is already booked for the selected dates.");
                    return null;
                }
            }
        }

        Reservation reservation = new Reservation(customer, room, checkInDate, checkOutDate);
        reservationMap.computeIfAbsent(customer, k -> new ArrayList<>()).add(reservation);
        //System.out.println("Reservation successful: " + reservation); // Print the actual reservation
        return reservation;
    }



    public Collection<IRoom> findRooms(Date checkInDate, Date checkoutDate) {
        Collection<IRoom> availableRooms = new ArrayList<>(roomMap.values());

        for (Collection<Reservation> reservations : reservationMap.values()) {
            for (Reservation res : reservations) {
                if (!(checkoutDate.before(res.getCheckInDate()) || checkInDate.after(res.getCheckOutDate()))) {
                    availableRooms.remove(res.getRoom());
                }
            }
        }

        return availableRooms;
    }


    public Collection<Reservation> getCustomerReservation(Customer customer){
        return reservationMap.getOrDefault(customer, Collections.emptyList());
    }

    public Collection<IRoom> getAllRooms() {
        return roomMap.values();
    }

    public Collection<Reservation> getAllReservations() {
        Collection<Reservation> allReservations = new ArrayList<>();
        for (Collection<Reservation> reservations : reservationMap.values()) {
            allReservations.addAll(reservations);
        }
        return allReservations;
    }
}
