package model;

public class Room implements IRoom {
    private final String roomNumber;
    private final double roomPrice;
    private final RoomType roomType;

    public Room(String roomNumber, double roomPrice, RoomType roomType) {
        this.roomNumber = roomNumber;
        this.roomPrice = roomPrice;
        this.roomType = roomType;
    }

    @Override
    public String getRoomNumber() {
        return roomNumber;
    }

    @Override
    public double getRoomPrice() {
        return roomPrice;
    }

    @Override
    public RoomType getRoomType() {
        return roomType;
    }

    @Override
    public boolean isFree() {
        return roomPrice == 0.0;
    }

    @Override
    public String toString() {
        return "Room{" +
                "roomNumber= " + roomNumber +
                ", roomPrice= " + roomPrice +
                ", roomType= " + roomType +
                ", isFree= " + isFree() +
                "}";
    }
}
