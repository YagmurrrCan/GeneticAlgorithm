package model;

public class SlotCounter {
    int examId;
    // example: exam with id 1 had been assigned to the timeslot with id 2, 10 times
    // meaning: exam.count[2] = 10
    int count[] = new int[50]; // for each timeslot

    public void increaseCount(int timeslotId) {
        this.count[timeslotId] +=1;
    }

    public SlotCounter(int examId) {
        super();
        this.examId = examId;
    }
    public int getExamId() {
        return this.examId;
    }
    public void setName(int examId) {
        this.examId = examId;
    }
    public int[] getCount() {
        return count;
    }
    public void setCount(int[] count) {
        this.count = count;
    }
}