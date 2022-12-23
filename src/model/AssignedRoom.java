/*
Bu sınıf, bir "AssignedRoom" sınıfı oluşturuyor gibi görünüyor. Sınıfın içinde sınav salonunun numarası (roomId) ve salon için ayırılmış zaman dilimlerinin listesi (occupiedTimeslots) gibi bilgileri saklamak için birkaç özellik (field) tanımlanmış.
 */

package model;

import java.util.List;

public class AssignedRoom {

    private String roomId;
    private List<String> occupiedTimeslots;


    public AssignedRoom(String roomId) {
        super();
        this.roomId = roomId;
    }

    public String getRoomId() {
        return roomId;
    }
    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }
    public List<String> getOccupiedTimeslots() {
        return occupiedTimeslots;
    }

    public void setOccupiedTimeslots(List<String> occupiedTimeslots) {
        this.occupiedTimeslots = occupiedTimeslots;
    }

}
