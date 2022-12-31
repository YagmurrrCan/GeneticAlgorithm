package model;

public class Exam {

    String examName;
    int studentCount;
    double ga[] = new double[50];
    String examCode;

    public Exam(String examName) {
        super();
        this.examName = examName;
        this.examCode = examName.substring(studentCount, 2);

        for(int i=0; i<50; i++) {
            this.ga[i] = 0;
        }
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

    public double[] getGa() {
        return ga;
    }

    public void setGa(double[] ga) {
        this.ga = ga;
    }

    public String getExamCode() {
        return examCode;
    }

    public void setExamCode(String examCode) {
        this.examCode = examCode;
    }

}