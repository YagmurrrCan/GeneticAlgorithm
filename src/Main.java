//Import library & ApachePoi

import model.Exam;
import model.Instructor;
import model.Room;
import model.Student;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

//If the object is not found
class objectNotInitialized extends Exception
{
    public objectNotInitialized (String str)
    {
        // calling the constructor of parent Exception
        super(str);
    }
}


class scale {

    public static int[] scalePheromones(int[] vals) {
        int[] result = new int[vals.length];
        int min = minArray(vals);
        int max = maxArray(vals);
        int scaleFactor = max - min;
        // scaling between [0..1] for starters. Will generalize later.
        for (int x = 0; x < vals.length; x++) {
            result[x] = ((vals[x] - min) / scaleFactor);
        }
        return result;
    }

    public static double[] scaleHeuristics(double[] vals) {
        double[] result = new double[vals.length];
        double min = minArray(vals);
        double max = maxArray(vals);
        double scaleFactor = max - min;
        // scaling between [0..1] for starters. Will generalize later.
        for (int x = 0; x < vals.length; x++) {
            result[x] = ((vals[x] - min) / scaleFactor);
        }
        return result;
    }

    // The standard collection classes don't have array min and max.
    public static int minArray(int[] vals) {
        int min = vals[0];
        for (int x = 1; x < vals.length; x++) {
            if (vals[x] < min) {
                min = vals[x];
            }
        }
        return min;
    }

    public static int maxArray(int[] vals) {
        int max = vals[0];
        for (int x = 1; x < vals.length; x++) {
            if (vals[x] > max) {
                max = vals[x];
            }
        }
        return max;
    }

    public static double minArray(double[] vals) {
        double min = vals[0];
        for (int x = 1; x < vals.length; x++) {
            if (vals[x] < min) {
                min = vals[x];
            }
        }
        return min;
    }


    public static double maxArray(double[] vals) {
        double max = vals[0];
        for (int x = 1; x < vals.length; x++) {
            if (vals[x] > max) {
                max = vals[x];
            }
        }
        return max;
    }

}


public class Main {

    //add-remove Student operation
    static List<Student> students = new Vector<Student>();
    //add-remove Instructor operation
    static List<Instructor> instructor = new Vector<Instructor>();
    //add-remove Room operation
    static List<Room> rooms = new Vector<Room>();
    //add-remove Exam operation
    static List<Exam> exams = new Vector<>();


    static HashMap<String, String> studentSizeForExams = new HashMap<>();
    static HashMap<String, String> roomSolutions = new HashMap<>();

    static List<AssignedRoom> assignedRooms = new Vector<>();
    static List<AssignedInstructor> assignedInstructors = new Vector<>();

