package com.callcenter.ui;

import javax.swing.*;

public class MainUI {

    public static QueuePanel queuePanel;
    public static DashboardPanel dashboardPanel;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Call Center Management");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 600);

            JTabbedPane tabs = new JTabbedPane();

            queuePanel = new QueuePanel();
            dashboardPanel = new DashboardPanel();

            tabs.add("Queue", queuePanel);
            tabs.add("New Call", new NewCallPanel());
            tabs.add("Dashboard", dashboardPanel);

            frame.add(tabs);
            frame.setVisible(true);
        });
    }

    public static void refreshAll() {
        if (queuePanel != null) queuePanel.refresh();
        if (dashboardPanel != null) dashboardPanel.refresh();
    }
}