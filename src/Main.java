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

public class Main {

    public static void main(String args[]) {
        readStudentsFile();
        readInstructorFile();
        readRoomFile();

        System.out.println(students.size());
        System.out.println(students.size());
        System.out.println(students.size());
    }

    static ArrayList<Integer> randomExamIndices = new ArrayList<>();

    //add-remove operation
    static List<Student> students = new Vector<Student>();

    //add-remove operation
    static List<Instructor> instructor = new Vector<Instructor>();

    //add-remove operation
    static List<Room> rooms = new Vector<Room>();

    //vector because it needs to be synchronized
    static Vector<Exam> exams = new Vector<>();

    public static boolean studentExist(String id) {
        return students.stream().anyMatch(o -> o.getStudentId().equals(id)); //filter(o -> o.getStudentId().equals(id)).findFirst().isPresent();
    }

    private static int findStudentIdByNo(String studentnotemp) {
        return 0;
    }

    public static boolean instructorExist(String id) {
        return students.stream().anyMatch(o -> o.getStudentId().equals(id)); //filter(o -> o.getStudentId().equals(id)).findFirst().isPresent();
    }

    private static int findInstructorIdByNo(String instructornotemp) {
        return 0;
    }

    public static boolean roomExist(String id) {
        return students.stream().anyMatch(o -> o.getStudentId().equals(id)); //filter(o -> o.getStudentId().equals(id)).findFirst().isPresent();
    }

    private static int findRoomIdByNo(String roomnotemp) {
        return 0;
    }

    public static void readStudentsFile() {

        try {
            XSSFWorkbook wb = new XSSFWorkbook(new File("C:\\Users\\Lenovo\\Desktop\\Tez\\students.xlsx"));
            XSSFSheet sheet = wb.getSheetAt(0);

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
                            if(!studentExist(val)) {
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

            FormulaEvaluator formulaEvaluator = wb.getCreationHelper().createFormulaEvaluator();

            DataFormatter fmt = new DataFormatter();

            Vector<String> vector2 = new Vector<>();
            int counterrow = 1;
            int index = 0;
            int countercell = 1;
            for (Row row : sheet) {

                if (counterrow != 1) {
                    for (Cell cell : row) {
                        String cellAddress = cell.getAddress().toString();

                        if (cellAddress.startsWith("A")) {
                            String val = fmt.formatCellValue(cell);
                            if(!instructorExist(val)) {
                                instructor.add(new Instructor(val));
                            }
                        } else if (cellAddress.startsWith("B")) {
                            //System.out.println("(gorev_yeri_fakulte)");
                        } else if (cellAddress.startsWith("C")) {
                            //System.out.println("(gorev_yeri_bolum)");
                        } else if (cellAddress.startsWith("D")) {
                            //System.out.println("students.size: " + students.size());
                            //System.out.println("(unvan)");

                            Cell tempcell = cell.getRow().getCell(0);
                            String instructornotemp = fmt.formatCellValue(tempcell);

                            int instructorindex = findInstructorIdByNo(instructornotemp);

                            instructor.get(instructorindex).addToClasses(cell.getStringCellValue());
                            //instructor.get(index);
                        } else if (cellAddress.startsWith("E")) {
                            //System.out.println("(ad_soyad)");
                        } else if (cellAddress.startsWith("F")) {
                            //System.out.println("(aktiflik)");
                        } else if (cellAddress.startsWith("G")) {
                            //System.out.println("(kadro)");
                        } else if (cellAddress.startsWith("H")) {
                            //System.out.println("(gozetmenlik_derecesi)");
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
                        } else if (cellAddress.startsWith("Q")) {
                            //System.out.println("(gorev_yeri_bolum)");
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

            FormulaEvaluator formulaEvaluator = wb.getCreationHelper().createFormulaEvaluator();

            DataFormatter fmt = new DataFormatter();

            Vector<String> vector3 = new Vector<>();
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
                            if(!roomExist(val)) {
                                rooms.add(new Room(val));
                            }
                        } else if (cellAddress.startsWith("B")) {
                            //System.out.println("(derslik_adi)");
                        } else if (cellAddress.startsWith("C")) {
                            //System.out.println("(bina_kodu)");
                        } else if (cellAddress.startsWith("D")) {
                            //System.out.println("rooms.size: " + rooms.size());
                            //System.out.println("(kat)");

                            Cell tempcell = cell.getRow().getCell(0); // row index should be the same as student index since they are stored in the same order!
                            String roomnotemp = fmt.formatCellValue(tempcell);

                            int roomindex = findRoomIdByNo(roomnotemp);

                            rooms.get(roomindex).addToClasses(cell.getStringCellValue());

                        } else if (cellAddress.startsWith("E")) {
                            //System.out.println("(sıra)");

                        } else if (cellAddress.startsWith("F")) {
                            //System.out.println("(sinav_kapasitesi)");

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

}