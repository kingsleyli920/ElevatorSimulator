package TOOLS;

import ITEMS.InitItem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

public class InitFrame extends JFrame implements ActionListener {

    private JTextField peopleField, minInterval, maxInterval, elevators, floors, runInterval;
    private JComboBox<String> comboBox;
    private static final InitFrame INSTANCE = new InitFrame();
    private InitItem initItem;
    private boolean confirmed;

    public InitFrame() {
        this.confirmed = false;
    }

    public static InitFrame getINSTANCE() {
        return INSTANCE;
    }

    public void initFrame() {
        initComponents();
    }

    private void initComponents() {
        JPanel panel = new JPanel(new GridLayout(8, 2));

        JButton comfirmButton = new JButton("Confirm");
        JButton resetButton = new JButton("Reset");
        comfirmButton.addActionListener(this);
        resetButton.addActionListener(this);

        MyTextFieldKeyAdaptor myTextFieldKeyAdaptor = new MyTextFieldKeyAdaptor();

        JLabel peopleLabel = new JLabel("Passenger #: ");
        peopleField = new JTextField(10);
        peopleField.addKeyListener(myTextFieldKeyAdaptor);

        JLabel minInterLabel = new JLabel("Minimum # for one passenger (seconds): ");
        minInterval = new JTextField(10);
        minInterval.addKeyListener(myTextFieldKeyAdaptor);

        JLabel maxInterLabel = new JLabel("Maximum # for one passenger (seconds): ");
        maxInterval = new JTextField(10);
        maxInterval.addKeyListener(myTextFieldKeyAdaptor);

        JLabel elevatorLabel = new JLabel("# of elevator: ");
        elevators = new JTextField(10);
        elevators.addKeyListener(myTextFieldKeyAdaptor);

        JLabel floorLabel = new JLabel("Total level: ");
        floors = new JTextField(10);
        floors.addKeyListener(myTextFieldKeyAdaptor);

        JLabel runInterLabel = new JLabel("Elevator running time at each floor (seconds): ");
        runInterval = new JTextField(10);
        runInterval.addKeyListener(myTextFieldKeyAdaptor);

        String mode[] = { "General", "Maintenance", "Emergency" };
        comboBox = new JComboBox<>(mode);
        JLabel modeLabel = new JLabel("Elevator mode: ");

        this.add(panel);
        panel.add(peopleLabel);
        panel.add(peopleField);
        panel.add(minInterLabel);
        panel.add(minInterval);
        panel.add(maxInterLabel);
        panel.add(maxInterval);
        panel.add(elevatorLabel);
        panel.add(elevators);
        panel.add(floorLabel);
        panel.add(floors);
        panel.add(runInterLabel);
        panel.add(runInterval);
        panel.add(modeLabel);
        panel.add(comboBox);
        panel.add(comfirmButton);
        panel.add(resetButton);

        comboBox.setSelectedIndex(0);
        comboBox.setVisible(true);

        this.setTitle("Initialization");
        this.setSize(640, 320);
        this.setLocation(500, 100);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        switch (command) {
            case "Confirm":
                if (!inputValid()) {
                    break;
                }
                int peopleNum = Integer.parseInt(peopleField.getText());
                int minIntervalNum = Integer.parseInt(minInterval.getText());
                int maxIntervalNum = Integer.parseInt(maxInterval.getText());
                int elevatorNum = Integer.parseInt(elevators.getText());
                int floorNum = Integer.parseInt(floors.getText());
                int runIntervalNum = Integer.parseInt(runInterval.getText());
                initItem = new InitItem(peopleNum, minIntervalNum, maxIntervalNum, elevatorNum, floorNum, runIntervalNum);
                checkMode(comboBox.getSelectedIndex());
                confirmed = true;
                this.dispose();
                break;
            case "Reset":
                reset();
                break;
        }
    }

    public boolean inputValid() {
        if (peopleField.getText().isEmpty() || minInterval.getText().isEmpty()
                || maxInterval.getText().isEmpty() || elevators.getText().isEmpty()
                || (floors.getText().isEmpty() || runInterval.getText().isEmpty())) {
            showWarning("Please fill all the fields");
            return false;
        }
        return true;
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public InitItem getInitItem() {
        return initItem;
    }

    private void checkMode(int selectedIndex) {
        switch (selectedIndex) {
            case 0:
                break;
            case 1:
                Random rand = new Random();
                int elevatorNum = initItem.getElevators();
                int numUnderMaintenance = rand.nextInt(elevatorNum);
                showWarning(numUnderMaintenance + " elevator(s) are under maintenance, "
                        + (elevatorNum - numUnderMaintenance) + " elevator(s) are available.");
                initItem.setElevators(elevatorNum - numUnderMaintenance);
                break;
            case 2:
                showWarning("The building in emergency status, no elevator available.");
                initItem.setElevators(0);
                break;
        }
    }

    private void reset() {
        peopleField.setText("");
        minInterval.setText("");
        maxInterval.setText("");
        elevators.setText("");
        floors.setText("");
        runInterval.setText("");
        confirmed = false;
        comboBox.setSelectedIndex(0);
    }

    private void showWarning(String message) {
        JOptionPane.showMessageDialog(null, message);
    }
}

class MyTextFieldKeyAdaptor extends KeyAdapter {

    @Override
    public void keyTyped(KeyEvent e) {
        String digits = "0123456789";

        if (digits.indexOf(e.getKeyChar()) < 0) {
            e.consume();
        }
    }
}
