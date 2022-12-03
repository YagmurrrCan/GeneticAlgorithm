package model;

import java.util.List;

public class AssignedInstructor {
    private String instructorId;
    private List<String> bookedTimeslots;



    public AssignedInstructor(String instructorId) {
        super();
        this.instructorId = instructorId;
    }
    public String getinstructorId() {
        return instructorId;
    }
    public void setRoomId(String instructorId) {
        this.instructorId = instructorId;
    }
    public List<String> getBookedTimeslots() {
        return bookedTimeslots;
    }
    public void setBookedTimeslots(List<String> bookedTimeslots) {
        this.bookedTimeslots = bookedTimeslots;
    }
}