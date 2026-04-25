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
        refreshBtn.addActionListener(e -> loadData());

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
    }

    private void updateSelectedCall() {
        int row = table.getSelectedRow();

        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a call first");
            return;
        }

        String id = table.getValueAt(row, 0).toString();

        try {
            // assumes agentId = 1 exists
            ApiClient.patch("/calls/" + id + "?status=IN_PROGRESS&agentId=1");

            JOptionPane.showMessageDialog(this, "Call updated!");
            MainUI.refreshAll();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error updating call");
        }
    }

    private void loadData() {
        try {
            String response = ApiClient.get("/calls/queue");

            tableModel.setRowCount(0);

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
            return value.replace("\"", "").replace("}", "");
        } catch (Exception e) {
            return "";
        }
    }
}