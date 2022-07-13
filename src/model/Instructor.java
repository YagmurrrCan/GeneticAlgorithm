package model;

public class Instructor {

    String instructorId;  // excelde->kod
    String faculty;
    String program;
    String instructorAvaibility;

    @Override
    public String toString() {
        return "Instructor{" +
                "instructorId='" + instructorId + '\'' +
                '}';
    }

    public Instructor(String instructorId) {
        super();
        this.instructorId=instructorId;
    }

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