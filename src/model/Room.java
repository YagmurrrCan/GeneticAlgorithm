package model;

public class Room {

    String roomId;
    String name;
    String building;
    int number;
    String seatingCapacity;
    boolean availability; /*isFull*/


    Room(String roomId, String name, String building, int number, String seatingCapacity) {
        super();
        this.roomId = roomId;
        this.name = name;
        this.building = building;
        this.number = number;
        this.seatingCapacity = seatingCapacity;
    }

    public Room(String roomId) {
        super();
        this.roomId = roomId;
        this.availability = false;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /*
    public boolean isFull() {

        return isFull;
    }

    public void setFull(boolean full) {
        isFull = full;
    }
    */

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
