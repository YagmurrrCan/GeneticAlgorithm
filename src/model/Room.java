/*
Bu sınıf, bir "Room" sınıfı oluşturuyor gibi görünüyor. Sınıfın içinde odanın numarası (roomId), odanın adı (roomName), oda hangi binada (building), hangi numarada (number) ve kaç kişilik bir kapasiteye sahip olduğu (seatingCapacity) gibi bilgileri saklamak için birkaç özellik (field) tanımlanmış. Ayrıca, oda mevcut olup olmadığını (availability) belirten bir boolean değişken de var.
 */

package model;

public class Room {

    String roomId;
    String roomName;
    String building;
    int number;
    String seatingCapacity;
    boolean availability;


    Room(String roomId, String roomName, String building, int number, String seatingCapacity) {
        super();
        this.roomId = roomId;
        this.roomName = roomName;
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

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public boolean isAvailability() {
        return availability;
    }

    public void setAvailability(boolean availability) {
        this.availability = availability;
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
