package model;

public class Exam {

    String examName;
    int studentCount;
    double ga[] = new double[50];
    String examCode;


    public Exam(String examName, int studentCount, String examCode) {
        super();
        this.examName = examName;
        this.studentCount = studentCount;
        this.examCode = examName.substring(studentCount, 2);

        for (int i=0; i<=50; i++) {
            this.ga[i] = 0;
        }
    }

    public Exam(String c) {
    }

    public String getExamName() {
        return examName;
    }

    public void setExamName(String examName) {
        this.examName = examName;
    }

    public int getStudentCount() {
        return studentCount;
    }

    public void setStudentCount(int studentCount) {
        this.studentCount = studentCount;
    }

    public String getExamCode() {
        return examCode;
    }

    public void setExamCode(String examCode) {
        this.examCode = examCode;
    }
}