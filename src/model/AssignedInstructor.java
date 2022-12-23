/*
Bu sınıf, bir "AssignedInstructor" sınıfı oluşturuyor gibi görünüyor. Sınıfın içinde öğretim elemanının numarası (instructorId) ve öğretim elemanı için ayırılmış zaman dilimlerinin listesi (occupiedTimeslots) gibi bilgileri saklamak için birkaç özellik (field) tanımlanmış.
 */

package model;

import java.util.List;

public class AssignedInstructor {
    private String instructorId;
    private List<String> occupiedTimeslots;

    public AssignedInstructor(String instructorId) {
        super();
        this.instructorId = instructorId;
    }

    public String getInstructorId() {
        return instructorId;
    }

    public void setInstructorId(String instructorId) {
        this.instructorId = instructorId;
    }

    public List<String> getOccupiedTimeslots() {
        return occupiedTimeslots;
    }

    public void setOccupiedTimeslots(List<String> occupiedTimeslots) {
        this.occupiedTimeslots = occupiedTimeslots;
    }

}