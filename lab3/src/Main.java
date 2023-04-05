import java.util.Calendar;
import java.util.Scanner;
public class Main {
    public static void main(String[] args) {
        System.out.println("Введите фамилию, имя, отчество и дату рождения в формате дд.мм.гггг");
        Scanner sc = new Scanner(System.in);
        String surname=sc.next();
        String name=sc.next();
        String patronymic=sc.next();
        String datef=sc.next();
        String[] parts=datef.split("\\.");
        int day=Integer.parseInt(parts[0]);
        int month=Integer.parseInt(parts[1]);
        int year=Integer.parseInt(parts[2]);
        Calendar birthday=Calendar.getInstance();
        birthday.setLenient(false);
        birthday.set(year,month-1,day);
        int flag=1;
        try{
            birthday.getTime();
        }
        catch (Exception f){
            System.out.print("error");
            flag=0;
        }
        if (flag==1) {
            Calendar current = Calendar.getInstance();
            int age = current.get(Calendar.YEAR) - birthday.get(Calendar.YEAR);
            if(current.get(Calendar.DAY_OF_YEAR)<birthday.get(Calendar.DAY_OF_YEAR)){
                age=age-1;
            }
            System.out.print(surname+" "+name.charAt(0)+"."+patronymic.charAt(0)+"."+" ");
            if (patronymic.endsWith("ч")) {
                System.out.print("М" + " ");
            } else {
                System.out.print("Ж" + " ");
            }
            System.out.print(age + " ");
            if (age % 10 == 1 && age != 11) {
                System.out.print("год");
            } else {
                if (age % 10 > 1 && age % 10 < 5 && age != 12 && age != 13 && age != 14) {
                    System.out.print("года");
                } else {
                    if (age % 10 > 4 || age % 10 == 0 && age >= 11 && age <= 20) {
                        System.out.print("лет");
                    }
                }
            }
        }
    }
}