    //GENETIC-ALGORITHM
    final static int population = 15;
    static List<GeneticAlgorithm> ga = new Vector<>();
    static int[][] conflictMatrix;
    static int maxIteration = 2;
    static int trailNumber = maxIteration * population;
    //
    static Random r;
    static int seed = 0;


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
        return ga.indexOf(ga.stream().filter(o -> o.getExamName().equals(examName)).findFirst().get());
        // students.indexOf(students.stream().filter(p -> p.getId().equals(studentno)));
    }

    public static int findStudentIdByNo(String studentno) {
        return students.indexOf(students.stream().filter(o -> o.getStudentId().equals(studentno)).findFirst().get());
    }

    public static void createConflictMatrix() {

        conflictMatrix = new int[exams.size()][exams.size()];

        int studentIterator = 0;
        while (studentIterator < students.size()) {

            Student currentStudent = students.get(studentIterator);
            Vector<String> currentExams = currentStudent.getClasses();

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

    static HashMap<Integer, Boolean> chosenExamsTable = new HashMap<>();
    static HashMap<Integer, Integer> chosenFrequencyTable = new HashMap<Integer, Integer>();
    List<int[]> eligibleSolutions = new ArrayList<int[]>();

    static double evaluatePartialSolution(HashMap<String, String> partialSolution, String nextExam, String nextTimeslot) throws InterruptedException {

        double cost = 0;
        cost = firstSoftC(partialSolution, nextExam, nextTimeslot);
        cost = secondSoftC(cost, partialSolution, nextExam, nextTimeslot);

        return cost;

    }

    static int returnHighesTimeslot(HashMap<String, String> partialSolution, String nextExam) throws InterruptedException {
        int timeslot = -1;
        double maxProb = Double.MAX_VALUE;

        for (int i = 0; i < timeslots.length; i++) {
            if ((evaluatePartialSolution(partialSolution, nextExam, String.valueOf(i))) > maxProb) {
                timeslot = i;
            }
        }

        return timeslot;
    }

    static double secondSoftC(double cost, HashMap<String, String> ga, String nextExam, String nextTimeslot) throws InterruptedException {

        double midCost = cost;

        double softConstraintFactor = 0.3;
        int studentExamCounter = 0;


        // 2) having more than 2 exams increases cost
        for (Student s : students) {

            Vector<String> currentStudentExams = s.getClasses();

            for (int i = 0; i < currentStudentExams.size(); i++) {

                studentExamCounter = 0;
                String currentExam = currentStudentExams.get(i);

                int currentExamTimeslot = Integer.parseInt(ga.get(currentExam));
                int nextExamTimeslot = Integer.parseInt(nextTimeslot);

                if (timeslots[currentExamTimeslot][2] == timeslots[nextExamTimeslot][2]) {
                    // means they are assigned to the same week
                    if (timeslots[currentExamTimeslot][1] == timeslots[nextExamTimeslot][1]) {
                        // means they are assigned to the same day
                        studentExamCounter += 1;

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
            Vector<String> currentStudentExams = s.getClasses();

            for (int i = 0; i < currentStudentExams.size(); i++) {

                String currentExam = currentStudentExams.get(i);
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

    public static void partialSoftConstraintStudentOverloadCheck(HashMap<String, String> ga) throws InterruptedException {

        // TODO:
        // change it to be pheromones again
        double softConstraintFactor = 0.2;

        // 1) having exams back to back increases cost

        for (Student s : students) {
            Vector<String> currentStudentExams = s.getClasses();

            for (int i = 0; i < currentStudentExams.size(); i++) {

                String currentExam = currentStudentExams.get(i);
                int currentExamId = findExamByName(currentExam);


                for (int k = i + 1; k < currentStudentExams.size(); k++) {

                    String nextExam = currentStudentExams.get(k);
                    int nextExamId = findExamByName(nextExam);

                    int currentExamTimeslot = Integer.parseInt(ga.get(currentExam)); // gives which timeslot it was assigned
                    int nextExamTimeslot = Integer.parseInt(ga.get(nextExam));

                    Exam e1 = exams.get(currentExamId);
                    Exam e2 = exams.get(nextExamId);

                    //double cost = e1.pheromones[currentClassTimeslot];

                    if (Math.abs(currentExamTimeslot - nextExamTimeslot) == 1) {
                        //cost += cost + cost*softConstraintFactor;


                        // e1.pheromones[currentExamTimeslot] = e1.pheromones[currentExamTimeslot] - e1.pheromones[currentExamTimeslot] * softConstraintFactor;
                        // e2.pheromones[nextExamTimeslot] = e2.pheromones[nextExamTimeslot] - e2.pheromones[nextExamTimeslot] * softConstraintFactor;

                    } else if (Math.abs(currentExamTimeslot - nextExamTimeslot) == 0) {
                        // since its looking for a single student
                        // meaning the classes cant have exams simultaneously

                        //System.out.println("Hard constraint fault!!");
                        //Thread.sleep(5000);

                    } else {


                    }
                }
            }
        }


    }

    public static void partialSoftConstraintStudentMaxExam(HashMap<String, String> ga) throws InterruptedException {
        double softConstraintFactor = 0.3;
        int studentExamCounter = 0;


        // 2) having more than 2 exams increases cost

        for (Student s : students) {
            //studentExamCounter=0;
            Vector<String> currentStudentClasses = s.getClasses();
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

                        //double cost = e1.pheromones[currentClassTimeslot];

                        if (timeslots[currentClassTimeslot][2] == timeslots[nextClassTimeslot][2]) {
                            // means they are assigned to the same week
                            if (timeslots[currentClassTimeslot][1] == timeslots[nextClassTimeslot][1]) {
                                // means they are assigned to the same day
                                studentExamCounter += 1;
                                if (studentExamCounter > 2) {
                                    //     e1.pheromones[currentClassTimeslot] = e1.pheromones[currentClassTimeslot] - e1.pheromones[currentClassTimeslot]*softConstraintFactor;
                                    //     e2.pheromones[nextClassTimeslot] = e2.pheromones[nextClassTimeslot] - e2.pheromones[nextClassTimeslot]*softConstraintFactor;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public static void softConstraintStudentOverloadCheck(HashMap<String, String> ga) throws InterruptedException {
        // TODO:
        // change it to be pheromones again
        double softConstraintFactor = 0.2;

        // 1) having exams back to back increases cost

        for (Student s : students) {
            Vector<String> currentStudentExams = s.getClasses();
            for (int i = 0; i < currentStudentExams.size(); i++) {

                String currentExam = currentStudentExams.get(i);
                int currentExamId = findExamByName(currentExam);


                for (int k = i + 1; k < currentStudentExams.size(); k++) {

                    String nextExam = currentStudentExams.get(k);
                    int nextExamId = findExamByName(nextExam);

                    int currentExamTimeslot = Integer.parseInt(ga.get(currentExam)); // gives which timeslot it was assigned
                    int nextExamTimeslot = Integer.parseInt(ga.get(nextExam));

                    Exam e1 = exams.get(currentExamId);
                    Exam e2 = exams.get(nextExamId);

                    //double cost = e1.pheromones[currentClassTimeslot];

                    if (Math.abs(currentExamTimeslot - nextExamTimeslot) == 1) {
                        //cost += cost + cost*softConstraintFactor;


                        //     e1.pheromones[currentExamTimeslot] = e1.pheromones[currentExamTimeslot] + e1.pheromones[currentExamTimeslot] * softConstraintFactor;
                        //    e2.pheromones[nextExamTimeslot] = e2.pheromones[nextExamTimeslot] + e2.pheromones[nextExamTimeslot] * softConstraintFactor;

                    } else if (Math.abs(currentExamTimeslot - nextExamTimeslot) == 0) {
                        // since its looking for a single student
                        // meaning the classes cant have exams simultaneously

                        System.out.println("Hard constraint fault!!");
                        //Thread.sleep(5000);

                    } else {


                    }
                }
            }
        }
    }

    public static void softConstraintStudentMaxExam(HashMap<String, String> ga) throws InterruptedException {
        double softConstraintFactor = 0.3;
        int studentExamCounter = 0;
        int[][] countedExams = new int[413][timeslots.length];

        // 2) having more than 2 exams increases cost

        for (Student s : students) {
            //studentExamCounter=0;
            Vector<String> currentStudentClasses = s.getClasses();
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


                        //double cost = e1.pheromones[currentClassTimeslot];

                        if (timeslots[currentClassTimeslot][2] == timeslots[nextClassTimeslot][2]) {
                            // means they are assigned to the same week
                            if (timeslots[currentClassTimeslot][1] == timeslots[nextClassTimeslot][1]) {
                                // means they are assigned to the same day


                                studentExamCounter += 1;

                                if (studentExamCounter > 2) {

                                    //     e1.pheromones[currentClassTimeslot] = e1.pheromones[currentClassTimeslot] + e1.pheromones[currentClassTimeslot]*softConstraintFactor;
                                    //      e2.pheromones[nextClassTimeslot] = e2.pheromones[nextClassTimeslot] + e2.pheromones[nextClassTimeslot]*softConstraintFactor;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public static double softConstraintExamWeekRelation(double cost, HashMap<String, String> ga) {

        double softConstraintFactor = 0.1;


        int maxExam = 0;
        int index = -1;

        for (int x = 0; x < examStudentSize.length; x++) {
            if (maxExam < examStudentSize[x]) {
                maxExam = examStudentSize[x];
                index = x;
            }
        }

        String timeslot = ga.get(exams.get(index).getExamName());
        if (Integer.parseInt(timeslots[Integer.parseInt(timeslot)][2]) == 2) {
            cost = cost + cost * softConstraintFactor;
        }

        return cost;

    }


    public static void readStudentsFile() {

        try {
            XSSFWorkbook wb = new XSSFWorkbook(new File("C:\\Users\\Lenovo\\Desktop\\Tez\\students.xlsx"));
            XSSFSheet sheet = wb.getSheetAt(0);

            //bunu silmiş
            FormulaEvaluator formulaEvaluator = wb.getCreationHelper().createFormulaEvaluator();
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

                            students.get(studentindex).addToClasses(cell.getStringCellValue());
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

            //bunu kapamış
            FormulaEvaluator formulaEvaluator = wb.getCreationHelper().createFormulaEvaluator();

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

            //bunu silmiş
            FormulaEvaluator formulaEvaluator = wb.getCreationHelper().createFormulaEvaluator();

            DataFormatter fmt = new DataFormatter();

            int counterrow = 1;
            int index = 0;
            int countercell = 1;

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
                            room.setCapacity(capacity);

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
            List<String> tempExams = s.getClasses();
            for (String c : tempExams) {
                if (!courseExist(c)) {
                    exams.add(new Exam(c));
                    counter++;

                }

            }

        }


    }


    public static void findStudentSizeForExam() {

        int size = 0;

        for (Exam e : exams) {
            size = 0;
            String examname = e.getExamName();

            for (Student currentStudent : students) {
                if (currentStudent.getClasses().contains(examname)) {
                    size++;
                }
            }
            studentSizeForExams.put(examname, String.valueOf(size));
        }
    }

    public static double evaluateSolution(HashMap<String, String> ga) throws InterruptedException {
        double cost = 0;
        // the argument doesnt need a timeslot
        // because all timeslots will be used


        //student soft constraints
        softConstraintStudentOverloadCheck(ga);
        softConstraintStudentMaxExam(ga);
        //softConstraintExamWeekRelation(ga);

        // exams and timeslots edges are updated
        // TODO: how to use them?


        return cost;

    }

    public static void createAssignmentForRooms() {
        // copy rooms with extra field called assignedTimeslots for that room

        for (int i = 0; i < rooms.size(); i++) {
            assignedRooms.add(new AssignedRoom(String.valueOf(i)));
        }
    }

    public static int returnUnoccupiedRoom(String timeslot) throws InterruptedException {

        int i = 0;
        int errorCode = -1;

        for (i = 0; i < assignedRooms.size(); i++) {

            if (!assignedRooms.get(i).getBookedTimeslots().contains(timeslot)) {
                return i;
            }
        }

        // if no room unoccupied:
        if (i == assignedRooms.size()) {


            if (assignedRooms.get(i).getBookedTimeslots().contains(timeslot)) {
                System.out.println("Error assigning room to the exam!");
                Thread.sleep(3000);
                return errorCode;
            }

        }
        System.out.println("Error assigning room to the exam!");
        Thread.sleep(3000);
        return errorCode;


    }

    public static int returnUnoccupiedSupervisor(String timeslot) throws InterruptedException {

        int i = 0;
        int errorCode = -1;

        for (i = 0; i < assignedInstructors.size(); i++) {

            if (!assignedInstructors.get(i).getBookedTimeslots().contains(timeslot)) {
                return i;
            }
        }

        // if no room unoccupied:
        if (i == assignedInstructors.size()) {


            if (assignedInstructors.get(i).getBookedTimeslots().contains(timeslot)) {
                System.out.println("Error assigning instructor to the exam!");
                Thread.sleep(3000);
                return errorCode;
            }

        }
        System.out.println("Error assigning instructor to the exam!");
        Thread.sleep(3000);
        return errorCode;


    }

    public static void assignRoom(HashMap<String, String> ga) throws InterruptedException {
        // runs after assigning timeslots
        // need a list of assigned rooms till now

        // 1. room chosen iteratively
        // treat each room equal


        int requiredSpace = 1;
        int roomCounter = 0;


        // --> iterate over solutions
        // exam x --> timeslot y
        // assign a room z

        for (String exam : ga.keySet()) {


            int sizeForTheExam = Integer.parseInt(studentSizeForExams.get(exam));
            requiredSpace = sizeForTheExam;

            while (requiredSpace > 0) {

                // get a room with the counter

                // get a room thats not occupied check it with the timeslot


                String timeslotExamSolution = ga.get(exam);

                int unoccupiedRoom = returnUnoccupiedRoom(timeslotExamSolution);
                Room examRoom = rooms.get(unoccupiedRoom);

                // check if size bigger than the capacity

                int capacity = Integer.parseInt(rooms.get(roomCounter).getCapacity());

                // if size bigger than the capacity need more rooms!
                requiredSpace = sizeForTheExam - capacity;

                // add room to the assignedRoom
                AssignedRoom ar = assignedRooms.get(Integer.parseInt(examRoom.getRoomId()));
                ar.getBookedTimeslots().add(timeslotExamSolution);
            }


        }


    }


    public static void createAssignmentForInstructors() {
        // copy rooms with extra field called assignedTimeslots for that room


        for (int i = 0; i < instructor.size(); i++) {
            assignedInstructors.add(new assignedInstructor(instructor.get(i).getInstructorId()));
        }
    }


    public static void assignInstructor(HashMap<String, String> ga) throws InterruptedException {
        // same with room
        // check timeslot

        // --> iterate over solutions
        // exam x --> timeslot y
        // assign a supervisor z

        for (String exam : ga.keySet()) {

            String timeslotExamSolution = ga.get(exam);

            int unoccupiedInstructor = returnUnoccupiedInstructor(timeslotExamSolution);
            Instructor examInstructor = instructor.get(unoccupiedInstructor);
            examInstructor.getInstructorId();

            // check if size bigger than the capacity


            // add room to the assingedroom
            AssignedInstructor as = AssignedInstructors.get(Integer.parseInt(examInstructor.getInstructorId()));
            as.getBookedTimeslots().add(timeslotExamSolution);
        }

    }

    ArrayList<String> maxStudentExams = new ArrayList<String>();

    static int[] examStudentSize = new int[413];

    public static void computeExamStudentSize() {
        // finds total students thats taking the exam
        for (Student s : students) {
            Vector<String> currentStudentClasses = s.getClasses();
            for (int i = 0; i < currentStudentClasses.size(); i++) {
                int idOfExam = findExamByName(currentStudentClasses.get(i));
                examStudentSize[idOfExam] += 1;

            }

        }
    }


    public static void main(String[] args) throws InterruptedException, objectNotInitialized, IOException {

        readStudentsFile();
        readInstructorFile();
        readRoomFile();
        readExamsFile();


        System.out.println("> Students: " + students.size());
        System.out.println("> Exams: " + exams.size());
        System.out.println("> Rooms: " + rooms.size());
        System.out.println("> Instructors: " + instructor.size());
        System.out.println("\n\n");
        System.err.print(">> GA running...");

        System.out.println();

        System.err.print(">> End.");


      /*
        private static int findInstructorIdByNo (String instructornotemp){
            return 0;
        }

        private static int findRoomIdByNo (String roomnotemp){
            return 0;
        }

       */
        Population population = new Population(GeneticAlgorithm.populationSize).initializePopulation();
        GeneticAlgorithm geneticAlgorithm = new GeneticAlgorithm();
        System.out.println("------------------------");
        System.out.println("Generation #0"+"| Fittest chromosome fitness:"+ population.getChromosomes()[0].getFitness());
        printPopulation(population);

        int generationNumber = 0;

        while(generationNumber < 50) {
            generationNumber++;
            System.out.println("\n ---------------------------");
            population = geneticAlgorithm.changePopulation(population);
            population.sortChromosomesByFitness();
            System.out.println("Generation #" + generationNumber + " | Fittest chromosome fitness:" + population.getChromosomes()[0].getFitness());
            printPopulation(population);

        }

    }

    public static void printPopulation(Population population) throws IOException {
        printIngredients(population.getChromosomes()[0]);

        System.out.println("------------------------");

        for (int i = 0; i < population.getChromosomes().length; i++) {
            System.out.println("Chromosome #" + i + ":" + Arrays.toString(population.getChromosomes()[i].getGenes()) +
                    "| Fitness " + population.getChromosomes()[i].getFitness());
        }
    }

    public static void printIngredients(Chromosome chromosome) throws IOException {

        int[] genes = chromosome.getGenes();

        for (int i = 0; i < chromosome.getGenesLength(); i++) {
            if (genes[i] == 1) {
                System.out.println(students.get(i).getStudentId() + " , StudentID: "+
                        exams.get(i).getExamCode() +", examCode: "+ rooms.get(i).getRoomId()
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


    public static void calculateMaxCalorie(int age, int gender, boolean pregnancy, int weight){
        maxCalorieIntake=2400;
    }

    public static void printPopulation(Population population, String heading) throws IOException {
        System.out.println(heading);
        System.out.println("------------------------");
        for(int i=0;i<population.getChromosomes().length;i++){

            System.out.println("Chromosome #" +i+":"+ Arrays.toString(population.getChromosomes()[i].getGenes())+
                    "| Fitness" + population.getChromosomes()[i].getFitness());
        }



    }
}
