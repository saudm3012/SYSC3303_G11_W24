package Gui;
import javax.swing.*;
import java.awt.*;

// @author Riya Arora (101190033)

public class ElevatorGUI extends JFrame {

    private static JLabel[] elevatorCurrentFloor;
    private static JLabel[] elevatorCurrentStatus;
    private static JLabel[] elevatorPassengers;
    //private static JLabel[] elevatorDoorFault;
    //private static JLabel[] elevatorFloorFault;
    private static JLabel[] elevatorFault; // Combined fault label
    private static ElevatorPanel[] elevatorPanels;

    private JLabel throughputLabel;


    public ElevatorGUI() {
        // Set up the main window
        super("Elevator GUI");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLayout(new GridLayout(2, 4));// 2 rows 4 elevators
        setLocationRelativeTo(null);

        // Create the elevator status panels
        elevatorCurrentFloor = new JLabel[4];
        elevatorCurrentStatus = new JLabel[4];
        elevatorPassengers = new JLabel[4]; // 4 elevators
        elevatorFault = new JLabel[4]; // Combined fault label
        JPanel[] elevatorStatusPanels = new JPanel[4];
        elevatorStatusPanels = new JPanel[4]; // Initialize elevatorStatusPanels array


        //throughputLabel = new JLabel("Throughput: ");
        //throughputLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        //add(throughputLabel, BorderLayout.NORTH);

        for (int i = 0; i < 4; i++) {
            elevatorCurrentFloor[i] = new JLabel("Current floor: 0");
            elevatorCurrentStatus[i] = new JLabel("State: IDLE");
            elevatorPassengers[i] = new JLabel("0 passengers");
            //elevatorDoorFault[i] = new JLabel("Door fault: None");
            //elevatorFloorFault[i] = new JLabel("Floor fault: None");
            elevatorFault[i] = new JLabel("Fault: NONE"); // Initialize fault label

            elevatorStatusPanels[i] = new JPanel();
            elevatorStatusPanels[i].setLayout(new GridLayout(4, 1));
            elevatorStatusPanels[i].setBackground(Color.LIGHT_GRAY);
            elevatorStatusPanels[i].setBorder(BorderFactory.createLineBorder(Color.BLACK));
            elevatorStatusPanels[i].add(elevatorCurrentFloor[i]);
            elevatorStatusPanels[i].add(elevatorCurrentStatus[i]);
            elevatorStatusPanels[i].add(elevatorPassengers[i]);
            //elevatorStatusPanels[i].add(elevatorDoorFault[i]); // Add door fault label
            //elevatorStatusPanels[i].add(elevatorFloorFault[i]); // Add floor fault label
            elevatorStatusPanels[i].add(elevatorFault[i]); // Add fault label

            add(elevatorStatusPanels[i]);
        }

        // Create the elevator panels
        elevatorPanels = new ElevatorPanel[4]; // 3 elevators
        for (int i = 0; i < 4; i++) { // 3 elevators
            elevatorPanels[i] = new ElevatorPanel();
            add(elevatorPanels[i]);
        }

        // Show the GUI
        setVisible(true);
    }

    public void updateThroughput(float throughput) {
        throughputLabel.setText("Throughput: " + String.format("%.2f", throughput));
    }

    public void updateStatus(int elevatorId, int currentFloor, String state, int numPassengers, String fault) {
        if (elevatorId >= 0 && elevatorId < 4) {
            elevatorCurrentFloor[elevatorId].setText("Current floor: " + currentFloor);
            elevatorCurrentStatus[elevatorId].setText("State: " + state);
            elevatorPassengers[elevatorId].setText(numPassengers + " passengers");
            elevatorFault[elevatorId].setText("Fault: " + fault); 

            elevatorPanels[elevatorId].setCurrentFloor(currentFloor);
            //elevatorPanels[elevatorId].setDestinationFloor(destinationFloor);
            elevatorPanels[elevatorId].setNumPassengers(numPassengers);
            elevatorPanels[elevatorId].repaint();
        } else {
            System.err.println("Invalid elevator ID: " + elevatorId);
        }
        System.out.println(elevatorId + " " + currentFloor + " " + state + " " + numPassengers

        );
    }

    public int getCurrentFloor(int elevatorId) {
        //elevator logic to get the current floor of the elevator with ID elevatorId
        return elevatorPanels[elevatorId].currentFloor;
    }

    public int getDestinationFloor(int elevatorId) {
        return elevatorPanels[elevatorId].destinationFloor;
    }

    public static void main(String[] args) {
        // Create and show the GUI with 3 elevators and 5 floors
        SwingUtilities.invokeLater(ElevatorGUI::new);
    }

    public class ElevatorPanel extends JPanel {
        private int currentFloor = 0;
        private int destinationFloor = 5;

        public void setCurrentFloor(int currentFloor) {
            this.currentFloor = currentFloor;
        }

        public void paintComponent(Graphics g) {
            super.paintComponent(g);

            // Draw the elevator cabin
            g.setColor(Color.MAGENTA); // Elevator color
            int elevatorWidth = 100;
            int elevatorHeight = 20;
            int elevatorX = 50 + 200 * (getWidth() / 1200);
            int elevatorY = getHeight() - currentFloor * (getHeight() / 22) - elevatorHeight / 2;
            g.fill3DRect(elevatorX, elevatorY, elevatorWidth, elevatorHeight, true); // Fill with 3D effect
            g.setColor(Color.BLACK);
            g.drawRect(elevatorX, elevatorY, elevatorWidth, elevatorHeight); // Draw outline

            // Draw the rope
            g.setColor(Color.GRAY);
            int ropeX = elevatorX + elevatorWidth / 2;
            int ropeY = 0;
            int ropeEndY = elevatorY;
            g.drawLine(ropeX, ropeY, ropeX, ropeEndY); // Draw rope

            // Draw the floors
            g.setColor(Color.BLACK); // Floor color
            g.setFont(new Font("Arial", Font.PLAIN, 12));
            for (int i = 0; i <= 22; i++) { // 5 floors
                g.drawString(Integer.toString(i), 5 + 200 * (getWidth() / 1200), getHeight() - i * (getHeight() / 22) + 5);
                g.drawLine(50 + 200 * (getWidth() / 1200), getHeight() - i * (getHeight() / 22), 150 + 200 * (getWidth() / 1200), getHeight() - i * (getHeight() / 22));
            }
        }

        public void setDestinationFloor(int destinationFloor) {
            this.destinationFloor = destinationFloor;
        }

        public void setNumPassengers(int numPassengers) {
        }
    }
}
