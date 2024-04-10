package Gui;
import javax.swing.*;
import java.awt.*;

// @author Riya Arora (101190033)

public class ElevatorGUI extends JFrame {

    private static JLabel[] elevatorCurrentFloor;
    private static JLabel[] elevatorCurrentStatus;
    private static JLabel[] elevatorPassengers;
    private static JLabel[] elevatorFault;
    private static ElevatorPanel[] elevatorPanels;

    private JLabel throughputLabel;
    private JLabel requestsCompletedLabel;
    private JLabel expectedRequestsLabel;

    public ElevatorGUI() {
        super("Elevator GUI");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLayout(new GridLayout(2, 4)); // 2 rows, 4 columns
        setLocationRelativeTo(null);

        // Create panel for metrics
        JPanel metricsPanel = new JPanel();
        metricsPanel.setLayout(new BoxLayout(metricsPanel, BoxLayout.Y_AXIS)); // Vertical layout
        metricsPanel.setPreferredSize(new Dimension(20, getHeight())); // Set preferred width
        throughputLabel = new JLabel("Throughput: ");
        requestsCompletedLabel = new JLabel("Requests Completed: ");
        expectedRequestsLabel = new JLabel("Expected Requests: ");
        metricsPanel.add(throughputLabel);
        metricsPanel.add(requestsCompletedLabel);
        metricsPanel.add(expectedRequestsLabel);
        add(metricsPanel);

        // Create panel for elevator status
        JPanel elevatorStatusPanel = new JPanel(new GridLayout(1, 4)); // 1 row, 4 columns
        elevatorCurrentFloor = new JLabel[4];
        elevatorCurrentStatus = new JLabel[4];
        elevatorPassengers = new JLabel[4];
        elevatorFault = new JLabel[4];
        for (int i = 0; i < 4; i++) {
            elevatorCurrentFloor[i] = new JLabel("Current floor: 0");
            elevatorCurrentStatus[i] = new JLabel("State: IDLE");
            elevatorPassengers[i] = new JLabel("0 passengers");
            elevatorFault[i] = new JLabel("Fault: NONE");

            JPanel elevatorStatusPanelSingle = new JPanel(new GridLayout(4, 1)); // 4 rows, 1 column
            elevatorStatusPanelSingle.setBackground(Color.LIGHT_GRAY);
            elevatorStatusPanelSingle.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            elevatorStatusPanelSingle.add(elevatorCurrentFloor[i]);
            elevatorStatusPanelSingle.add(elevatorCurrentStatus[i]);
            elevatorStatusPanelSingle.add(elevatorPassengers[i]);
            elevatorStatusPanelSingle.add(elevatorFault[i]);

            elevatorStatusPanel.add(elevatorStatusPanelSingle);
        }
        add(elevatorStatusPanel);

        // Add empty panel for spacing
        add(new JPanel());

        // Create panel for elevator visualization
        JPanel elevatorVisualizationPanel = new JPanel(new GridLayout(1, 4)); // 1 row, 4 columns
        elevatorPanels = new ElevatorPanel[4];
        for (int i = 0; i < 4; i++) {
            elevatorPanels[i] = new ElevatorPanel();
            elevatorVisualizationPanel.add(elevatorPanels[i]);
        }
        add(elevatorVisualizationPanel);

        // Show the GUI
        setVisible(true);
    }

    public void updateMetrics(float throughput, int requestsCompleted, int expectedRequests) {
        throughputLabel.setText("Throughput: " + String.format("%.2f", throughput));
        requestsCompletedLabel.setText("Requests Completed: " + requestsCompleted);
        expectedRequestsLabel.setText("Expected Requests: " + expectedRequests);
    }

    public void updateStatus(int elevatorId, int currentFloor, String state, int numPassengers, String fault) {
        if (elevatorId >= 0 && elevatorId < 4) {
            elevatorCurrentFloor[elevatorId].setText("Current floor: " + currentFloor);
            elevatorCurrentStatus[elevatorId].setText("State: " + state);
            elevatorPassengers[elevatorId].setText(numPassengers + " passengers");
            elevatorFault[elevatorId].setText("Fault: " + fault);

            // Apply color changes based on fault
            if (fault.equals("DOOR")) {
                elevatorPanels[elevatorId].setBackground(Color.YELLOW);
            } else if (fault.equals("FLOOR")) {
                elevatorPanels[elevatorId].setBackground(Color.RED);
            } else {
                elevatorPanels[elevatorId].setBackground(null);
            }

            elevatorPanels[elevatorId].setCurrentFloor(currentFloor);
            elevatorPanels[elevatorId].setNumPassengers(numPassengers);
            elevatorPanels[elevatorId].repaint();
        } else {
            System.err.println("Invalid elevator ID: " + elevatorId);
        }
    }

    public int getCurrentFloor(int elevatorId) {
        return elevatorPanels[elevatorId].currentFloor;
    }

    public int getDestinationFloor(int elevatorId) {
        return elevatorPanels[elevatorId].destinationFloor;
    }

    public static void main(String[] args) {
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
            g.setColor(Color.MAGENTA);
            int elevatorWidth = 100;
            int elevatorHeight = 20;
            int elevatorX = 50 + 200 * (getWidth() / 1200);
            int elevatorY = getHeight() - currentFloor * (getHeight() / 22) - elevatorHeight / 2;
            g.fill3DRect(elevatorX, elevatorY, elevatorWidth, elevatorHeight, true);
            g.setColor(Color.BLACK);
            g.drawRect(elevatorX, elevatorY, elevatorWidth, elevatorHeight);

            // Draw the rope
            g.setColor(Color.GRAY);
            int ropeX = elevatorX + elevatorWidth / 2;
            int ropeY = 0;
            int ropeEndY = elevatorY;
            g.drawLine(ropeX, ropeY, ropeX, ropeEndY);

            // Draw the floors
            g.setColor(Color.BLACK);
            g.setFont(new Font("Arial", Font.PLAIN, 12));
            for (int i = 0; i <= 22; i++) {
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