/*
Bu sınıf, bir "AssignedExam" sınıfı oluşturuyor gibi görünüyor. Sınıfın içinde sınavın numarası (examId) ve sınav için ayırılmış zaman dilimlerinin listesi (occupiedTimeslots) gibi bilgileri saklamak için birkaç özellik (field) tanımlanmış.
 */

package model;

import java.util.List;

public class AssignedExam {
    private String examId;
    private List<String> occupiedTimeslots;

    public AssignedExam(String roomId) {
        super();
        this.examId = examId;
    }

    public String getExamId() {
        return examId;
    }

    public void setExamId(String examId) {
        this.examId = examId;
    }

    public List<String> getOccupiedTimeslots() {
        return occupiedTimeslots;
    }

    public void setOccupiedTimeslots(List<String> occupiedTimeslots) {
        this.occupiedTimeslots = occupiedTimeslots;
    }
}
