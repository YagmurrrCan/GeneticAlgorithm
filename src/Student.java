import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

//constructor
public class Student {
    private String studentId;
    private String studentName;
    private String studentSurname;
    private String departmentId;
    private String courseId;
    private String examName;
    int capacity;
   // private Vector<String> classes() = new Vector<>();
    HashMap<String,String> assignedExams = new HashMap<>();


    Student(String studentId, String studentName, String studentSurname, String departmentId, String courseId, String examName, int capacity) {
        this.studentId = studentId;
        this.studentName = studentName;
        this.studentSurname = studentSurname;
        this.departmentId = departmentId;
        this.courseId = courseId;
        this.examName = examName;
        this.capacity = capacity;
    }

    public Student(String studentId) {
        this.studentId = studentId;
    }

    List<Student> list = new ArrayList<Student>();

    public void addToClasses(String stringCellValue) {
        System.out.println(stringCellValue);
    }

  //  public HashMap<String,String> assignedExams = new HashMap<>();
    
    /*Getter & Setter*/

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getStudentSurname() {
        return studentSurname;
    }

    public void setStudentSurname(String studentSurname) {
        this.studentSurname = studentSurname;
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getExamName() {
        return examName;
    }

    public void setExamName(String examName) {
        this.examName = examName;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public HashMap<String, String> getAssignedExams() {
        return this.assignedExams;
    }

    public void setAssignedExams(HashMap<String, String> assignedExams) {
        this.assignedExams = assignedExams;
    }

    public List<Student> getList() {
        return list;
    }

    public void setList(List<Student> list) {
        this.list = list;
    }
}
