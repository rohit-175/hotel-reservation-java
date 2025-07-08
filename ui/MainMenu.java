package ui;

import api.HotelResource;
import model.Customer;
import model.IRoom;
import model.Reservation;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Scanner;

public class MainMenu {
    private static final HotelResource hotelResource = HotelResource.getInstance();
    private static final Scanner scanner = new Scanner(System.in);

    public static void displayMainMenu() {
        while (true) {
            System.out.println("\nWelcome to the Hotel Reservation System");
            System.out.println("1. Find and reserve a room");
            System.out.println("2. See my reservations");
            System.out.println("3. Create an account");
            System.out.println("4. Admin");
            System.out.println("5. Exit");

            String userInput = scanner.nextLine();
            switch (userInput) {
                case "1":
                    findAndReserveRoom();
                    break;
                case "2":
                    seeMyReservations();
                    break;
                case "3":
                    createAccount();
                    break;
                case "4":
                    AdminMenu.displayAdminMenu();
                    break;
                case "5":
                    System.out.println("Exiting application...");
                    scanner.close();
                    System.exit(0);
                default:
                    System.out.println("Invalid input. Please select a valid option.");
            }
        }
    }

    private static void findAndReserveRoom() {
        // Parse check-in and check-out dates
        System.out.println("Enter Check-in Date (yyyy-mm-dd): ");
        Date checkInDate = parseDate(scanner.nextLine());
        if (checkInDate == null) {
            System.out.println("Invalid check-in date. Please try again.");
            return;
        }
        System.out.println("Enter Check-out Date (yyyy-mm-dd): ");
        Date checkOutDate = parseDate(scanner.nextLine());
        if (checkOutDate == null) {
            System.out.println("Invalid check-out date. Please try again.");
            return;
        }

        Collection<IRoom> allRooms = hotelResource.findARoom(checkInDate, checkOutDate);

        Collection<IRoom> availableRooms = new ArrayList<>();
        Collection<Reservation> allReservations = hotelResource.getAllReservations();
        for (IRoom room : allRooms) {
            boolean reserved = false;
            for (Reservation res : allReservations) {
                if (res.getRoom().getRoomNumber().equals(room.getRoomNumber())) {
                    if (!(checkOutDate.before(res.getCheckInDate()) || checkInDate.after(res.getCheckOutDate()))) {
                        reserved = true;
                        break;
                    }
                }
            }
            if (!reserved) {
                availableRooms.add(room);
            }
        }

        // If no rooms available for the selected dates, suggest alternate dates after 7 days.
        if (availableRooms.isEmpty()) {
            System.out.println("No rooms available for the selected dates.");
            System.out.println("Would you like to see available rooms for dates after 7 days? (y/n)");
            String answer = scanner.nextLine();
            if (answer.equalsIgnoreCase("y")) {
                Date newCheckIn = new Date(checkInDate.getTime() + 7L * 24 * 60 * 60 * 1000);
                Date newCheckOut = new Date(checkOutDate.getTime() + 7L * 24 * 60 * 60 * 1000);

                Collection<IRoom> suggestedRooms = hotelResource.findARoom(newCheckIn, newCheckOut);
                Collection<IRoom> availableSuggestedRooms = new ArrayList<>();
                for (IRoom room : suggestedRooms) {
                    boolean reserved = false;
                    for (Reservation res : hotelResource.getAllReservations()) {
                        if (res.getRoom().getRoomNumber().equals(room.getRoomNumber())) {
                            if (!(newCheckOut.before(res.getCheckInDate()) || newCheckIn.after(res.getCheckOutDate()))) {
                                reserved = true;
                                break;
                            }
                        }
                    }
                    if (!reserved) {
                        availableSuggestedRooms.add(room);
                    }
                }

                if (availableSuggestedRooms.isEmpty()) {
                    System.out.println("No rooms available even after 7 days.");
                    return;
                } else {
                    System.out.println("Available rooms after 7 days:");
                    for (IRoom room : availableSuggestedRooms) {
                        System.out.println(room);
                    }
                    System.out.println("Would you like to proceed with these new dates? (y/n)");
                    String proceed = scanner.nextLine();
                    if (proceed.equalsIgnoreCase("y")) {
                        checkInDate = newCheckIn;
                        checkOutDate = newCheckOut;
                        availableRooms = availableSuggestedRooms;
                    } else {
                        return;
                    }
                }
            } else {
                return;
            }
        }

        System.out.println("Available rooms:");
        for (IRoom room : availableRooms) {
            System.out.println(room);
        }

        System.out.println("Enter room number to book:");
        String roomNumber = scanner.nextLine();
        IRoom room = hotelResource.getRoom(roomNumber);
        if (room == null || !availableRooms.contains(room)) {
            System.out.println("Invalid room number or room is not available.");
            return;
        }

        System.out.println("Enter your email:");
        String email = scanner.nextLine();
        Customer customer = hotelResource.getCustomer(email);
        if (customer == null) {
            System.out.println("No account found. Please create an account first.");
            return;
        }

        Reservation reservation = hotelResource.bookARoom(email, room, checkInDate, checkOutDate);
        if (reservation == null) {
            System.out.println("Reservation failed. Please try again with a different room or date.");
        } else {
            System.out.println("\nReservation Successful!");
            System.out.println("----------------------------");
            System.out.println("Customer: " + customer.getEmail());
            System.out.println("Room Number: " + room.getRoomNumber() +
                    " | Type: " + room.getRoomType() +
                    " | Price: $" + room.getRoomPrice());
            System.out.println("Check-in Date: " + checkInDate +
                    " | Check-out Date: " + checkOutDate);
            System.out.println("----------------------------\n");
        }
    }


    private static void seeMyReservations() {
        System.out.println("Enter your email:");
        String email = scanner.nextLine();
        Collection<Reservation> reservations = hotelResource.getCustomerReservations(email);

        if (reservations == null || reservations.isEmpty()) {
            System.out.println("No reservations found.");
        } else {
            for (Reservation reservation : reservations) {
                System.out.println(reservation);
            }
        }
    }

    private static void createAccount() {
        System.out.println("Enter email:");
        String email = scanner.nextLine();
        System.out.println("Enter first name:");
        String firstName = scanner.nextLine();
        System.out.println("Enter last name:");
        String lastName = scanner.nextLine();

        try {
            hotelResource.createACustomer(email, firstName, lastName);
            System.out.println("Account successfully created.");
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid email format. Please try again with a valid email (e.g., user@example.com).");
        }
    }


    private static Date parseDate(String dateStr) {
        try {
            return java.sql.Date.valueOf(dateStr);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid date format. Use yyyy-mm-dd.");
            return null;
        }
    }
}
