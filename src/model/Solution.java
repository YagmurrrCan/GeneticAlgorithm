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
    public List<Solution> assignRooms(int[] genes) {
        // Öğrenci grubunun sınav salonlarına atama işlemini gerçekleştirir

        List<Solution> solutions = new ArrayList<>();

        for (int i = 0; i < genes.length; i++) {
            Solution solution = new Solution(exams.get(i).getExamName());
            solution.setAssignedRooms(Arrays.asList(rooms.get(genes[i]).getRoomId()));

            // Eğer sınav salonu ataması geçerliyse, Solution nesnesine ekle
            if (solution.isValid()) {
                solutions.add(solution);
            }
        }

        return solutions;
    }


 */
    // bu metod, sınav salonlarına atamanın geçerli olup olmadığını kontrol eder
    public boolean isValid() {
        // Eğer atanmış sınav salonu sayısı 1'den azsa geçersizdir
        if (assignedRooms.size() < 1) {
            return false;
        }

        // Diğer geçerliliği kontrol eden logikleri buraya yazılacak

        // Eğer tüm kontroller geçerliyse, atanmış sınav salonları geçerlidir
        return true;
    }

}
