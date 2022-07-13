package model;

import java.util.ArrayList;

/**
 *
 * @author Lenovo
 */
public class Course {

    private String courseId;
    private String exam;
    private String name = null;
    private int maxNumbOfStudents;
    private ArrayList<Instructor> instructors;


    public Course(String courseId, String exam, String name, int maxNumbOfStudents, ArrayList<Instructor> instructors) {
        super();
        this.courseId = courseId;
        this.exam = exam;
        this.name = name;
        this.maxNumbOfStudents = maxNumbOfStudents;
        this.instructors = instructors;

    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getExam() {
        return exam;
    }

    public void setExam(String exam) {
        this.exam = exam;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMaxNumbOfStudents() {
        return maxNumbOfStudents;
    }

    public void setMaxNumbOfStudents(int maxNumbOfStudents) {
        this.maxNumbOfStudents = maxNumbOfStudents;
    }

    public ArrayList<Instructor> getInstructors() {
        return instructors;
    }

    public void setInstructors(ArrayList<Instructor> instructors) {
        this.instructors = instructors;
    }


    @Override
    public String toString() {
        return "Course{" +
                "name='" + name + '\'' +
                '}';
    }

}
