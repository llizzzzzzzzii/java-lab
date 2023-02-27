import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IllegalArgumentException {
        try {
            System.out.println("Введите название файла, в котором хотите посчитать вхождения символов, в формате *имя файла.txt*");
            Scanner sc = new Scanner(System.in);
            String inputfile,outputfile;
            inputfile=sc.nextLine();
            System.out.println("Введите название файла, куда хотите записать данные, в формате *имя файла.txt*");
            outputfile=sc.nextLine();
            FileInputStream input = new FileInputStream(inputfile);
            FileOutputStream output = new FileOutputStream(outputfile);
            counter c = new counter();
            c.countfreq(input);
            c.record(output);
        }
        catch (FileNotFoundException ex) {
        System.out.println("Не найден файл" );
        throw new RuntimeException(ex);
    }
    }
}