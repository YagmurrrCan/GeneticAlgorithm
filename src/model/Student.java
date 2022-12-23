/*
Bu sınıf, öğrenciler için bir "Student" sınıfı oluşturuyor gibi görünüyor. Sınıfın içinde öğrenci numarası, öğrencinin okuduğu program (course), öğrencinin almış olduğu sınıflar (classes) ve öğrenciye atanmış sınavlar (assignedExams) gibi bilgileri saklamak için birkaç özellik (field) tanımlanmış. Ayrıca, öğrencinin sınıflarına yeni bir sınıf eklemeyi (addToClasses) ve belirli bir sınıfın öğrencinin sınıfları arasında olup olmadığını kontrol etmeyi (classExist) sağlayan iki adet yöntem (method) de var.
 */

package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

//constructor
public class Student {
    private String studentId;
   // private String studentName;
   // private String studentSurname;
   // private String departmentId;
    private String course; //program

    private Vector<String> classes = new Vector<>();
    HashMap<String,String> assignedExams = new HashMap<>();

    public Student(String studentId) {
        this.studentId = studentId;
    }

    public Student(String studentId, String course, String examName, Vector<String> classes) {
        super();
        this.studentId = studentId;
        this.course = course;
        this.classes = classes;
    }

   // List<Student> list = new ArrayList<Student>();

    public void addToClasses(String newClass) {

        if(!classExist(newClass)) {
            this.classes.add(newClass);
        }
        else {

        }
    }

    public boolean classExist(String name) {
        return this.classes.stream().filter(o -> o.equals(name)).findFirst().isPresent();
    }

    /*Getter & Setter*/
    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public Vector<String> getClasses() {
        return classes;
    }

    public void setClasses(Vector<String> classes) {
        this.classes = classes;
    }

    public HashMap<String, String> getAssignedExams() {
        return this.assignedExams;
    }

    public void setAssignedExams(HashMap<String, String> assignedExams) {
        this.assignedExams = assignedExams;
    }

}
