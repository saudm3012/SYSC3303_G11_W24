public class MainSchedulerData {
    public static void main(String[] args) {
        // Example 1: FLOOR request
        boolean[] floorLamps1 = {true, false, true, false, true};
        SchedulerData floorRequestData = new SchedulerData(1, SchedulerData.FLOOR_REQUEST, floorLamps1, 2, 4);

        // Example 2: MOVE/STOP request
        SchedulerData moveStopRequestData = new SchedulerData(2, SchedulerData.MOVE_REQUEST, true, false, true);

        // Example 3: CONTINUE/DOOR request
        SchedulerData continueDoorRequestData = new SchedulerData(3, SchedulerData.CONTINUE_REQUEST);

        // Display information about the instances
        displaySchedulerData(floorRequestData);
        displaySchedulerData(moveStopRequestData);
        displaySchedulerData(continueDoorRequestData);
    }

    private static void displaySchedulerData(SchedulerData schedulerData) {
        System.out.println("Elevator Number: " + schedulerData.getElevatorNumber());
        System.out.println("Mode: " + schedulerData.getRequestMode());

        if (schedulerData.getRequestMode() == SchedulerData.FLOOR_REQUEST) {
            System.out.println("Requested Floors: " + schedulerData.getCurrentFloor());
            System.out.println("Destination Floor: " + schedulerData.getDestinationFloor());
            System.out.println("Floor Lamps: " + arrayToString(schedulerData.getFloorLamps()));
        } else if (schedulerData.getRequestMode() == SchedulerData.MOVE_REQUEST) {
            System.out.println("Move Up: " + schedulerData.isMovingUp());
            System.out.println("Move Down: " + schedulerData.isMovingDown());
            System.out.println("Door Open: " + schedulerData.isDoorOpen());
        } else {
            // For CONTINUE/DOOR request, no additional information to display
        }

        System.out.println("------");
    }

    private static String arrayToString(boolean[] array) {
        StringBuilder result = new StringBuilder("[");
        for (boolean value : array) {
            result.append(value).append(", ");
        }
        // Remove the trailing comma and space
        if (result.length() > 1) {
            result.setLength(result.length() - 2);
        }
        result.append("]");
        return result.toString();
    }
}


