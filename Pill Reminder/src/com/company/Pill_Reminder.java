package com.company;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class Pill_Reminder {

    private JFrame frame;
    private JTextField pillNameField;
    private JTextField intervalField;
    private Timer timer;
    private JSpinner alarmTimeSpinner;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }

            new Pill_Reminder().initialize();
        });
    }

    private void initialize() {
        frame = new JFrame("Pill Reminder");
        frame.setBounds(100, 100, 400, 250);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setBackground(Color.CYAN);
        frame.getContentPane().add(panel, BorderLayout.CENTER);
        panel.setLayout(new GridLayout(4, 2, 10, 10));

        Font labelFont = new Font("Arial", Font.PLAIN, 16);

        JLabel pillNameLabel = new JLabel("Pill Name:");
        pillNameLabel.setFont(labelFont);
        panel.add(pillNameLabel);

        pillNameField = new JTextField();
        pillNameField.setFont(labelFont);
        panel.add(pillNameField);

        JLabel intervalLabel = new JLabel("Interval (minutes):");
        intervalLabel.setFont(labelFont);
        panel.add(intervalLabel);

        intervalField = new JTextField();
        intervalField.setFont(labelFont);
        panel.add(intervalField);

        JLabel alarmTimeLabel = new JLabel("Set Time:");
        alarmTimeLabel.setFont(labelFont);
        panel.add(alarmTimeLabel);

        alarmTimeSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(alarmTimeSpinner, "HH:mm");
        alarmTimeSpinner.setEditor(dateEditor);
        alarmTimeSpinner.setFont(labelFont);
        panel.add(alarmTimeSpinner);

        JButton startButton = new JButton("Start Reminder");
        startButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                startReminder();
            }
        });
        startButton.setFont(labelFont);
        startButton.setBackground(Color.GREEN);
        startButton.setForeground(Color.BLACK);
        panel.add(startButton);

        JButton stopButton = new JButton("Stop Reminder");
        stopButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                stopReminder();
            }
        });
        stopButton.setFont(labelFont);
        stopButton.setBackground(Color.RED);
        stopButton.setForeground(Color.BLACK);
        panel.add(stopButton);

        frame.setVisible(true);
    }

    private void startReminder() {
        String pillName = pillNameField.getText().trim();
        String intervalStr = intervalField.getText().trim();

        if (pillName.isEmpty() || intervalStr.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Please enter pill name and interval.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int interval;
        try {
            interval = Integer.parseInt(intervalStr);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(frame, "Invalid interval. Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Date alarmTime = (Date) alarmTimeSpinner.getValue();

        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                showReminder(pillName);
            }
        }, alarmTime, interval * 60 * 1000); // Convert minutes to milliseconds
    }

    private void stopReminder() {
        if (timer != null) {
            timer.cancel();
            timer.purge();
        }
    }

    private void showReminder(String pillName) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentTime = dateFormat.format(new Date());

        String message = "Time to take your " + pillName + "!\nCurrent time: " + currentTime;

        UIManager.put("OptionPane.messageForeground", Color.RED);
        UIManager.put("OptionPane.background", Color.GREEN);
        UIManager.put("Panel.background", Color.GREEN);

        JOptionPane.showMessageDialog(frame, message, "Reminder", JOptionPane.INFORMATION_MESSAGE);

        UIManager.put("OptionPane.messageForeground", UIManager.get("OptionPane.messageForeground"));
        UIManager.put("OptionPane.background", UIManager.get("OptionPane.background"));
        UIManager.put("Panel.background", UIManager.get("Panel.background"));
    }
}
