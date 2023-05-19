import java.util.Random;

public class ElevatorRequest implements Runnable {
    private final int numberOfFloors;
    private final int numberOfRequests;
    private final ElevatorManager elevatorManager;
    private final int interval;

    public ElevatorRequest(int numberOfFloors, int numberOfRequests, ElevatorManager elevatorManager, int interval) {
        this.numberOfFloors = numberOfFloors;
        this.numberOfRequests = numberOfRequests;
        this.elevatorManager = elevatorManager;
        this.interval=interval;
    }

    @Override
    public void run() {
        Random random = new Random();

        while (true) {
            try {
                int numbers=random.nextInt(1, numberOfRequests);
                int startingFloor = random.nextInt(1, numberOfFloors);

                for (int i = 1; i < numbers; i++) {
                    int destinationFloor = random.nextInt(1,numberOfFloors + 1);

                    while (startingFloor == destinationFloor) {
                        destinationFloor = random.nextInt(1,numberOfFloors + 1);
                    }

                    elevatorManager.setPassengersList(new MyPassenger(startingFloor, destinationFloor));
                }
                Thread.sleep(interval);
            } catch (InterruptedException interruptedException) {
                System.out.println("error");
            }
        }
    }
}