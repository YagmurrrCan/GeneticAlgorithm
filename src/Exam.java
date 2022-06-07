public class Exam {

    String name;
    String instructor;
    int time;
    int roomnumber;


    Exam(String name, String instructor, int time, int roomnumber) {
        this.name = name;
        this.instructor = instructor;
        this.time = time;
        this.roomnumber = roomnumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInstructor() {
        return instructor;
    }

    public void setInstructor(String instructor) {
        this.instructor = instructor;
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