//Import library & ApachePoi
import model.*;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.w3c.dom.ls.LSOutput;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

public class Main {

    //add-remove operation, excel data
    static List<Student> students = new Vector<Student>();
    static List<Instructor> instructor = new Vector<Instructor>();
    static List<Room> rooms = new Vector<Room>();
    static List<Exam> exams = new Vector<>();

    static List<Solution> solutions = new Vector<>();
    static List<SlotCounter> examTimeslotCounters = new Vector<>();

    static HashMap<String, String> studentSizeForExams = new HashMap<>();
    static HashMap<String, String> roomSolutions = new HashMap<>();

    static List<AssignedRoom> assignedRooms = new Vector<>();
    static List<AssignedInstructor> assignedInstructors = new Vector<>();

    //GENETIC-ALGORITHM
    static List<GeneticAlgorithm> ga = new ArrayList<>(); //Vector
    static int[][] conflictMatrix;

    final static int weight_order1 = 16; // student max exam sc
    final static int weight_order2 = 8; // student overload sc
    final static int weight_order3 = 4; // time period sc
    final static int weight_order4 = 2; //
    final static int weight_order5 = 1;

    static Random r;
    static int seed = 0;
    static int iterationCounter = 0;
    static int maxIteration = 0;

    public static boolean studentExist(String id) {
        return students.stream().anyMatch(o -> o.getStudentId().equals(id)); //filter(o -> o.getStudentId().equals(id)).findFirst().isPresent();
    }

    public static boolean instructorExist(String id) {
        return instructor.stream().filter(o -> o.getInstructorId().equals(id)).findFirst().isPresent();
    }

    public static boolean roomExist(String id) {
        return rooms.stream().filter(o -> o.getRoomId().equals(id)).findFirst().isPresent();
    }

    public static boolean courseExist(String examName) {
        return exams.stream().filter(o -> o.getExamName().equals(examName)).findFirst().isPresent();
    }

    public static int findExamByName(String examName) {
        return exams.indexOf(exams.stream().filter(o -> o.getExamName().equals(examName)).findFirst().get());
    }

    public static int findSolutionByName(String examName) {
        return solutions.indexOf(solutions.stream().filter(o -> o.getExamName().equals(examName)).findFirst().orElse(null)); //get());
    }

    public static int findStudentIdByNo(String studentno) {
        return students.indexOf(students.stream().filter(o -> o.getStudentId().equals(studentno)).findFirst().orElse(null)); //get());
    }

    // ConflictMatrix, sınavlar arasındaki çakışmaları belirtir.
    // sınav 1 ile sınav 2 arasında bir çakışma varsa, matrisin [1][2] pozisyonu bir artırılır. Sınav 1 ile sınav 3 arasında da bir çakışma varsa, matrisin [1][3] pozisyonu da bir artırılır.
    public static void createConflictMatrix() {

        conflictMatrix = new int[exams.size()][exams.size()];

        int studentIterator = 0;
        while (studentIterator < students.size()) {

            Student currentStudent = students.get(studentIterator);
            Vector<String> currentExams = currentStudent.getExams();

            for (int i = 0; i < currentExams.size(); i++) {
                /*
                 * for class x of student y
                 * check every other classes of the student y
                 * and increases the old value which is initially 0
                 */

                String currentExam = currentExams.get(i);
                int idOfCurrentExam = findExamByName(currentExam);

                for (int j = i + 1; j < currentExams.size(); j++) {
                    String nextExam = currentExams.get(j);
                    int idOfNextExam = findExamByName(nextExam);

                    int oldConflict = conflictMatrix[idOfCurrentExam][idOfNextExam];
                    int oldConflictReverse = conflictMatrix[idOfNextExam][idOfCurrentExam];
                    conflictMatrix[idOfCurrentExam][idOfNextExam] = oldConflict + 1;
                    conflictMatrix[idOfNextExam][idOfCurrentExam] = oldConflictReverse + 1;
                }
            }
            studentIterator++;
        }
    }

    public static void softConstraintStudentOverload(HashMap<String, String> solution, double[][] populationFitness) {
        double softConstraintFactor = 0.2; // Yumuşak kısıt faktörünün değeri
        double baseValue = 0.5; // Temel değer, sınavların fitness değerlerinin en düşük olmasını sağlar

        HashMap<String, Double> counterTable = new HashMap<>(); // Öğrencilerin sınav sayılarını tutacak tablodaki anahtar: sınav adı, değer: sınav sayısı
        // Öğrenciler dizisini dolaşır ve her öğrencinin sınavlarını tutan bir Vektör nesnesine ulaşır
        for (Student s : students) {
            Vector<String> currentStudentExams = s.getExams();
            // Her öğrencinin sınavlarını dolaşır ve her sınav için bir sonraki sınavı arar
            for (int i = 0; i < currentStudentExams.size(); i++) {
                String currentExam = currentStudentExams.get(i);
                int currentExamId = findExamByName(currentExam);
                // Eğer iki sınav arasındaki zaman aralığı 1 ise, bu
            }
        }
    }

    public static void hardConstraintPartialSolution(HashMap<String, String> firstSolution) {

        String currentExam = null;
        String nextExam = null;
        String currentExamTimeslot = null;
        String nextExamTimeslot = null;

        for(int i=0 i< exams.size(); i++) { // i also denotes current exam id
            currentExam = exams.get(i).getExamName();
            currentExamTimeslot = firstSolution.get(currentExam);
            int cet = Integer.parseInt(currentExamTimeslot);

            for(int m=i+1; m < exams.size(); m++) { // m denotes next exam id
                nextExam = exams.get(m).getExamName();
                nextExamTimeslot = firstSolution.get(nextExam);
                int net = Integer.parseInt(nextExamTimeslot);

                if(conflictMatrix[i][m]>0 && currentExamTimeslot==nextExamTimeslot) {
                    exams.get(i).heuristics[cet] = Double.MAX_VALUE;
                }else {
                }
            }
        }
        editHeuristics();
    }

    public static boolean checkHardConstraints(HashMap<String, String> examTimetable) {
        // Sınavlar aynı anda yapılmamalıdır.
        for (String exam1 : examTimetable.keySet()) {
            String timeslot1 = examTimetable.get(exam1);
            for (String exam2 : examTimetable.keySet()) {
                String timeslot2 = examTimetable.get(exam2);
                if (exam1.equals(exam2)) {
                    continue;
                }
                if (timeslot1.equals(timeslot2)) {
                    return false;
                }
            }
        }
        // Diğer hard constraintlerinizi buraya ekleyebilirsiniz.
        return true;
    }

