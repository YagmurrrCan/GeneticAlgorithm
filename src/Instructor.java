public class Instructor {

    String instructorId;  // excelde->kod
    //String studentName;
    //String studentSurname;
    String faculty;
    String program;
    String instructorAvaibility;

    public Instructor(String instructorId) {
        super();
        this.instructorId=instructorId;
    }

    public Instructor(String id, String faculty, String program, String instructorAvaibility) {
        super();
        this.instructorId = instructorId;
        this.faculty = faculty;
        this.program = program;
        this.instructorAvaibility = instructorAvaibility;
    }

    public String getId() {
        return instructorId;
    }
    public void setId(String id) {
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
    public String getDegreeOfInstructor() {
        return instructorAvaibility;
    }
    public void setDegreeOfInstructor(String instructorAvaibility) {
        this.instructorAvaibility = instructorAvaibility;
    }



}