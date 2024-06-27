package ITEMS;

public class InitItem {

    private int people;
    private int minInteral;
    private int MaxInterval;
    private int Elevators;
    private int Floors;
    private int RunInterval;

    public InitItem(int people, int minInteral, int MaxInterval, int Elevators, int Floors, int RunInterval) {
        this.people = people;
        this.minInteral = minInteral;
        this.MaxInterval = MaxInterval;
        this.Elevators = Elevators;
        this.Floors = Floors;
        this.RunInterval = RunInterval;
    }

    public int getPeople() {
        return people;
    }

    public void setPeople(int people) {
        this.people = people;
    }

    public int getMinInteral() {
        return minInteral;
    }

    public void setMinInteral(int minInteral) {
        this.minInteral = minInteral;
    }

    public int getMaxInterval() {
        return MaxInterval;
    }

    public void setMaxInterval(int maxInterval) {
        MaxInterval = maxInterval;
    }

    public int getElevators() {
        return Elevators;
    }

    public void setElevators(int elevators) {
        Elevators = elevators;
    }

    public int getFloors() {
        return Floors;
    }

    public void setFloors(int floors) {
        Floors = floors;
    }

    public int getRunInterval() {
        return RunInterval;
    }

    public void setRunInterval(int runInterval) {
        RunInterval = runInterval;
    }
}