    public static boolean violatesHardConstraint( ArrayList<Integer> possibleTimeslotsTable, HashMap<String, String> partialSolution, Exam e, int nextTimeslot) {

        if(nextTimeslot==-1) {
            return true;
        }else {

            String currentExam = null;
            String nextExam = null;

            for(Student currentStudent:students) {
                Vector<String> currentStudentClasses = currentStudent.getExams();

                for(int i=0;i<currentStudentClasses.size();i++) {
                    currentExam = currentStudentClasses.get(i);
                    nextExam = e.getExamName();

                    int currentExamId = findExamByName(currentExam);
                    int nextExamId = findExamByName(e.getExamName());

                    String currentTimeslot = partialSolution.get(currentExam);

                    if(currentTimeslot!=null) {
                        if(conflictMatrix[currentExamId][nextExamId]>0) {
                            // if they are two conflicting exams:
                            if(currentTimeslot == String.valueOf(nextTimeslot)) {
                                removeFromTimeslotTable(possibleTimeslotsTable, nextTimeslot);
                                return true;
                            }
                        }else {
                            // if not conflicting no reason to not use the timeslot
                            return false;
                        }
                    }else {
                        // the exam still not assigned not possible to cause a conflict
                        return false;
                    }
                }
            }
        }
        return false;
    }

    public static void writeTable() throws IOException {

        XSSFWorkbook workbook = new XSSFWorkbook();

        XSSFSheet spreadsheet = workbook.createSheet(" Student Data ");

        XSSFRow row;

        // This data needs to be written (Object[])
        Map<String, Object[]> studentData = new TreeMap<String, Object[]>();

        studentData.put(
                "1",
                new Object[] { "Roll No", "NAME", "Year" });

        studentData.put("2", new Object[] { "128", "Aditya",
                "2nd year" });

        studentData.put(
                "3",
                new Object[] { "129", "Narayana", "2nd year" });

        studentData.put("4", new Object[] { "130", "Mohan",
                "2nd year" });

        studentData.put("5", new Object[] { "131", "Radha",
                "2nd year" });

        studentData.put("6", new Object[] { "132", "Gopal",
                "2nd year" });

        Set<String> keyid = studentData.keySet();

        int rowid = 0;

        for (String key : keyid) {

            row = spreadsheet.createRow(rowid++);
            Object[] objectArr = studentData.get(key);
            int cellid = 0;

            for (Object obj : objectArr) {
                Cell cell = row.createCell(cellid++);
                cell.setCellValue((String)obj);
            }
        }

        FileOutputStream out = new FileOutputStream(new File("C:/savedexcel/GFGsheet.xlsx"));

        workbook.write(out);
        out.close();
    }

    /* Bu metod, bir sınav zamanlama senaryosu için çakışma matrisini oluşturur. Bu metod, bir students dizisi içindeki öğrencilerin sınavlarını kontrol ederek, sınavlar arasındaki çakışmaları belirtir. Örneğin, sınav 1 ve sınav 2 arasında bir çakışma varsa, matrisin bu iki sınavı temsil eden hücrelerinde bir "1" değeri bulunur.

 Bu metod, ayrıca findExamByName() adlı bir yardımcı metod kullanarak, bir sınavın adını kullanarak sınavın ID'sini bulur. Bu ID, daha sonra çakışma matrisini oluşturmak için kullanılır.

 Bu metod, bir sınav zamanlama senaryosu için bir "çözümün geçerliliğini" değerlendirir. Bu çözüm, bir "parçalı çözüm" olarak adlandırılır ve bir sınavın belirli bir zaman aralığına atandığı bir "çözüm parçası" içerir. Bu metod, bu çözüm parçasının bir sınavın belirli bir zaman aralığına atandığı diğer çözüm parçalarıyla oluşan tam bir sınav zamanlama çözümünün geçerliliğini değerlendirir.
     partialSolution: Bir sınavın belirli bir zaman aralığına atandığı çözüm parçalarını tutan bir hash tablosu.
             nextExam: Değerlendirilecek olan sonraki sınavın adı.
             nextTimeslot: Sonraki sınavın atanacağı zaman aralığı.
 */
    //static HashMap<Integer, Boolean> chosenExamsTable = new HashMap<>();
    //static HashMap<Integer, Integer> chosenFrequencyTable = new HashMap<Integer, Integer>();
    List<int[]> eligibleSolutions = new ArrayList<int[]>();

    static ArrayList<Integer> possibleTimeslotsTable = new ArrayList<>();
    public static void resetTimeslotTable(ArrayList<Integer> timeslotTable) {
        possibleTimeslotsTable.clear();
        for(int i=0; i<50; i++) {
            possibleTimeslotsTable.add(i);
        }
    }

    //ALOOOO
    public static void resetExamTimeslotCounters() {
        for(int i=0; i < examTimeslotCounters.size(); i++) { // iterate over etc
            for(int m=0; m < 50; m++) { // iterate over the etc's count array
                examTimeslotCounters.get(i).count[m] = 0;
            }
        }
    }
    public static void initializeExamTimeslotCounters() {

        for(int i=0; i<exams.size(); i++) {
            examTimeslotCounters.add(new SlotCounter(i));
        }
    }

    static double evaluatePartialSolution(HashMap<String, String> partialSolution, String nextExam, String nextTimeslot) throws InterruptedException {

        double cost = 0;
        cost = firstSoftC(partialSolution, nextExam, nextTimeslot);
        cost = secondSoftC(cost, partialSolution, nextExam, nextTimeslot);

        return cost;
    }

 /*   Bu metod, bir sınav zamanlama senaryosu için en uygun zaman aralığını bulur. Bu metod, bir "parçalı çözüm" olarak adlandırılan bir sınavın belirli bir zaman aralığına atandığı çözüm parçaları ve bir sonraki sınavın adını kullanarak çalışır. Bu metod, değerlendirilen sınavın zaman aralıklarına atanma olasılıklarını hesaplar ve en yüksek olasılığa sahip zaman aralığını döndürür.   */
    static int returnHighesTimeslot(HashMap<String, String> partialSolution, String nextExam) throws InterruptedException {
        int highestTimeslot = -1;
        // En yüksek olasılığı saklayan değişken
        double maxProbability = Double.MAX_VALUE;

        // Tüm zaman aralıklarını dolaş
        for (int i = 0; i < timeslots.length; i++) {
            /*// Sınavın zaman aralığına atanma olasılığını hesapla
    double probability = evaluatePartialSolution(partialSolution, nextExam, String.valueOf(i));

             // Olasılık en yüksekse, zaman aralığını güncelle
    if (probability > maxProbability) {
      highestTimeslot = i;
      maxProbability = probability;
    }
  }

  // En yüksek zaman aralığını döndür
  return highestTimeslot;
}
  */
            if ((evaluatePartialSolution(partialSolution, nextExam, String.valueOf(i))) > maxProbability) {
                highestTimeslot = i;
            }
        }

        return highestTimeslot;
    }

