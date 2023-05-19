public class MyPassenger {
    private int startFloor;
    private int finishFloor;
    private final int direction;

    public MyPassenger(int startFloor, int finishFloor) {
        setStartFloor(startFloor);
        setFinishFloor(finishFloor);

        if (startFloor < 1 || finishFloor < 1) {
            throw new IllegalArgumentException("Этаж не может быть меньше 1");
        } else if (startFloor > 15 || finishFloor > 15) {
            throw new IllegalArgumentException("Этаж не может быть больше 15");
        }

        if (startFloor == finishFloor) {
            throw new IllegalArgumentException("Стартовый и конечный этажи не могут быть одинаковыми");
        }

        this.direction = Integer.compare(finishFloor, startFloor);
    }

    public int getDirection() {
        return direction;
    }

    public int getStartFloor() {
        return startFloor;
    }

    public int getFinishFloor() {
        return finishFloor;
    }

    public void setStartFloor(int startFloor) {
        this.startFloor = startFloor;
    }

    public void setFinishFloor(int finishFloor) {
        this.finishFloor = finishFloor;
    }
}
