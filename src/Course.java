/**
 *
 * @author Lenovo
 */
public class Course {

    int courseId;
    String exam;
    int capacity;


    Course(int courseId, String exam, int capacity) {
        this.courseId = courseId;
        this.exam = exam;
        this.capacity = capacity;
        if (capacity >= 0 && capacity <= 100) {
            this.capacity = capacity;
        } else {
            this.capacity = 0;
        }

    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

}