    /*Bu metod, bir sınav zamanlama senaryosu için "yumuşak kısıtları" değerlendirir.
    Yumuşak kısıtlar, bir sınav zamanlama çözümünün geçerliliğini etkileyen faktörlerdir ve bu faktörlerin ağırlıkları düşük olabilir.
    Örneğin, bir öğrencinin aynı anda birden fazla sınavı olması bir yumuşak kısıttır ve bu kısıtın ağırlığı düşük olabilir.

    Bu metod, aşağıdaki parametreleri kullanarak çalışır:

    cost: Değerlendirilen çözümün maliyeti.
    ga: Bir sınavın belirli bir zaman aralığına atandığı çözüm parçalarını tutan bir hash tablosu.
    nextExam: Değerlendirilecek olan sonraki sınavın adı.
    nextTimeslot: Sonraki sınavın atanacağı zaman aralığı.

     */
    static double secondSoftC(double cost, HashMap<String, String> ga, String nextExam, String nextTimeslot) throws InterruptedException {

        // Weight of soft constraints
        /* Bu değer, yumuşak kısıtların ağırlığını temsil eder.
        Yumuşak kısıtlar, bir sınav zamanlama çözümünün geçerliliğini etkileyen faktörlerdir ve bu faktörlerin ağırlıkları düşük olabilir.
        Örneğin, bir öğrencinin aynı anda birden fazla sınavı olması bir yumuşak kısıttır ve bu kısıtın ağırlığı düşük olabilir.
        Bu yumuşak kısıtların ağırlığı, uygulamanın ihtiyaçlarına göre değiştirilebilir.
        Örneğin, eğer yumuşak kısıtların önemi daha yüksekse, bu değer daha yüksek bir değere ayarlanabilir.
        Ancak, unutmayın ki bu değerin düşük olması, yumuşak kısıtların önemini azaltmaz, sadece bu kısıtların çözüme etkisinin daha düşük olacağı anlamına gelir.
         */
        double softConstraintFactor = 0.3;

        // Student exam counter
        int studentExamCounter = 0;

        // Store the cost of the current solution
        double midCost = cost;

        // 2) having more than 2 exams increases cost
        //Öğrencileri döngü ile gez
        for (Student s : students) {

            // Mevcut öğrencinin sınavlarını tut
            //This code stores the exams of the current student
            Vector<String> currentStudentExams = s.getExams();

            for (int i = 0; i < currentStudentExams.size(); i++) {

                studentExamCounter = 0;
                String currentExam = currentStudentExams.get(i);

                // Öğrencinin mevcut sınavının zaman aralığını tut
                int currentExamTimeslot = Integer.parseInt(ga.get(currentExam));
                int nextExamTimeslot = Integer.parseInt(nextTimeslot);

                if (timeslots[currentExamTimeslot][2] == timeslots[nextExamTimeslot][2]) {
                    // means they are assigned to the same week
                    if (timeslots[currentExamTimeslot][1] == timeslots[nextExamTimeslot][1]) {
                        // means they are assigned to the same day
                        // Öğrenci, mevcut sınavının atandığı zaman aralığında başka bir sınavı da alıyorsa sayacı artır
                        studentExamCounter++;

                        // Eğer sayaç 2'den büyükse, mevcut çözümün maliyetini artır
                        if (studentExamCounter > 2) {
                            midCost = midCost + midCost * softConstraintFactor;
                        }

                    }
                }
            }


        }
        return midCost;
    }

    static double firstSoftC(HashMap<String, String> ga, String nextExam, String nextTimeslot) throws InterruptedException {
        double midCost = 0;
        double softConstraintFactor = 0.2;

        for (Student s : students) {
            Vector<String> currentStudentExams = s.getExams();

            for (int i = 0; i < currentStudentExams.size(); i++) {

                String currentExam = currentStudentExams.get(i);
                // Öğrencinin mevcut sınavının zaman aralığını tut
                int currentExamTimeslot = Integer.parseInt(ga.get(currentExam));
                int currentExamId = findExamByName(currentExam);
                int nextExamId = findExamByName(currentExam);

                int nextExamTimeslot = Integer.parseInt(nextTimeslot);


                if (Math.abs(currentExamTimeslot - nextExamTimeslot) == 1) {

                    midCost = midCost + midCost * softConstraintFactor;

                } else if (Math.abs(currentExamTimeslot - nextExamTimeslot) == 0) {
                    // since its looking for a single student
                    // meaning the classes cant have exams simultaneously

                    System.out.println("Hard constraint fault!!");
                    //Thread.sleep(5000);

                } else {


                }
            }
        }
        return midCost;
    }

    /*
    Bu kod, geçmiş sınav ve mevcut sınav arasında bir kısıt olup olmadığını kontrol eden bir yöntemdir. Kısıt olup olmadığını, öğrencilerin sınavlarını tutan conflictMatrix adlı bir matris kullanarak belirler. Eğer iki sınav arasında bir kısıt varsa, yani conflictMatrix matrisinde iki sınavın indeksleri arasında bir değer varsa, bu kod true değerini döndürür. Aksi takdirde, false değerini döndürür. Bu kod, sınav oturumlarının planlanması sırasında kısıtları kontrol etmek için kullanılabilir.
     */
    public static boolean isBreakHardConstraint(int pastExam, int currentExam) {
        if(conflictMatrix[pastExam][currentExam]>0||conflictMatrix[currentExam][pastExam]>0) {
            return true;
        }
        return false;
    }

   /*
    Bu metod, bir öğrencinin aynı gün içinde almış olduğu sınav sayısını kontrol eder.
    Eğer bir öğrencinin aynı gün içinde 3'ten fazla sınavı varsa, maliyeti artırır.
    */

