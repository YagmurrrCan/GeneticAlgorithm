package model;

import java.util.List;

public class AssignedRoom {

    private String roomId;
    private List<String> bookedTimeslots;


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
    public List<String> getBookedTimeslots() {
        return bookedTimeslots;
    }
    public void setBookedTimeslots(List<String> bookedTimeslots) {
        this.bookedTimeslots = bookedTimeslots;
    }

}
