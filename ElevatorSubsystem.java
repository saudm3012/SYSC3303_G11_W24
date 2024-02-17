 public class ElevatorSubsystem extends Thread{
     
     //List of elevators
     private Elevator elevatorList[];
     
     
     /**
      * Create a new elevator subsystem with numElevators and numFloors
      * @param numElevators the number of elevators in the system
      */
     public ElevatorSubsystem(int numFloors, int numElevators) {
         elevatorList = new Elevator[numElevators];
         
         //Initialize the elevators
         for (int i = 0; i < numElevators; i ++) {
             elevatorList[i] = ((new Elevator(i)));
         }

     }
          
     /**
      * Close the sockets of all elevators
      */
     public void closeSockets() {
         // We're finished, so close the sockets.
         for (Elevator e: elevatorList) {
             e.closeSockets();
         }
     }
     
     /**
      * Print a status message in the console
      * @param message the message to be printed
      */
     public void print(String message) {
         System.out.println("ELEVATOR SUBSYSTEM: " + message);
     }
     public void runElevators() {
        for (Elevator e: elevatorList) {
			e.start();
		}
    }
 }