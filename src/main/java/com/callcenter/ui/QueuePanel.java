package com.callcenter.ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class QueuePanel extends JPanel implements Refreshable {

    private DefaultTableModel tableModel;
    private JTable table;

    public QueuePanel() {
        setLayout(new BorderLayout());

        tableModel = new DefaultTableModel(
                new Object[]{"ID", "Caller", "Priority", "Status"}, 0);

        table = new JTable(tableModel);

        JButton refreshBtn = new JButton("Refresh");
        refreshBtn.addActionListener(e -> refresh());

        JButton updateBtn = new JButton("Mark In Progress");
        updateBtn.addActionListener(e -> updateSelectedCall());

        JPanel topPanel = new JPanel();
        topPanel.add(updateBtn);
        topPanel.add(refreshBtn);

        add(topPanel, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);

        loadData();
    }

    @Override
    public void refresh() {
        loadData();
        table.revalidate();
        table.repaint();
    }

    private void updateSelectedCall() {
        int row = table.getSelectedRow();

        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a call first");
            return;
        }

        String callId = table.getValueAt(row, 0).toString();

        try {
            ApiClient.post("/calls/" + callId + "/update?status=IN_PROGRESS", "");

            JOptionPane.showMessageDialog(this, "Call assigned successfully!");

            MainUI.refreshAll();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void loadData() {
        try {
            String response = ApiClient.get("/calls/queue");

            System.out.println("QUEUE RESPONSE: " + response);

            tableModel.setRowCount(0);

            if (response.equals("[]")) return;

            String[] items = response.split("\\},\\{");

            for (String item : items) {
                if (!item.contains("id")) continue;

                String id = extract(item, "id");
                String name = extract(item, "callerName");
                String priority = extract(item, "priority");
                String status = extract(item, "status");

                tableModel.addRow(new Object[]{id, name, priority, status});
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error loading queue");
        }
    }

    private String extract(String json, String key) {
        try {
            String[] parts = json.split("\"" + key + "\":");
            String value = parts[1].split(",")[0];
            return value
                    .replace("\"", "")
                    .replace("}", "")
                    .replace("]", "")
                    .trim();
        } catch (Exception e) {
            return "";
        }
    }
}