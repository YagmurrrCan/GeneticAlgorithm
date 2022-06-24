//Import library & ApachePoi
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
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.HashMap;

//If the object is not found
class objectNotInitialized extends Exception
{
    public objectNotInitialized (String str)
    {
        // calling the constructor of parent Exception
        super(str);
    }
}

public class Main {

    // static ArrayList<Integer> randomExamIndices = new ArrayList<>();

    //add-remove Student operation
    static List<Student> students = new Vector<Student>();

    //add-remove Instructor operation
    static List<Instructor> instructor = new Vector<Instructor>();

    //add-remove Room operation
    static List<Room> rooms = new Vector<Room>();

    //add-remove Exam operation
    static List<Exam> exams = new Vector<>();
    //static Vector<Exam> exams = new Vector<>();

    //GENETIC-ALGORITHM
    //

    public static boolean studentExist (String id){
        return students.stream().anyMatch(o -> o.getStudentId().equals(id)); //filter(o -> o.getStudentId().equals(id)).findFirst().isPresent();
    }

    public static boolean instructorExist(String id){
        return instructor.stream().filter(o -> o.getInstructorId().equals(id)).findFirst().isPresent();
    }

    public static boolean roomExist(String id){
        return rooms.stream().filter(o -> o.getRoomId().equals(id)).findFirst().isPresent();
    }

    public static boolean courseExist(String examName){
        return exams.stream().filter(o -> o.getExamName().equals(examName)).findFirst().isPresent();
    }

    public static int findExamByName(String examName) {
        return exams.indexOf(exams.stream().filter(o->o.getExamName().equals(examName)).findFirst().get());
    }

    public static int findSolutionByName(String examName) {
        return solutions.indexOf(solutions.stream().filter(o->o.getExamName().equals(examName)).findFirst().get());
        // students.indexOf(students.stream().filter(p -> p.getId().equals(studentno)));
    }

    public static int findStudentIdByNo(String studentno) {
        return students.indexOf(students.stream().filter(o->o.getStudentId().equals(studentno)).findFirst().get());
    }

    public static void createConflictMatrix() {

        conflictMatrix = new int[exams.size()][exams.size()];
        // corresponds to the

        int studentIterator=0;
        while(studentIterator<students.size()) {

            Student currentStudent = students.get(studentIterator);
            Vector<String> currentExams = currentStudent.getClasses();

            for(int i=0;i<currentExams.size();i++) {
                /*
                 * for class x of student y
                 * check every other classes of the student y
                 * and increases the old value which is initially 0
                 */

                String currentExam = currentExams.get(i);
                int idOfCurrentExam = findExamByName(currentExam);

                for(int j=i+1;j<currentExams.size();j++) {
                    String nextExam = currentExams.get(j);
                    int idOfNextExam = findExamByName(nextExam);

                    int oldConflict = conflictMatrix[idOfCurrentExam][idOfNextExam];
                    int oldConflictReverse = conflictMatrix[idOfNextExam][idOfCurrentExam];
                    conflictMatrix[idOfCurrentExam][idOfNextExam] = oldConflict+1;
                    conflictMatrix[idOfNextExam][idOfCurrentExam] = oldConflictReverse+1;

                }
            }
            studentIterator++;
        }
    }

    static double evaluatePartialSolution(HashMap<String, String> partialSolution, String nextExam, String nextTimeslot) throws InterruptedException {

        double cost = 0;
        cost = firstSoftC(partialSolution, nextExam, nextTimeslot);
        cost = secondSoftC(cost, partialSolution, nextExam, nextTimeslot);

        return cost;

    }

    static int returnHighesTimeslot(HashMap<String, String> partialSolution, String nextExam) throws InterruptedException {
        int timeslot = -1;
        double maxProb = Double.MAX_VALUE;

        for(int i=0;i<timeslots.length;i++) {
            if((evaluatePartialSolution(partialSolution, nextExam, String.valueOf(i))) > maxProb) {
                timeslot = i;
            }
        }

        return timeslot;
    }

