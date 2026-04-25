package com.callcenter.ui;

import javax.swing.*;
import java.awt.*;

public class NewCallPanel extends JPanel {

    public NewCallPanel() {
        setLayout(new GridLayout(5, 2));

        JTextField nameField = new JTextField();
        JTextField phoneField = new JTextField();

        JComboBox<String> priorityBox = new JComboBox<>(
                new String[]{"LOW", "MEDIUM", "HIGH", "CRITICAL"});

        JButton submitBtn = new JButton("Create Call");

        add(new JLabel("Caller Name:"));
        add(nameField);

        add(new JLabel("Phone:"));
        add(phoneField);

        add(new JLabel("Priority:"));
        add(priorityBox);

        add(new JLabel(""));
        add(submitBtn);

        submitBtn.addActionListener(e -> {
            try {
                String json = String.format(
                        "{\"callerName\":\"%s\",\"phoneNumber\":\"%s\",\"priority\":\"%s\"}",
                        nameField.getText(),
                        phoneField.getText(),
                        priorityBox.getSelectedItem()
                );

                ApiClient.post("/calls", json);

                JOptionPane.showMessageDialog(this, "Call created!");

                // Auto refresh all tabs
                MainUI.refreshAll();

                // Clear fields
                nameField.setText("");
                phoneField.setText("");

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error creating call");
            }
        });
    }
}