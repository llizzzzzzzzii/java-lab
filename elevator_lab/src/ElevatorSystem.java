import java.util.*;

public class ElevatorSystem {
    private final int numFloors;
    private final Elevator[] elevators;
    private final Queue<Request> requests;

    public ElevatorSystem(final int numFloors, final int numElevators) {
        this.numFloors = numFloors;
        this.elevators = new Elevator[numElevators];
        for (int i = 0; i < numElevators; i++) {
            elevators[i] = new Elevator(numFloors);
        }
        this.requests = new LinkedList<>();
    }

    public void run() {
        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                synchronized (requests) {
                    final Request request = requests.poll();
                    if (request != null) {
                        addRequest(request);
                    }
                }

                // Добавляем проверку на наличие заявок перед движеием каждого лифта
                for (final Elevator elevator : elevators) {
                    if (!elevator.isEmpty()) {
                        elevator.step();
                        System.out.println("Elevator " + elevator + " moved to floor " + elevator.getCurrentFloor());
                    }
                }
            }
        }, 0, 1000);

        while (true) {
            final Scanner scanner = new Scanner(System.in);
            System.out.print("Enter pickup floor: ");
            final int pickupFloor = scanner.nextInt();
            System.out.print("Enter dropoff floor: ");
            final int dropoffFloor = scanner.nextInt();
            final int direction = dropoffFloor > pickupFloor ? 1 : -1;
            System.out.println("Request received: pickupFloor=" + pickupFloor + ", direction=" + (direction > 0 ? "up" : "down"));
            synchronized (requests) {
                requests.offer(new Request(pickupFloor, dropoffFloor, direction));
            }
        }
    }

    private void addRequest(final Request request) {
        System.out.println("Adding request: " + request);
        final boolean added = tryAddToExistingElevator(request);
        if (!added) {
            final int bestElevator = findBestElevator(request);
            elevators[bestElevator].addRequest(request);
            System.out.println("Added request to elevator " + bestElevator + ": " + request);
        }
    }

    private boolean tryAddToExistingElevator(final Request request) {
        for (final Elevator elevator : elevators) {
            if (elevator.isGoingTo(request.getPickupFloor(), request.getDirection())) {
                System.out.println("Adding request to existing elevator: " + request);
                elevator.addRequest(request);
                return true;
            }
        }
        return false;
    }

    private int findBestElevator(final Request request) {
        int bestElevator = -1;
        int bestScore = Integer.MAX_VALUE;
        for (int i = 0; i < elevators.length; i++) {
            final int score = elevators[i].score(request);
            if (score < bestScore) {
                bestElevator = i;
                bestScore = score;
            }
        }
        return bestElevator;
    }

    public static class Elevator {
        private final int numFloors;
        private final Queue<Integer> requests;
        private int direction = 0;
        private int currentFloor = 1;

        public Elevator(final int numFloors) {
            this.numFloors = numFloors;
            this.requests = new LinkedList<>();
        }

        public void addRequest(final Request request) {
            requests.offer(request.getPickupFloor());
            requests.offer(request.getDropoffFloor());
        }

        public boolean isGoingTo(final int pickupFloor, final int direction) {
            if (this.direction == 0) {
                return true;
            } else {
                return direction == this.direction && (
                        (this.direction > 0 && pickupFloor >= currentFloor && pickupFloor <= maxFloorInDirection()) ||
                                (this.direction < 0 && pickupFloor <= currentFloor && pickupFloor >= minFloorInDirection())
                );
            }
        }

        private int maxFloorInDirection() {
            return requests.stream()
                    .filter(request -> direction >= 0 && request > currentFloor)
                    .max(Comparator.naturalOrder())
                    .orElse(numFloors);
        }

        private int minFloorInDirection() {
            return requests.stream()
                    .filter(request -> direction < 0 && request < currentFloor)
                    .min(Comparator.naturalOrder())
                    .orElse(1);
        }

        public int score(final Request request) {
            final int stops = stopsUntil(request.getPickupFloor()) + stopsUntil(request.getDropoffFloor());
            final int distance = Math.abs(currentFloor - request.getPickupFloor()) + Math.abs(request.getPickupFloor() - request.getDropoffFloor());
            return stops + distance;
        }

        private int stopsUntil(final int floor) {
            final int index = requests.stream()
                    .filter(request -> direction >= 0 && request > currentFloor && request < floor ||
                            direction < 0 && request < currentFloor && request > floor)
                    .findFirst()
                    .orElse(requests.isEmpty() ? currentFloor : (
                            direction > 0 ? numFloors : 1
                    ));
            return Math.abs(index - currentFloor);
        }

        // Добавляем проверку на наличие заявок перед движеием лифта
        public void step() {
            if (direction == 0) {
                if (!requests.isEmpty()) {
                    final int firstRequest = requests.peek();
                    direction = firstRequest > currentFloor ? 1 : -1;
                }
            } else {
                if (requests.contains(currentFloor)) {
                    requests.remove(currentFloor);
                }
                if (requests.isEmpty()) {
                    direction = 0;
                } else if (currentFloor == 1) {
                    direction = 1;
                } else if (currentFloor == numFloors) {
                    direction = -1;
                } else if (direction == 1) {
                    final int nextRequest = maxFloorInDirection();
                    if (nextRequest == numFloors) {
                        direction = -1;
                    }
                } else if (direction == -1) {
                    final int nextRequest = minFloorInDirection();
                    if (nextRequest == 1) {
                        direction = 1;
                    }
                }
                currentFloor += direction;
            }
        }

        public boolean isEmpty() {
            return requests.isEmpty();
        }

        public int getCurrentFloor() {
            return currentFloor;
        }

        @Override
        public String toString() {
            return "{" + currentFloor + ", " + direction + ", " + requests + "}";
        }
    }

    public static class Request {
        private final int pickupFloor;
        private final int dropoffFloor;
        private final int direction;

        public Request(final int pickupFloor, final int dropoffFloor, final int direction) {
            this.pickupFloor = pickupFloor;
            this.dropoffFloor = dropoffFloor;
            this.direction = direction;
        }

        public int getPickupFloor() {
            return pickupFloor;
        }

        public int getDropoffFloor() {
            return dropoffFloor;
        }

        public int getDirection() {
            return direction;
        }

        @Override
        public String toString() {
            return "(" + pickupFloor + ", " + dropoffFloor + ")";
        }
    }

    public static void main(final String[] args) {
        final ElevatorSystem system = new ElevatorSystem(20, 2);
        system.run();
    }
}