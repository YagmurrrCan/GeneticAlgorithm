public class Exam {

    String examName;
    int time;
    int roomnumber;


    Exam(String examName, int time, int roomnumber) {
        this.examName = examName;
        this.time = time;
        this.roomnumber = roomnumber;
    }

    public Exam(String c) {
    }

    public String getExamName() {
        return examName;
    }

    public void setExamName(String examName) {
        this.examName = examName;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getRoomnumber() {
        return roomnumber;
    }

    public void setRoomnumber(int roomnumber) {
        this.roomnumber = roomnumber;
    }
}