    public static void readStudentsFile () {

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

    public static void readInstructorFile () {

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

                        if(cellAddress.startsWith("A")) {
                            //System.out.println("(id of supervisor)");
                            String id = fmt.formatCellValue(cell);
                            if(!instructorExist(id)) {
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

                            if(degree.equals(yapmaz)) {
                                instruct.setInstructorAvaibility("None");
                            }
                            if(degree.equals(fazla)) {
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

    public static void readRoomFile () {

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
        for(Student s : students) {
            List<String> tempExams = s.getClasses();
            for(String c: tempExams) {
                if(!courseExist(c)) {
                    exams.add(new Exam(c));
                    counter++;

                }

            }

        }


    }

    static List<AssignedRoom> assignedRooms = new Vector<>();
    static List<AssignedInstructor> assignedInstructors = new Vector<>();

    public static void createAssignmentForRooms(){
        // copy rooms with extra field called assignedTimeslots for that room

        for(int i=0;i<rooms.size();i++) {
            assignedRooms.add(new AssignedRoom(String.valueOf(i)));
        }
    }

    public static int returnUnoccupiedRoom(String timeslot) throws InterruptedException {

        int i=0;
        int errorCode=-1;

        for(i=0;i<assignedRooms.size();i++) {

            if(!assignedRooms.get(i).getBookedTimeslots().contains(timeslot)) {
                return i;
            }
        }

        // if no room unoccupied:
        if(i==assignedRooms.size()) {


            if(assignedRooms.get(i).getBookedTimeslots().contains(timeslot)) {
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

        int i=0;
        int errorCode=-1;

        for(i=0;i<assignedInstructors.size();i++) {

            if(!assignedInstructors.get(i).getBookedTimeslots().contains(timeslot)) {
                return i;
            }
        }

        // if no room unoccupied:
        if(i==assignedInstructors.size()) {


            if(assignedInstructors.get(i).getBookedTimeslots().contains(timeslot)) {
                System.out.println("Error assigning instructor to the exam!");
                Thread.sleep(3000);
                return errorCode;
            }

        }
        System.out.println("Error assigning instructor to the exam!");
        Thread.sleep(3000);
        return errorCode;


    }

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

        for(String exam:solution.keySet()) {


            int sizeForTheExam = Integer.parseInt(studentSizeForExams.get(exam));
            requiredSpace = sizeForTheExam;

            while(requiredSpace>0) {

                // get a room with the counter

                // get a room thats not occupied check it with the timeslot


                String timeslotExamSolution = solution.get(exam);

                int unoccupiedRoom = returnUnoccupiedRoom(timeslotExamSolution);
                Room examRoom = rooms.get(unoccupiedRoom);

                // check if size bigger than the capacity

                int capacity = Integer.parseInt(rooms.get(roomCounter).getCapacity());

                // if size bigger than the capacity need more rooms!
                requiredSpace = sizeForTheExam-capacity;

                // add room to the assignedRoom
                AssignedRoom ar = assignedRooms.get(Integer.parseInt(examRoom.getId()));
                ar.getBookedTimeslots().add(timeslotExamSolution);
            }



        }


    }


    public static void createAssignmentForInstructors(){
        // copy rooms with extra field called assignedTimeslots for that room


        for(int i=0;i<instructor.size();i++) {
            assignedInstructors.add(new assignedInstructor(instructor.get(i).getId()));
        }
    }


    public static void assignInstructor(HashMap<String, String> solution) throws InterruptedException{
        // same with room
        // check timeslot




        // --> iterate over solutions
        // exam x --> timeslot y
        // assign a supervisor z

        for(String exam:solution.keySet()) {

            String timeslotExamSolution = solution.get(exam);

            int unoccupiedSupervisor = returnUnoccupiedSupervisor(timeslotExamSolution);
            Supervisor examSupervisor = supervisors.get(unoccupiedSupervisor);
            examSupervisor.getId();

            // check if size bigger than the capacity


            // add room to the assingedroom
            AssignedInstructor as = AssignedInstructors.get(Integer.parseInt(examSupervisor.getId()));
            as.getBookedTimeslots().add(timeslotExamSolution);
        }

    }


    ArrayList<String> maxStudentExams = new ArrayList<String>();

    public static void main(String[] args) throws InterruptedException, objectNotInitialized {
        
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

    }
}