    public static void partialSoftConstraintStudentMaxExam(HashMap<String, String> ga) throws InterruptedException {
        double softConstraintFactor = 0.3;
        int studentExamCounter = 0;


        // 2) having more than 2 exams increases cost

        for (Student s : students) {
            //studentExamCounter=0;
            Vector<String> currentStudentClasses = s.getExams();
            for (int i = 0; i < currentStudentClasses.size(); i++) {
                studentExamCounter = 0;
                String currentClass = currentStudentClasses.get(i);
                int currentClassId = findExamByName(currentClass);

                for (int k = i + 1; k < currentStudentClasses.size(); k++) {

                    if (i == k) {
                        // do nothing
                    } else {
                        String nextClass = currentStudentClasses.get(k);
                        int nextClassId = findExamByName(nextClass);

                        int currentClassTimeslot = Integer.parseInt(ga.get(currentClass));
                        int nextClassTimeslot = Integer.parseInt(ga.get(nextClass));

                        Exam e1 = exams.get(currentClassId);
                        Exam e2 = exams.get(nextClassId);


                        if (timeslots[currentClassTimeslot][2] == timeslots[nextClassTimeslot][2]) {
                            // means they are assigned to the same week
                            if (timeslots[currentClassTimeslot][1] == timeslots[nextClassTimeslot][1]) {
                                // means they are assigned to the same day
                                studentExamCounter++;
                                if (studentExamCounter > 2) {

                            /*
                                    ga.setFitness(ga.getFitness() + ga.getFitness() * softConstraintFactor);
                             */
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public static void softConstraintStudentMaxExam(HashMap<String, String> solution) throws InterruptedException {
        double softConstraintFactor = 0.3;
        int studentExamCounter = 0;

        // 2) having more than 2 exams increases cost

        for(Student s:students) {
            //studentExamCounter=0;
            Vector<String> currentStudentClasses = s.getExams();
            for(int i=0;i<currentStudentClasses.size();i++) {
                studentExamCounter=0;
                String currentClass = currentStudentClasses.get(i);
                int currentClassId = findExamByName(currentClass);

                for(int k=i+1;k<currentStudentClasses.size();k++) {

                    if(i==k) {
                        // do nothing
                    }else {
                        String nextClass = currentStudentClasses.get(k);
                        int nextClassId = findExamByName(nextClass);

                        int currentClassTimeslot =  Integer.parseInt(solution.get(currentClass));
                        int nextClassTimeslot = Integer.parseInt(solution.get(nextClass));

                        Exam e1 = exams.get(currentClassId);
                        Exam e2 = exams.get(nextClassId);




                        //double cost = e1.pheromones[currentClassTimeslot];

                        if(timeslots[currentClassTimeslot][2] == timeslots[nextClassTimeslot][2]) {
                            // means they are assigned to the same week
                            if(timeslots[currentClassTimeslot][1] == timeslots[nextClassTimeslot][1]) {
                                // means they are assigned to the same day


                                studentExamCounter+=1;

                                if(studentExamCounter>2) {

                                    e1.heuristics[currentClassTimeslot] = e1.heuristics[currentClassTimeslot] + e1.heuristics[currentClassTimeslot]*softConstraintFactor;
                                    e2.heuristics[nextClassTimeslot] = e2.heuristics[nextClassTimeslot] + e2.heuristics[nextClassTimeslot]*softConstraintFactor;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public static void partialSoftConstraintStudentOverloadCheck(HashMap<String, String> solution) throws InterruptedException {

        double softConstraintFactor = 0.2;
        double baseValue = 0.5;

        // 1) having exams back to back increases cost

        HashMap<String, Double> counterTable = new HashMap<>();
        for(int i=0;i<exams.size();i++) {
            counterTable.put(exams.get(i).getExamName(), (double) 0);
        }


        for(Student s:students) {
            Vector<String> currentStudentExams = s.getExams();
            for(int i=0;i<currentStudentExams.size();i++) {

                String currentExam = currentStudentExams.get(i);
                int currentExamId = findExamByName(currentExam);


                for(int k=i+1;k<currentStudentExams.size();k++) {

                    String nextExam = currentStudentExams.get(k);
                    int nextExamId = findExamByName(nextExam);

                    int cet =  Integer.parseInt(solution.get(currentExam)); // gives which timeslot it was assigned
                    int net = Integer.parseInt(solution.get(nextExam));

                    //double cost = e1.pheromones[currentClassTimeslot];

                    if(Math.abs(cet-net)==1) {
                        //cost = heuristic

                        double oldval = counterTable.get(currentExam);
                        counterTable.put(currentExam, oldval+1);

                        double nextoldval = counterTable.get(nextExam);
                        counterTable.put(nextExam, nextoldval+1);
                    }
                }
            }
        }

        for(String key : counterTable.keySet()) {
            Exam currentExam = exams.get(findExamByName(key));
            double oldscore = currentExam.heuristics[Integer.parseInt(solution.get(key))];
            if(oldscore==0) {
                oldscore = baseValue; // keeping it as min as possible to not control the cost manually

            }

            double editedFactor = softConstraintFactor + 0.1*(counterTable.get(key)-2);
            currentExam.heuristics[Integer.parseInt(solution.get(key))] = oldscore + (counterTable.get(key))*editedFactor;
        }

        editHeuristics();
    }

    public static void softConstraintStudentOverloadCheck(HashMap<String, String> solution) throws InterruptedException {

        double softConstraintFactor = 0.2;

        // 1) having exams back to back increases cost

        for(Student s:students) {
            Vector<String> currentStudentExams = s.getExams();
            for(int i=0;i<currentStudentExams.size();i++) {

                String currentExam = currentStudentExams.get(i);
                int currentExamId = findExamByName(currentExam);


                for(int k=i+1;k<currentStudentExams.size();k++) {

                    String nextExam = currentStudentExams.get(k);
                    int nextExamId = findExamByName(nextExam);

                    int currentExamTimeslot =  Integer.parseInt(solution.get(currentExam)); // gives which timeslot it was assigned
                    int nextExamTimeslot = Integer.parseInt(solution.get(nextExam));

                    Exam e1 = exams.get(currentExamId);
                    Exam e2 = exams.get(nextExamId);

                    if(Math.abs(currentExamTimeslot-nextExamTimeslot)==1) {

                        e1.heuristics[currentExamTimeslot] = e1.heuristics[currentExamTimeslot] + e1.heuristics[currentExamTimeslot] * softConstraintFactor;
                        e2.heuristics[nextExamTimeslot] = e2.heuristics[nextExamTimeslot] + e2.heuristics[nextExamTimeslot] * softConstraintFactor;

                    }else if(Math.abs(currentExamTimeslot-nextExamTimeslot)==0) {

                        System.out.println("Hard constraint fault!!");

                    }else {

                    }
                }
            }
        }
    }

    public static double softConstraintExamWeekRelation(double cost, HashMap<String, String> ga) {
        // Soft constraint faktörü maliyeti kaç kat artıracağını belirler.
        double softConstraintFactor = 0.1;

        // En fazla öğrenci sayısına sahip sınavı bulun.
        int maxExam = 0;
        int index = -1;

        for (int x = 0; x < examStudentSize.length; x++) {
            if (maxExam < examStudentSize[x]) {
                maxExam = examStudentSize[x];
                index = x;
            }
        }

        // En fazla öğrenci sayısına sahip sınavın zaman dilimini alın.
        String timeslot = ga.get(exams.get(index).getExamName());
        // Sınavın 2. hafta içinde yapılmasını tercih ediyorsak, maliyeti artırın.
        if (Integer.parseInt(timeslots[Integer.parseInt(timeslot)][2]) == 2) {
            cost = cost + cost * softConstraintFactor;
        }

        return cost;
    }

    public static int getRandomExam(Ant currentAnt) {

        r = new Random();
        int randomSeed = r.nextInt();
        r.setSeed(randomSeed);

        int randomExamIndex = r.nextInt(0, currentAnt.notVisited.size());
        int chosenExamIndex = currentAnt.notVisited.get(randomExamIndex);
        currentAnt.removeExam(randomExamIndex);
        return chosenExamIndex;
    }

    public static int returnRandomExamIndex(Ant currentAnt) {
        r = new Random();
        r.setSeed(seed);

        int randomExamIndex = r.nextInt(0, currentAnt.notVisited.size());
        int chosenExamIndex = currentAnt.notVisited.get(randomExamIndex);
        currentAnt.removeExam(randomExamIndex);
        return chosenExamIndex;
    }

    public static int computeTransitionProbability(HashMap<String, String> partialSolution, Exam e) {

        int timeslot = -1;
        resetTimeslotTable(possibleTimeslotsTable);


        while(violatesHardConstraint(possibleTimeslotsTable, partialSolution, e, timeslot) && possibleTimeslotsTable.size()>0) {
            //System.out.println(">> violateshc loop");
            double pheromoneImportance = 1;
            double heuristicPenalty= 15;

            double maxValue = -Double.MAX_VALUE;
            double prob = 0;

            for(int i=0;i<possibleTimeslotsTable.size();i++) {

                int examTimeslot = possibleTimeslotsTable.get(i);
                // corresponds to the inverse of the cost value at the end of the day
                prob = e.getPheromones()[examTimeslot]*pheromoneImportance - e.getHeuristics()[examTimeslot]*heuristicPenalty;

                if(prob>maxValue) {
                    // with the max pheromone & least cost which is lowest heuristic
                    maxValue = prob;
                    timeslot = i;
                    // goes to violateshc from here
                }else {
                }

            }
        }

        return timeslot;
    }

    public static void readStudentsFile() {

        try {
            XSSFWorkbook wb = new XSSFWorkbook(new File("C:\\Users\\Lenovo\\Desktop\\Tez\\students.xlsx"));
            XSSFSheet sheet = wb.getSheetAt(0);

            //FormulaEvaluator formulaEvaluator = wb.getCreationHelper().createFormulaEvaluator();
            //System.out.println("row: " + sheet.getActiveCell().getRow());
            //System.out.println("row: " + sheet.getActiveCell().getColumn());

            DataFormatter fmt = new DataFormatter();

            Vector<String> vector1 = new Vector<>();
            int counterrow = 1;
            int index = 0;
            int countercell = 1;
            for (Row row : sheet) {

                if (counterrow != 1) {
                    for (Cell cell : row) {
                        String cellAddress = cell.getAddress().toString();
                        //System.out.print(cellAddress + "\t");
                        if (cellAddress.startsWith("A")) {
                            String val = fmt.formatCellValue(cell);
                            if (!studentExist(val)) {
                                students.add(new Student(val));
                            }
                        } else if (cellAddress.startsWith("B")) {
                            //System.out.println("(kayıtli_oldugu_program_kodu)");
                        } else if (cellAddress.startsWith("C")) {
                            //System.out.println("(sinif)");
                        } else if (cellAddress.startsWith("D")) {
                            //System.out.println("students.size: " + students.size());
                            //System.out.println("(dersi_aldigi_program_kodu)");

                            Cell tempcell = cell.getRow().getCell(0); // row index should be the same as student index since they are stored in the same order!
                            String studentnotemp = fmt.formatCellValue(tempcell);

                            int studentindex = findStudentIdByNo(studentnotemp);

                            students.get(studentindex).addToExams(cell.getStringCellValue());
                            //students.get(index);

                        } else if (cellAddress.startsWith("E")) {
                            //System.out.println("(grup)");

                        } else if (cellAddress.startsWith("F")) {
                            //System.out.println("(ders programi)");

                        } else if (cellAddress.startsWith("G")) {
                            //System.out.println("(fakulte)");

                        } else {
                            //System.out.println("no way!");
                        }
                        countercell++;
                    }
                }
                counterrow++;

            }


        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException | InvalidFormatException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public static void readInstructorFile() {

        try {
            XSSFWorkbook wb = new XSSFWorkbook(new File("C:\\Users\\Lenovo\\Desktop\\Tez\\instructor.xlsx"));
            XSSFSheet sheet = wb.getSheetAt(0);

            //FormulaEvaluator formulaEvaluator = wb.getCreationHelper().createFormulaEvaluator();

            DataFormatter fmt = new DataFormatter();

            Vector<String> vector2 = new Vector<>();
            int counterrow = 1;
            int index = 0;
            int countercell = 1;
            for (Row row : sheet) {

                Instructor instruct = null;

                if (counterrow != 1) {
                    for (Cell cell : row) {
                        String cellAddress = cell.getAddress().toString();

                        if (cellAddress.startsWith("A")) {
                            //System.out.println("(id of supervisor)");
                            String id = fmt.formatCellValue(cell);
                            if (!instructorExist(id)) {
                                instruct = new Instructor(id);
                                instructor.add(instruct);
                            }
                        } else if (cellAddress.startsWith("B")) {
                            //System.out.println("(gorev_yeri_fakulte)");

                        } else if (cellAddress.startsWith("C")) {
                            //System.out.println("(gorev_yeri_bolum)");

                        } else if (cellAddress.startsWith("D")) {
                            //System.out.println("(unvan)");

                        } else if (cellAddress.startsWith("E")) {
                            //System.out.println("(ad_soyad)");

                        } else if (cellAddress.startsWith("F")) {
                            //System.out.println("(aktiflik)");

                        } else if (cellAddress.startsWith("G")) {
                            //System.out.println("(kadro)");

                        } else if (cellAddress.startsWith("H")) {
                            //System.out.println("(gozetmenlik_derecesi)");
                            String degree = fmt.formatCellValue(cell);
                            String yapmaz = "Yapmaz";
                            String fazla = "Fazla";

                            if (degree.equals(yapmaz)) {
                                instruct.setInstructorAvaibility("None");
                            }
                            if (degree.equals(fazla)) {
                                instruct.setInstructorAvaibility("Able");
                            }

                        } else if (cellAddress.startsWith("I")) {
                            //System.out.println("(gozetmenlik_yeri_fakulte_kod)");

                        } else if (cellAddress.startsWith("J")) {
                            //System.out.println("(gozetmenlik_yeri_program_kod)");

                        } else if (cellAddress.startsWith("K")) {
                            //System.out.println("(gozetmenlik_yeri_kampus_kod)");

                        } else if (cellAddress.startsWith("L")) {
                            //System.out.println("(gunluk_verebilecegi_max_ders)");

                        } else if (cellAddress.startsWith("M")) {
                            //System.out.println("(gozetmenlik_max_sayi)");

                        } else if (cellAddress.startsWith("N")) {
                            //System.out.println("(eposta)");

                        } else if (cellAddress.startsWith("O")) {
                            //System.out.println("(telefon)");

                        } else if (cellAddress.startsWith("P")) {
                            //System.out.println("(gorev_yeri_fakulte)");
                            String faculty = fmt.formatCellValue(cell);
                            instruct.setFaculty(faculty);

                        } else if (cellAddress.startsWith("Q")) {
                            //System.out.println("(gorev_yeri_bolum)"); //program
                            String program = fmt.formatCellValue(cell);
                            instruct.setProgram(program);

                        } else if (cellAddress.startsWith("R")) {
                            //System.out.println("(gorev_yeri_fakulte_adları)");

                        } else if (cellAddress.startsWith("S")) {
                            //System.out.println("(gorev_yeri_program_adları)");

                        } else if (cellAddress.startsWith("T")) {
                            //System.out.println("(gorev_yeri_kampus_adları)");

                        } else {
                            //System.out.println("no way!");
                        }
                        countercell++;
                    }
                }
                counterrow++;

            }


        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException | InvalidFormatException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public static void readRoomFile() {

        try {
            XSSFWorkbook wb = new XSSFWorkbook(new File("C:\\Users\\Lenovo\\Desktop\\Tez\\rooms.xlsx"));
            XSSFSheet sheet = wb.getSheetAt(0);

            //FormulaEvaluator formulaEvaluator = wb.getCreationHelper().createFormulaEvaluator();

            DataFormatter fmt = new DataFormatter();

            int counterrow = 1;
            int index = 0;
            int countercell = 1;

            // Satırları oku
            for (Row row : sheet) {

                Room room = null;

                if (counterrow != 1) {
                    for (Cell cell : row) {
                        String cellAddress = cell.getAddress().toString();
                        //System.out.print(cellAddress + "\t");
                        if (cellAddress.startsWith("A")) {
                            String id = fmt.formatCellValue(cell);
                            if (!roomExist(id)) {
                                room = new Room(id);
                                rooms.add(room);
                            }
                        } else if (cellAddress.startsWith("B")) {
                            //System.out.println("(derslik_adi)");
                            String name = fmt.formatCellValue(cell);
                            room.setRoomName(name);

                        } else if (cellAddress.startsWith("C")) {
                            //System.out.println("(bina_kodu)");
                            String building = fmt.formatCellValue(cell);
                            room.setBuilding(building);

                        } else if (cellAddress.startsWith("D")) {
                            //System.out.println("rooms.size: " + rooms.size());
                            //System.out.println("(kat)");

                        } else if (cellAddress.startsWith("E")) {
                            //System.out.println("(sıra)");

                        } else if (cellAddress.startsWith("F")) {
                            //System.out.println("(sinav_kapasitesi)");

                            String capacity = fmt.formatCellValue(cell);
                            room.setSeatingCapacity(capacity);

                        } else if (cellAddress.startsWith("G")) {
                            //System.out.println("(ders_kapasitesi)");

                        } else if (cellAddress.startsWith("H")) {
                            //System.out.println("(gozetmen_sayisi)");

                        } else if (cellAddress.startsWith("I")) {
                            //System.out.println("(tip_kodu)");

                        } else if (cellAddress.startsWith("J")) {
                            //System.out.println("(ozellikler)");

                        } else if (cellAddress.startsWith("K")) {
                            //System.out.println("(durum)");

                        } else if (cellAddress.startsWith("L")) {
                            //System.out.println("(kampus_kodu)");

                        } else if (cellAddress.startsWith("M")) {
                            //System.out.println("(kampus_Adı)");

                        } else if (cellAddress.startsWith("N")) {
                            //System.out.println("(bina_adı)");

                        } else if (cellAddress.startsWith("O")) {
                            //System.out.println("(aciklama)");

                        } else {
                            //System.out.println("no way!");
                        }
                        countercell++;
                    }
                }
                counterrow++;
            }
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException | InvalidFormatException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void readExamsFile() {
        // to find the exam with less flexibilty: meaning it has more conflicts than other exams
        int counter = 0;
        for (Student s : students) {
            List<String> tempExams = s.getExams();
            for (String c : tempExams) {
                if (!courseExist(c)) {
                    exams.add(new Exam(c));
                    counter++;
                }
            }
        }
    }

    /*
    Bu metod, bir sınavın kaç öğrencinin alacağını bulmak için kullanılır. Öğrencilerin listesi ve sınavların listesi kullanılarak, her bir sınav için kaç öğrencinin alacağı sayısı bulunur.
    Öğrencilerin listesi ve sınavların listesi, önceden okunan "öğrenci" ve "sınav" dosyalarından elde edilir. Elde edilen sayı, bir "HashMap" nesnesine "sınav adı" ve "öğrenci sayısı" olarak eklenir.
     */
    public static void findStudentSizeForExam() {

        // Öğrenci sayısını tutacak olan değişken
        int size = 0;

        // Tüm sınavları döndür
        for (Exam e : exams) {
            // Öğrenci sayacını sıfırla
            size = 0;
            // Mevcut sınavın adını al
            String examname = e.getExamName();

            // Tüm öğrencileri döndür
            for (Student currentStudent : students) {
                // Öğrencinin almış olduğu sınavları kontrol et
                if (currentStudent.getExams().contains(examname)) {
                    // Öğrencinin sınavı almış olduğuna dair sayacı artır
                    size++;
                }
            }

            // Öğrenci sayısını sınav adına göre bir tablo oluştur
            studentSizeForExams.put(examname, String.valueOf(size));
        }
    }

    /*
    Bu metod, verilen sınav zamanlama çözümünün maliyetini hesaplamak için kullanılır.
    Öncelikle, maliyet değişkeni sıfıra eşitlenir. Daha sonra, her bir sınav için aşağıdaki adımlar uygulanır:
    Sınavın atandığı zaman aralığı (timeslot) belirlenir.
    Bu sınavın atandığı zaman aralığındaki diğer sınavlarla çakışma sayısı bulunur.
    Çakışma sayısı, sınavın maliyetini artırmak için kullanılır.
    Bu işlem diğer sınavlar için de tekrarlanır ve sınavların toplam maliyeti hesaplanır.
     */

    //ALOOOOOOO
    public static double computeSolutionCost(HashMap<String, String> solution) throws InterruptedException {
        double cost = 0;

        for(int i=0; i<exams.size(); i++) {
            Exam e = exams.get(i);

            String key = e.getExamName();
            int timeslot = Integer.parseInt(solution.get(key));
            //double examPheromone = e.getPheromones()[timeslot];
            //cost += (1-examPheromone) + examHeuristic;
        }
        return cost;
    }

    /*Bu metod, mevcut odaların bir kopyasını oluşturur ve bu kopyalara "atananZamanlar" adında bir ek alan ekler. Bu alan, her bir oda için atanan zaman aralıklarını tutar. Örneğin, sınav 1'in sınav odasına atandığı zamanı bu alana kaydeder.
    Bu sayede, bir sonraki sınavı atamak için kullanılabilir odaları belirleme işlemi yapılabilir. */
    public static void createAssignmentForRooms() {
        // copy rooms with extra field called assignedTimeslots for that room

        for (int i = 0; i < rooms.size(); i++) {
            assignedRooms.add(new AssignedRoom(String.valueOf(i)));
        }
    }

    //The returnUnoccupiedRoom method is used to find an unoccupied room for a given timeslot. It loops through the list of assigned rooms and checks if the room is already occupied at the given timeslot. If it is not occupied, it returns the ID of the unoccupied room. Otherwise, it returns -1.
    public static int returnUnoccupiedRoom(String timeslot) throws InterruptedException {

        int i = 0;
        int errorCode = -1;

        for (i = 0; i < assignedRooms.size(); i++) {
            if (!assignedRooms.get(i).getOccupiedTimeslots().contains(timeslot)) {
                return i;
            }
        }

        // if no room unoccupied:
        if (i == assignedRooms.size()) {

            if (assignedRooms.get(i).getOccupiedTimeslots().contains(timeslot)) {
                System.out.println("Error assigning room to the exam!");
                Thread.sleep(3000);
                return errorCode;
            }
        }
        System.out.println("Error assigning room to the exam!");
        Thread.sleep(3000);
        return errorCode;
    }

    public static int returnUnoccupiedInstructor(String timeslot) throws InterruptedException {

        int i = 0;
        int errorCode = -1;

        for (i = 0; i < assignedInstructors.size(); i++) {

            if (!assignedInstructors.get(i).getOccupiedTimeslots().contains(timeslot)) {
                return i;
            }
        }

        // if no room unoccupied:
        if (i == assignedInstructors.size()) {


            if (assignedInstructors.get(i).getOccupiedTimeslots().contains(timeslot)) {
                System.out.println("Error assigning instructor to the exam!");
                Thread.sleep(3000);
                return errorCode;
            }

        }
        System.out.println("Error assigning instructor to the exam!");
        Thread.sleep(3000);
        return errorCode;
    }

    /*
    The purpose of the assignRoom function is to assign rooms to exams according to the given timeslots. It does this by iterating through the list of rooms and checking if the current room is available at the given timeslot. If a room is available, it is assigned to the exam and added to the list of assigned rooms. If no room is available at the given timeslot, the function returns an error code. This function also takes into account the required space for each exam, as specified in the requiredSpace variable.
     */

    public static void assignRoom(HashMap<String, String> solution) throws InterruptedException {
        // runs after assigning timeslots
        // need a list of assigned rooms till now

        // 1. room chosen iteratively
        // treat each room equal


        int requiredSpace = 1;
        int roomCounter = 0;


        // --> iterate over solutions
        // exam x --> timeslot y
        // assign a room z

        for (String exam : solution.keySet()) {


            int sizeForTheExam = Integer.parseInt(studentSizeForExams.get(exam));
            requiredSpace = sizeForTheExam;

            while (requiredSpace > 0) {

                // get a room with the counter
                // get a room thats not occupied check it with the timeslot

                String timeslotExamSolution = solution.get(exam);

                int unoccupiedRoom = returnUnoccupiedRoom(timeslotExamSolution);
                Room examRoom = rooms.get(unoccupiedRoom);

                // check if size bigger than the capacity

                int capacity = Integer.parseInt(rooms.get(roomCounter).getSeatingCapacity());

                // if size bigger than the capacity need more rooms!
                requiredSpace = sizeForTheExam - capacity;

                // Create a new AssignedRoom object with the ID of the room
                AssignedRoom ar = assignedRooms.get(Integer.parseInt(examRoom.getRoomId()));
                // Add the timeslot for the exam to the list of booked timeslots for the assigned room
                ar.getOccupiedTimeslots().add(timeslotExamSolution);

                // Add the assigned room to the list of assigned rooms
                assignedRooms.add(ar);
            }
        }
    }


    public static void createAssignmentForInstructors() {
        // copy rooms with extra field called assignedTimeslots for that room
        for (int i = 0; i < instructor.size(); i++) {
            assignedInstructors.add(new AssignedInstructor(instructor.get(i).getInstructorId()));
        }
    }

    //ALOOOOOOOOO solution neymiş araştır
    public static void assignInstructor(HashMap<String, String> solution) throws InterruptedException {
        // same with room
        // check timeslot

        // --> iterate over solutions
        // exam x --> timeslot y
        // assign a supervisor z

        for (String exam : solution.keySet()) {

            String timeslotExamSolution = solution.get(exam);

            int unoccupiedInstructor = returnUnoccupiedInstructor(timeslotExamSolution);
            Instructor examInstructor = instructor.get(unoccupiedInstructor);
            examInstructor.getInstructorId();

            // add room to the assingedroom
            AssignedInstructor as = assignedInstructors.get(Integer.parseInt(examInstructor.getInstructorId()));
            as.getOccupiedTimeslots().add(timeslotExamSolution);
        }

    }

    ArrayList<String> maxStudentExams = new ArrayList<String>();

    static int[] examStudentSize = new int[413];

    public static void computeExamStudentSize() {
        // finds total students thats taking the exam
        for (Student s : students) {
            Vector<String> currentStudentClasses = s.getExams();
            for (int i = 0; i < currentStudentClasses.size(); i++) {
                int idOfExam = findExamByName(currentStudentClasses.get(i));
                examStudentSize[idOfExam] += 1;
            }
        }
    }


        // create initial population
        Population population = new Population(GeneticAlgorithm.populationSize).initializePopulation();

        public static void printPopulation (Population population) throws IOException {
            printGA(population.getChromosomes()[0]);

            System.out.println("------------------------");

            for (int i = 0; i < population.getChromosomes().length; i++) {
                System.out.println("Chromosome #" + i + ":" + Arrays.toString(population.getChromosomes()[i].getGenes()) +
                        "| Fitness " + population.getChromosomes()[i].getFitness());
            }
        }

        public static void printGA (Chromosome chromosome) throws IOException {

            int[] genes = chromosome.getGenes();

            for (int i = 0; i < chromosome.getGeneLength(); i++) {
                if (genes[i] == 1) {
                    System.out.println(students.get(i).getStudentId() + " , StudentID: " +
                            exams.get(i).getExamCode() + ", examCode: " + rooms.get(i).getRoomId()
                            + ",roomID):  . ");
                }
            }

        }

            static String[][] timeslots = {

                // preference as cost 1 increases the cost, -1 decreases the cost morning exams not preferred
                //timeslot id, week, timeslot, preference, ga
                {"0", "MO", "1", "07:30-09:30", "1", "0"},
                {"1", "MO", "1", "10:00-12:00", "-1", "0"},
                {"2", "MO", "1", "12:30-14:30", "-1", "0"},
                {"3", "MO", "1", "15:00-17:00", "-1", "0"},
                {"4", "MO", "1", "17:30-19:30", "1", "0"},

                {"5", "TU", "1", "07:30-09:30", "1", "0"},
                {"6", "TU", "1", "10:00-12:00", "-1", "0"},
                {"7", "TU", "1", "12:30-14:30", "-1", "0"},
                {"8", "TU", "1", "15:00-17:00", "-1", "0"},
                {"9", "TU", "1", "17:30-19:30", "1", "0"},

                {"10", "WE", "1", "07:30-09:30", "1", "0"},
                {"11", "WE", "1", "10:00-12:00", "-1", "0"},
                {"12", "WE", "1", "12:30-14:30", "-1", "0"},
                {"13", "WE", "1", "15:00-17:00", "-1", "0"},
                {"14", "WE", "1", "17:30-19:30", "1", "0"},

                {"15", "TH", "1", "07:30-09:30", "1", "0"},
                {"16", "TH", "1", "10:00-12:00", "-1", "0"},
                {"17", "TH", "1", "12:30-14:30", "-1", "0"},
                {"18", "TH", "1", "15:00-17:00", "-1", "0"},
                {"19", "TH", "1", "17:30-19:30", "1", "0"},

                {"20", "FR", "1", "07:30-09:30", "1", "0"},
                {"21", "FR", "1", "10:00-12:00", "-1", "0"},
                {"22", "FR", "1", "12:30-14:30", "-1", "0"},
                {"23", "FR", "1", "15:00-17:00", "-1", "0"},
                {"24", "FR", "1", "17:30-19:30", "1", "0"},


                {"25", "MO", "2", "07:30-09:30", "1", "0"},
                {"26", "MO", "2", "10:00-12:00", "-1", "0"},
                {"27", "MO", "2", "12:30-14:30", "-1", "0"},
                {"28", "MO", "2", "15:00-17:00", "-1", "0"},
                {"29", "MO", "2", "17:30-19:30", "1", "0"},


                {"30", "TU", "2", "07:30-09:30", "1", "0"},
                {"31", "TU", "2", "10:00-12:00", "-1", "0"},
                {"32", "TU", "2", "12:30-14:30", "-1", "0"},
                {"33", "TU", "2", "15:00-17:00", "-1", "0"},
                {"34", "TU", "2", "17:30-19:30", "1", "0"},

                {"35", "WE", "2", "07:30-09:30", "1", "0"},
                {"36", "WE", "2", "10:00-12:00", "-1", "0"},
                {"37", "WE", "2", "12:30-14:30", "-1", "0"},
                {"38", "WE", "2", "15:00-17:00", "-1", "0"},
                {"39", "WE", "2", "17:30-19:30", "1", "0"},

                {"40", "TH", "2", "07:30-09:30", "1", "0"},
                {"41", "TH", "2", "10:00-12:00", "-1", "0"},
                {"42", "TH", "2", "12:30-14:30", "-1", "0"},
                {"43", "TH", "2", "15:00-17:00", "-1", "0"},
                {"44", "TH", "2", "17:30-19:30", "1", "0"},

                {"45", "FR", "2", "07:30-09:30", "1", "0"},
                {"46", "FR", "2", "10:00-12:00", "-1", "0"},
                {"47", "FR", "2", "12:30-14:30", "-1", "0"},
                {"48", "FR", "2", "15:00-17:00", "-1", "0"},
                {"49", "FR", "2", "17:30-19:30", "1", "0"},
            };

        public static void printPopulation (Population population, String heading) throws IOException {
            System.out.println(heading);
            System.out.println("------------------------");
            for (int i = 0; i < population.getChromosomes().length; i++) {

                System.out.println("Chromosome #" + i + ":" + Arrays.toString(population.getChromosomes()[i].getGenes()) +
                        "| Fitness" + population.getChromosomes()[i].getFitness());
            }
        }

        public static void removeFromTimeslotTable(ArrayList<Integer> timeslotTable, int toremove) {
        boolean removed=false;
        for(int i=0; i<timeslotTable.size(); i++) {

            if(timeslotTable.get(i) == toremove) {

                timeslotTable.remove(i);
                removed = true;
            }
        }
    }

        public static void printConflictMatrix() {
        System.out.println("Conflict Matrix: ");
        for(int i=0; i<conflictMatrix.length/40; i++) {
            for(int j=0; j<conflictMatrix.length; j++) {
                System.out.println("the " + "["+i+"]" + "["+j+"] element: " + conflictMatrix[i][j]);
            }
        }
    }

        public static void printTheExamCalendar() { }

    public static void main(String[] args) throws InterruptedException, IOException {

        readStudentsFile();
        readInstructorFile();
        readRoomFile();
        readExamsFile();

        findStudentSizeForExam();
        createAssignmentForRooms();
        createConflictMatrix();

        initializeExamTimeslotCounters(); // createAnts();

        Population population = new Population(GeneticAlgorithm.populationSize).initializePopulation();

        GeneticAlgorithm geneticAlgorithm = new GeneticAlgorithm();

        System.out.println("------------------------");
        System.out.println("Generation #0" + "| Fittest chromosome fitness:" + population.getChromosomes()[0].getFitness());
        //System.out.println("Generation #" + generationNumber+" | Fittest chromosome fitness: "+ population.getChromosomes()[0].getFitness());
        printPopulation(population);

        int generationNumber = 0;
        GeneticAlgorithm ga = new GeneticAlgorithm();

        while (generationNumber < 50) {
            generationNumber++;
            System.out.println("\n ---------------------------");
            population = geneticAlgorithm.evolve(population);
            population.sortChromosomesByFitness();
            System.out.println("Generation #" + generationNumber + " | Fittest chromosome fitness:" + population.getChromosomes()[0].getFitness());
            printPopulation(population);
        }

        System.out.println("> Students: " + students.size());
        System.out.println("> Exams: " + exams.size());
        System.out.println("> Rooms: " + rooms.size());
        System.out.println("> Instructors: " + instructor.size());
        System.out.println("> Conflict Matrix: [" + conflictMatrix.length + "," + conflictMatrix[0].length + "]");
        System.out.println("\n\n");


        System.err.print(">> Genetic Algorithm running...");

        System.out.println();

        //runFirst();
        // runAco();

        System.err.print(">> End.");

    }
}
