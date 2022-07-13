package model;

import java.util.ArrayList;

public class Department {

    String departmentName;
    private ArrayList<Course> courses;

    public Department(String departmentName, ArrayList<Course> courses)
    {
        super();
        this.departmentName = departmentName;
        this.courses = courses;
    }


    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public ArrayList<Course> getCourses() {
        return courses;
    }

    public void setCourses(ArrayList<Course> courses) {
        this.courses = courses;
    }


}
