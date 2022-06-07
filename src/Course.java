/**
 *
 * @author Lenovo
 */
public class Course {
    String name;
    String exam;
    int capacity;


    Course(String name, String exam, int capacity) {
        this.name = name;
        this.exam = exam;
        this.capacity = capacity;
        if (capacity >= 0 && capacity <= 100) {
            this.capacity = capacity;
        } else {
            this.capacity = 0;
        }

    }

}
