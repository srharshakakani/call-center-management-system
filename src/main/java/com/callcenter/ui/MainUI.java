package com.callcenter.ui;

import javax.swing.*;

public class MainUI {

    public static QueuePanel queuePanel;
    public static DashboardPanel dashboardPanel;
    public static AgentPanel agentPanel;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Call Center Management");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 600);

            JTabbedPane tabs = new JTabbedPane();

            queuePanel = new QueuePanel();
            dashboardPanel = new DashboardPanel();
            agentPanel = new AgentPanel();

            tabs.add("Queue", queuePanel);
            tabs.add("New Call", new NewCallPanel());
            tabs.add("Agents", agentPanel);
            tabs.add("Dashboard", dashboardPanel);

            frame.add(tabs);
            frame.setVisible(true);
        });
    }

    // FIXED REFRESH METHOD
    public static void refreshAll() {
        SwingUtilities.invokeLater(() -> {
            if (queuePanel != null) queuePanel.refresh();
            if (dashboardPanel != null) dashboardPanel.refresh();
            if (agentPanel != null) agentPanel.refresh();
        });
    }
}