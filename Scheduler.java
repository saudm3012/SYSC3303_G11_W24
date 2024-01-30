import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

class Scheduler {
    private BlockingQueue<String> floorRequestsQueue;  // Queue for receiving floor requests from the Floor subsystem
    private BlockingQueue<String> elevatorRequestsQueue;  // Queue for receiving elevator requests from the Elevator subsystem

    public Scheduler() {
        this.floorRequestsQueue = new LinkedBlockingQueue<>();
        this.elevatorRequestsQueue = new LinkedBlockingQueue<>();
    }

    // Method to receive floor requests from the Floor subsystem
    public void receiveFloorRequest(String request) {
        try {
            floorRequestsQueue.put(request);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // Method to receive elevator requests from the Elevator subsystem
    public void receiveElevatorRequest(String request) {
        try {
            elevatorRequestsQueue.put(request);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // Method to process requests and coordinate between the subsystems
    public void processRequests() {
        // Implement the logic to coordinate between the Floor and Elevator subsystems
        // For now, let's just print the received requests as an example
        while (true) {
            try {
                String floorRequest = floorRequestsQueue.take();
                System.out.println("Received Floor Request: " + floorRequest);

                // Add your logic to determine how to handle the request (e.g., assign an elevator)

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        Scheduler scheduler = new Scheduler();

        // For testing purposes, simulate receiving floor requests
        new Thread(() -> {
            for (int i = 1; i <= 7; i++) {
                String request = "Floor " + i + " Up";
                scheduler.receiveFloorRequest(request);

                try {
                    Thread.sleep(1000);  // Simulate time between requests
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        // Start processing requests
        scheduler.processRequests();
    }
}

