import java.util.ArrayList;

public class ElevatorManager implements Runnable {
    private int floorsAmount;
    private int payloadWeight;
    private int interval;
    private final ArrayList<MyPassenger> passengersList = new ArrayList<>();
    private final ArrayList<Lift> elevatorsList = new ArrayList<>();

    public ArrayList<MyPassenger> getPassengersList() {
        return passengersList;
    }


    public void setFloorsAmount(int floorsAmount) {

        this.floorsAmount = floorsAmount;
    }

    public void setPayloadWeight(int payloadWeight) {
        this.payloadWeight = payloadWeight;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public void setElevatorsList(Lift elevator) {
        this.elevatorsList.add(elevator);
    }

    public void setPassengersList(MyPassenger passenger) {
        this.passengersList.add(passenger);
    }

    ElevatorManager(int floorsAmount, int payloadWeight, int interval) {
        setFloorsAmount(floorsAmount);
        setPayloadWeight(payloadWeight);
        setInterval(interval);

        setElevatorsList(new Lift(1));
        setElevatorsList(new Lift(2));
    }

    @Override
    public void run() {
        int count = 0;
        while (true) {
            try {
                count++;
                int usedElevators = 0;
                System.out.println("Шаг № " + count);

                for (Lift elevator : elevatorsList) {
                    elevator.move();

                    int currentFloor = elevator.getCurrentFloor();

                    int availablePayloadWeight = usedElevators * payloadWeight;

                    if (payloadWeight - elevator.getPassengersList().size() > 0) {

                        ArrayList<MyPassenger> requestedUp = new ArrayList<>();

                        ArrayList<MyPassenger> requestedDown = new ArrayList<>();

                        ArrayList<MyPassenger> upDirectedPeople = new ArrayList<>();

                        ArrayList<MyPassenger> downDirectedPeople = new ArrayList<>();

                        for (MyPassenger passenger : getPassengersList()) {
                            int passengerStartFloor = passenger.getStartFloor();
                            int passengerDirection = passenger.getDirection();

                            if (passengerStartFloor > currentFloor) {
                                requestedUp.add(passenger);
                                System.out.println("пассажир " + passenger + " ожидает вверху на " + passengerStartFloor + " этаже");
                            } else if (passengerStartFloor < currentFloor) {
                                requestedDown.add(passenger);
                                System.out.println("пассажир " + passenger + " ожидает внизу на " + passengerStartFloor + " этаже");
                            }
                            if (passengerStartFloor == currentFloor && passengerDirection == Destination.Up) {
                                upDirectedPeople.add(passenger);
                                System.out.println("пассажир " + passenger + " хочет зайти в лифт " + elevator.getId() + " на " + currentFloor + " этаже и поехать вверх");

                            } else if (passengerStartFloor == currentFloor && passengerDirection == Destination.Down) {
                                downDirectedPeople.add(passenger);
                                System.out.println("пассажир " + passenger + " хочет зайти в лифт " + elevator.getId() + " на " + currentFloor + " этаже и поехать вниз");
                            }
                        }

                        if (elevator.getPassengersList().size() == 0 && currentFloor == floorsAmount) {
                            if (elevator.getDirection() == Destination.Up && (requestedUp.size() < availablePayloadWeight
                                    && requestedDown.size() < availablePayloadWeight)) {
                                elevator.setDirection(Destination.Stopped);
                            } else if (requestedDown.size() > availablePayloadWeight || currentFloor == floorsAmount) {
                                elevator.setDirection(Destination.Down);
                            }
                        } else if (currentFloor == 1) {
                            if (requestedUp.size() > availablePayloadWeight) {
                                elevator.setDirection(Destination.Up);
                            } else {
                                elevator.setDirection(Destination.Stopped);
                            }
                        } else {
                            elevator.setDirection(Destination.Up);
                        }


                        if (elevator.getDirection() == Destination.Stopped) {
                            if (upDirectedPeople.size() > downDirectedPeople.size()
                                    && requestedUp.size() > availablePayloadWeight) {
                                elevator.setDirection(Destination.Up);
                            } else if (upDirectedPeople.size() <= downDirectedPeople.size()
                                    && requestedDown.size() > availablePayloadWeight) {
                                elevator.setDirection(Destination.Down);
                            }
                        }

                        if (currentFloor == floorsAmount) {
                            elevator.setDirection(Destination.Down);
                        }


                        if (currentFloor == 1 && elevator.getDirection() == Destination.Stopped && upDirectedPeople.size() > downDirectedPeople.size()
                                && requestedUp.size() > availablePayloadWeight ) {
                            elevator.setDirection(Destination.Up);
                        }
                        if (currentFloor == 1 && elevator.getDirection() == Destination.Stopped && upDirectedPeople.size() <= downDirectedPeople.size()
                                && requestedDown.size() > availablePayloadWeight ) {
                            elevator.setDirection(Destination.Down);
                        }
                        if (currentFloor == 1 && elevator.getDirection() == Destination.Stopped && upDirectedPeople.size()!=0) {
                            elevator.setDirection(Destination.Up);
                        }
                        if (currentFloor == 1 && elevator.getDirection() == Destination.Stopped && downDirectedPeople.size()!=0) {
                            elevator.setDirection(Destination.Up);
                        }
                        if (elevator.getDirection() == Destination.Up && requestedUp.size()==0 && requestedDown.size()!=0){
                            elevator.setDirection(Destination.Down);
                        }
                        if (elevator.getDirection() == Destination.Down && requestedUp.size()!=0 && requestedDown.size()==0){
                            elevator.setDirection(Destination.Up);
                        }
                        if ((elevator.getDirection() == Destination.Down || elevator.getDirection() == Destination.Up)&& requestedUp.size()==0 && requestedDown.size()==0){
                            elevator.setDirection(Destination.Stopped);
                        }

                        ArrayList<MyPassenger> priorityPassengers = new ArrayList<>();

                        if (elevator.getDirection() == Destination.Down) {
                            priorityPassengers = downDirectedPeople;
                        } else if (elevator.getDirection() == Destination.Up) {
                            priorityPassengers = upDirectedPeople;
                        }

                        while (priorityPassengers.size() != 0 && elevator.getPassengersList().size() < payloadWeight) {
                            elevator.setPassengersList(priorityPassengers.get(0));
                            passengersList.remove(priorityPassengers.get(0));
                            priorityPassengers.remove(0);
                        }

                    } else {
                        if (currentFloor == floorsAmount) {
                            elevator.setDirection(Destination.Down);
                        }
                    }

                    usedElevators++;
                    System.out.println(elevator);
                }
                Thread.sleep(interval);
            } catch (InterruptedException exception) {
                System.out.println("error");
            }
        }
    }
}

