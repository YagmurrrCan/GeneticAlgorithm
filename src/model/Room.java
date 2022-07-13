package model;

public class Room {

    String roomId;
    String building;
    int number;
    String seatingCapacity;


    Room(String roomId, String building, int number, String seatingCapacity) {
        super();
        this.roomId = roomId;
        this.building = building;
        this.number = number;
        this.seatingCapacity = seatingCapacity;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public String getSeatingCapacity() {
        return seatingCapacity;
    }

    public void setSeatingCapacity(String seatingCapacity) {
        this.seatingCapacity = seatingCapacity;
    }

}
