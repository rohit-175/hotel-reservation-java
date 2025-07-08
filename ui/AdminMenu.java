package ui;

import api.AdminResource;
import model.Customer;
import model.IRoom;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;

public class AdminMenu {
    private static final AdminResource adminResource = AdminResource.getInstance();
    private static final Scanner scanner = new Scanner(System.in);

    public static void displayAdminMenu() {
        while (true) {
            System.out.println("\nAdmin Menu");
            System.out.println("1. See all Customers");
            System.out.println("2. See all Rooms");
            System.out.println("3. See all Reservations");
            System.out.println("4. Add a Room");
            System.out.println("5. Back to Main Menu");

            String userInput = scanner.nextLine();
            switch (userInput) {
                case "1":
                    seeAllCustomers();
                    break;
                case "2":
                    seeAllRooms();
                    break;
                case "3":
                    adminResource.displayAllReservations();
                    break;
                case "4":
                    addRoom();
                    break;
                case "5":
                    return;
                default:
                    System.out.println("Invalid input. Please try again.");
            }
        }
    }

    private static void seeAllCustomers() {
        Collection<Customer> customers = adminResource.getAllCustomers();
        if (customers.isEmpty()) {
            System.out.println("No customers found.");
        } else {
            for (Customer customer : customers) {
                System.out.println(customer);
            }
        }
    }

    private static void seeAllRooms() {
        Collection<IRoom> rooms = adminResource.getAllRooms();
        if (rooms.isEmpty()) {
            System.out.println("No rooms available.");
        } else {
            for (IRoom room : rooms) {
                System.out.println(room);
            }
        }
    }

    private static void addRoom() {
        while (true) {
            System.out.println("Enter room number:");
            String roomNumber = scanner.nextLine();

            System.out.println("Enter price per night:");
            double price;
            try {
                price = Double.parseDouble(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid price. Try again.");
                continue;
            }

            System.out.println("Enter room type (SINGLE/DOUBLE):");
            String roomTypeStr = scanner.nextLine().toUpperCase();
            model.RoomType roomType;
            try {
                roomType = model.RoomType.valueOf(roomTypeStr);
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid room type. Use SINGLE or DOUBLE.");
                continue;
            }

            IRoom room = new model.Room(roomNumber, price, roomType);
            List<IRoom> rooms = new ArrayList<>();
            rooms.add(room);
            adminResource.addRoom(rooms);
            // System.out.println("Room added successfully!");

            System.out.println("Would you like to add another room? (y/n)");
            if (!scanner.nextLine().equalsIgnoreCase("y")) {
                break;
            }
        }
    }

}
