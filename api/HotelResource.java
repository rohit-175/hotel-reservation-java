package api;

import Service.CustomerService;
import Service.ReservationService;
import model.Customer;
import model.IRoom;
import model.Reservation;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;

public class HotelResource {
    private static final HotelResource INSTANCE = new HotelResource();
    private static final CustomerService customerService = CustomerService.getInstance();
    private final ReservationService reservationService = ReservationService.getInstance();

    private HotelResource(){}

    public static HotelResource getInstance() {
        return INSTANCE;
    }

    public Customer getCustomer(String email){
        return customerService.getCustomer(email);
    }

    public boolean createACustomer(String email, String firstName, String lastName) {
        return customerService.addCustomer(email, firstName, lastName);
    }


    public IRoom getRoom(String roomNumber){
        return reservationService.getARoom(roomNumber);
    }

    public Reservation bookARoom(String customerEmail, IRoom room, Date checkInDate, Date checkOutDate){
        Customer customer = customerService.getCustomer(customerEmail);
        return (customer != null) ? reservationService.reserveARoom(customer, room, checkInDate, checkOutDate) : null;
    }

    public Collection<Reservation> getCustomerReservations(String customerEmail) {
        Customer customer = customerService.getCustomer(customerEmail);
        if (customer == null) {
            //System.out.println("No reservations found for this email: " + customerEmail);
            return Collections.emptyList();
        }
        return reservationService.getCustomerReservation(customer);
    }

    public Collection<IRoom> findARoom(Date checkIn, Date checkOut){
        return reservationService.findRooms(checkIn, checkOut);
    }

    public Collection<Reservation> getAllReservations() {
        return reservationService.getAllReservations();
    }
}
