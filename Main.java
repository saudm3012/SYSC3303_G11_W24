public class Main {
    public static void main (String args[]){
        Thread floorSubsystem = new Thread(new FloorSubsystem("data.txt"));
        Thread scheduler =  new Thread(new Scheduler());
        Thread elevator =  new Thread(new Elevator());
        floorSubsystem.start();
        scheduler.start();
        elevator.start();
    }
}
