import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
public class counter {
    /**
     * файл, с которого читаем
     * файл, куда записываем
     * словарик, где считаем вхождения символов
     */
    private FileInputStream input;
    private FileOutputStream output;
    private HashMap<Character, Integer> dict = new HashMap<Character, Integer>();

    /**
     * подсчёт вхождений
     * @param input - файл, с которого читаем
     * @throws IllegalArgumentException
     */
    public void countfreq(FileInputStream input) throws IllegalArgumentException {
        try {
            int c;
            while ((c =input.read())!=-1) {
                char sym = (char) c;
                if (dict.containsKey(sym)) {
                    int freq = dict.get(sym);
                    dict.put(sym, ++freq);
                } else {
                    dict.put(sym, 1);
                }
            }
//            System.out.println(dict);
        } catch (FileNotFoundException ex) {
            System.out.println("Не найден файл");
            throw new RuntimeException(ex);
        } catch (IOException ex) {
            System.out.println("Ошибка при чтении файла");
            throw new RuntimeException(ex);
        }
    }

    /**
     * запись в файл
     * @param output - файл, в который записываем
     * @throws IllegalArgumentException
     */
    public void record(FileOutputStream output) throws IllegalArgumentException {
        try {
            for (Map.Entry<Character, Integer> entry : dict.entrySet()) {
                output.write((entry.getKey() + ":" + entry.getValue()+"\n").getBytes(StandardCharsets.UTF_8));
            }
        } catch (IOException ex) {
            System.out.println("Ошибка при чтении файла");
            throw new RuntimeException(ex);
        }
    }
}
