import java.util.*;
public class Main {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("¬ведите максимальное число за€вок, которые могут быть поданы людьми с одного этажа за один шаг");

        int number_of_requests = sc.nextInt();

        System.out.println("¬ведите интервал");

        int interval = sc.nextInt();

        System.out.println("¬ведите максимальную вместимость лифта");

        int payload = sc.nextInt();

        System.out.println("¬ведите количество этажей");

        int amount_floors = sc.nextInt();

        ElevatorManager manager = new ElevatorManager(amount_floors, payload, interval);

        ElevatorRequest request = new ElevatorRequest(amount_floors, number_of_requests, manager,interval);

        Thread requestsThread = new Thread(request);

        Thread elevatorsThread = new Thread(manager);

        requestsThread.start();

        elevatorsThread.start();
    }
}

