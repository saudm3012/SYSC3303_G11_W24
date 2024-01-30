public class Main {
    public static void main (String args[]){
        Thread floorSubsystem = new Thread(new FloorSubsystem("data.txt"));
        floorSubsystem.start();
    }
}
