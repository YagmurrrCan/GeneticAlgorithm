/*
Bu sınıf, bir "Instructor" sınıfı oluşturuyor gibi görünüyor. Sınıfın içinde öğretim elemanının numarası (instructorId), öğretim elemanının hangi fakültede çalıştığı (faculty), öğretim elemanının hangi programda ders verdiği (program) ve öğretim elemanının mevcut olup olmadığı (instructorAvailability) gibi bilgileri saklamak için birkaç özellik (field) tanımlanmış.
 */

package model;

public class Instructor {

    String instructorId;  // excelde->kod satırı
    String faculty;
    String program;
    // The availability of the instructor
    String instructorAvaibility;

    public Instructor(String instructorId) {
        super();
        this.instructorId=instructorId;
    }

    // Constructs an instructor with the given availability
    public Instructor(String instructorId, String faculty, String program, String instructorAvaibility) {
        super();
        this.instructorId = instructorId;
        this.faculty = faculty;
        this.program = program;
        this.instructorAvaibility = instructorAvaibility;
    }

    public String getInstructorId() {
        return instructorId;
    }

    public void setInstructorId(String instructorId) {
        this.instructorId = instructorId;
    }

    public String getFaculty() {
        return faculty;
    }

    public void setFaculty(String faculty) {
        this.faculty = faculty;
    }

    public String getProgram() {
        return program;
    }

    public void setProgram(String program) {
        this.program = program;
    }

    public String getInstructorAvaibility() {
        return instructorAvaibility;
    }

    public void setInstructorAvaibility(String instructorAvaibility) {
        this.instructorAvaibility = instructorAvaibility;
    }


}