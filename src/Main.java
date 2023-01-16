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
import java.time.LocalTime;
import java.util.*;

public class Main {

    public static void main(String[] args) throws InterruptedException, IOException {

        readStudentsFile();
        readInstructorFile();
        readRoomFile();
        readExamsFile();

        findStudentSizeForExam();
        createAssignmentForRooms();

        initializeExamTimeslotCounters();

        System.out.println("> Students: " + students.size());
        System.out.println("> Exams: " + exams.size());
        System.out.println("> Rooms: " + rooms.size());
        System.out.println("> Instructors: " + instructor.size());
        System.out.println("\n\n");
        System.err.print(">> Genetic Algorithm running...");

        Population population = new Population(GeneticAlgorithm.populationSize).initializePopulation();
        GeneticAlgorithm ga = new GeneticAlgorithm();

        System.out.println("------------------------");
        System.out.println("Generation #0" + "| Fittest chromosome fitness:" + population.getChromosomes()[0].getFitness());
        printPopulation(population);

        int generationNumber = 0;

        while (generationNumber < 50) {
            generationNumber++;
            System.out.println("\n ---------------------------------------------------------------------------------------------------------------------------------------");
            population = ga.evolve(population);
            population.sortChromosomesByFitness();
            System.out.println("Generation #" + generationNumber + " | Fittest chromosome fitness:" + population.getChromosomes()[0].getFitness());
            printPopulation(population);
        }

        System.out.println();
        System.err.print(">> End.");
    }

    Chromosome chromosome = new Chromosome();
    int[] genes = chromosome.getGenes();

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

    static List<GeneticAlgorithm> ga = new Vector<>(); //ArrayList

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

    public static void initializeExamTimeslotCounters() {

        for (int i = 0; i < exams.size(); i++) {
            examTimeslotCounters.add(new SlotCounter(i));
        }
    }

    public static int calculateHardConstraints() throws IOException {
        // Calculate hard constraints violations.
        // These constraints must be satisfied for a solution to be valid.
        int hardConstraintsViolations = 0;
        // Iterate through all exams
        for (int i = 0; i < exams.size(); i++) {
            Exam exam1 = exams.get(i);

            // Check if this exam overlaps with any other exam in the same timeslot
            for (int j = 0; j < exams.size(); j++) {
                if (i != j) {  // Skip self-comparison
                    Exam exam2 = exams.get(j);

                    // Check if exams are scheduled at the same time
                    if (exam1.getExamCode().equals(exam2.getExamCode())) {
                        hardConstraintsViolations++;
                    }
                }
            }
        }

        return hardConstraintsViolations;
    }

    public static int calculateHardConstraints2() throws IOException {
        // Calculate hard constraints violations.
        int hardConstraintsViolations = 0;
        // Iterate through all instructors
        for (Instructor instructor : instructor) {
            // Count the number of exams that this instructor is scheduled to proctor in the same timeslot
            int examCount = 0;
            for (Exam exam : exams) {
                if (exam.getExamCode().equals(instructor.getInstructorId()) && exam.getExamCode().equals(instructor.getInstructorAvaibility())) {
                    examCount++;
                }
            }
            // If the instructor is proctoring more than 1 exam in the same timeslot, increment the number of soft constraints violations
            if (examCount > 1) {
                hardConstraintsViolations += examCount - 1;
            }
        }
        return hardConstraintsViolations;
    }

    public static int calculateHardConstraints3() throws IOException {
        int hardConstraintsViolations = 0;

        for (int i = 0; i < examTimeslotCounters.size(); i++) {
            Student student1 = students.get(i);
            if (student1.getMaxExamCount() <= 3) {
                hardConstraintsViolations++;
            }
        }
        return hardConstraintsViolations;
    }

    public static int calculateHardConstraints4() {
        int hardConstraintsViolations = 0;

        // Iterate through all rooms
        for (Room room : rooms) {
            int studentCount = 0;
            // Count the number of students scheduled in this room
            for (AssignedRoom assignedRoom : assignedRooms) {
                if (assignedRoom.getRoomId().equals(room.getRoomId())) {
                    studentCount += assignedRoom.getOccupiedTimeslots().size();
                }
            }
            // If the number of students in the room exceeds the room's seating capacity, increment the number of soft constraints violations
            if (studentCount > Integer.parseInt(room.getSeatingCapacity())) {
                hardConstraintsViolations += studentCount - Integer.parseInt(room.getSeatingCapacity());
            }
        }
        return hardConstraintsViolations;
    }

    public static int calculateSoftConstraint1() {
        int softConstraintsViolations = 0;
        // Iterate through all instructors
        for (Instructor instructor : instructor) {
            int instructorAvaibilityViolations = 0;
            // Count the number of times the instructor is scheduled during their unavailable times
            for (AssignedInstructor assignedInstructor : assignedInstructors) {
                if (assignedInstructor.getInstructorId().equals(instructor.getInstructorId())) {
                    for (String occupiedTimeslot : assignedInstructor.getOccupiedTimeslots()) {
                        if (!instructor.getInstructorAvaibility().contains(occupiedTimeslot)) {
                            instructorAvaibilityViolations++;
                        }
                    }
                }
            }
            // Increment the number of soft constraints violations with the number of times the instructor is scheduled during their unavailable times
            softConstraintsViolations += instructorAvaibilityViolations;
        }
        return softConstraintsViolations;
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

    public static void createAssignmentForRooms() {
        // copy rooms with extra field called assignedTimeslots for that room

        for (int i = 0; i < rooms.size(); i++) {
            assignedRooms.add(new AssignedRoom(String.valueOf(i)));
        }
    }

    public static void createAssignmentForInstructors() {
        // copy rooms with extra field called assignedTimeslots for that room
        for (int i = 0; i < instructor.size(); i++) {
            assignedInstructors.add(new AssignedInstructor(instructor.get(i).getInstructorId()));
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

    public static void printPopulation (Population population) throws IOException {
            printGA(population.getChromosomes()[0]);

            System.out.println("PRINT_POPULATION");

            for (int i = 0; i < population.getChromosomes().length; i++) {
                System.out.println("Chromosome #" + i + ":" + Arrays.toString(population.getChromosomes()[i].getGenes()) + "| Fitness " + population.getChromosomes()[i].getFitness());
            }
    }

    public static void printGA (Chromosome chromosome) throws IOException {

            int[] genes = chromosome.getGenes();
            for (int i = 0; i < chromosome.getGeneLength(); i++) {
                if (genes[i] == 1) {
                    System.out.println(" StudentID : " + students.get(i).getStudentId() + ", examCode : " + exams.get(i).getExamCode() + ", roomID : " + rooms.get(i).getRoomId() + ".");
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

}
