import java.util.ArrayList;

public class Lift {
    private int id;
    private int currentFloor;
    private int direction;

    private ArrayList<MyPassenger> passengersList = new ArrayList<>();

    Lift(int num){
        setId(num);
        this.currentFloor=1;
    }

    public void move(){
        setCurrentFloor(currentFloor += direction);

        passengersList.removeIf(passenger -> (passenger.getFinishFloor() == currentFloor));
    }

    public int getCurrentFloor() {
        return currentFloor;
    }

    public int getDirection() {
        return direction;
    }

    public ArrayList<MyPassenger> getPassengersList() {
        return passengersList;
    }

    public void setId(int id) {
        this.id = id;
    }
    public int getId() {
        return id;
    }
    public void setCurrentFloor(int currentFloor) {
        this.currentFloor = currentFloor;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public void setPassengersList(MyPassenger passenger) {
        this.passengersList.add(passenger);
    }

    @Override
    public String toString () {
        String direct = "";

        switch (direction) {
            case -1:
                direct = "down";
                break;
            case 1:
                direct = "up";
                break;
            default:
                direct = "stopped";
                break;
        }

        return  new String("Номер лифта: " + id + "; Этаж: " + currentFloor + "; Направление: " + direct + "; Число пассажиров по направлению движения лифта: " + passengersList.size());
    }
}
