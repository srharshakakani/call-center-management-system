package com.callcenter.ui;

import javax.swing.*;
import java.awt.*;

public class DashboardPanel extends JPanel implements Refreshable {

    private JLabel totalCalls = new JLabel();
    private JLabel queued = new JLabel();
    private JLabel inProgress = new JLabel();
    private JLabel resolved = new JLabel();
    private JLabel escalated = new JLabel();
    private JLabel availableAgents = new JLabel();
    private JLabel busyAgents = new JLabel();

    public DashboardPanel() {
        setLayout(new GridLayout(10, 1, 10, 10));

        add(title("Call Summary"));
        add(totalCalls);
        add(queued);
        add(inProgress);
        add(resolved);
        add(escalated);

        add(title("Agent Summary"));
        add(availableAgents);
        add(busyAgents);

        JButton refreshBtn = new JButton("Refresh");
        refreshBtn.addActionListener(e -> loadData());
        add(refreshBtn);

        loadData();
    }

    @Override
    public void refresh() {
        loadData();
    }

    private JLabel title(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 16));
        return label;
    }

    private void loadData() {
        try {
            String response = ApiClient.get("/dashboard/summary");

            totalCalls.setText("Total Calls: " + extract(response, "totalCalls"));
            queued.setText("Queued: " + extract(response, "queued"));
            inProgress.setText("In Progress: " + extract(response, "inProgress"));
            resolved.setText("Resolved: " + extract(response, "resolved"));
            escalated.setText("Escalated: " + extract(response, "escalated"));

            availableAgents.setText("Available Agents: " + extract(response, "availableAgents"));
            busyAgents.setText("Busy Agents: " + extract(response, "busyAgents"));

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading dashboard");
        }
    }

    private String extract(String json, String key) {
        try {
            String[] parts = json.split("\"" + key + "\":");
            String value = parts[1].split(",")[0];
            return value.replace("}", "");
        } catch (Exception e) {
            return "0";
        }
    }
}