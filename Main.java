import java.io.IOException;

public class Main {
    public static void main (String args[]) throws IOException {
        //Thread floorSubsystem = new Thread(new FloorSubsystem("data.txt"));
        InputReader datafile = new InputReader("data.txt");
        FloorSubsystem floor_sys = new FloorSubsystem();
        floor_sys.addPacketToQueue(datafile.getNextPacket());
        Thread floorSubsystem = new Thread(floor_sys);
        Thread scheduler =  new Thread(new Scheduler());
        Thread elevator =  new Thread(new Elevator());
        floorSubsystem.start();
        scheduler.start();
        elevator.start();
    }
}
