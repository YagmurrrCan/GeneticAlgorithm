package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Solution {

    private String examName;
    private List<String> assignedRooms;

    public Solution(String name) {
        this.examName = name;
    }

    public String getExamName() {
        return examName;
    }
    public void setExamName(String examName) {
        this.examName = examName;
    }
    public List<String> getAssignedRooms() {
        return assignedRooms;
    }
    public void setAssignedRooms(List<String> assignedRooms) {
        this.assignedRooms = assignedRooms;
    }

    /*
    public boolean isValid() {
        // Eğer atanmış sınav salonu sayısı 1'den azsa geçersizdir
        if (assignedRooms.size() < 1) {
            return false;
        }
        return true;
    }

     */

}
