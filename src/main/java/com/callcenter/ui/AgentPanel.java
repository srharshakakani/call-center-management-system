package com.callcenter.ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class AgentPanel extends JPanel implements Refreshable {

    private DefaultTableModel tableModel;

    public AgentPanel() {
        setLayout(new BorderLayout());

        tableModel = new DefaultTableModel(
                new Object[]{"ID", "Name", "Extension", "Status"}, 0);

        JTable table = new JTable(tableModel);

        JButton refreshBtn = new JButton("Refresh");
        JButton createBtn = new JButton("Create Agent");

        refreshBtn.addActionListener(e -> loadData());
        createBtn.addActionListener(e -> createAgent());

        JPanel topPanel = new JPanel();
        topPanel.add(createBtn);
        topPanel.add(refreshBtn);

        add(topPanel, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);

        loadData();
    }

    @Override
    public void refresh() {
        loadData();
    }

    private void createAgent() {
        try {
            String name = JOptionPane.showInputDialog("Enter Agent Name:");
            String extension = JOptionPane.showInputDialog("Enter Extension:");

            if (name == null || extension == null) return;

            String json = String.format(
                    "{\"name\":\"%s\",\"extension\":\"%s\",\"status\":\"AVAILABLE\"}",
                    name, extension
            );

            ApiClient.post("/agents", json);

            JOptionPane.showMessageDialog(this, "Agent created!");
            MainUI.refreshAll();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error creating agent");
        }
    }

    private void loadData() {
        try {
            String response = ApiClient.get("/agents");

            tableModel.setRowCount(0);

            if (response.equals("[]")) return;

            String[] items = response.split("\\},\\{");

            for (String item : items) {
                if (!item.contains("id")) continue;

                String id = extract(item, "id");
                String name = extract(item, "name");
                String ext = extract(item, "extension");
                String status = extract(item, "status");

                tableModel.addRow(new Object[]{id, name, ext, status});
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading agents");
        }
    }

    private String extract(String json, String key) {
        try {
            String[] parts = json.split("\"" + key + "\":");
            String value = parts[1].split(",")[0];
            return value
                    .replace("\"", "")
                    .replace("}", "")
                    .replace("]", "")   // removes the trailing bracket
                    .trim();
        } catch (Exception e) {
            return "";
        }
    }
}