import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class App {
    private final DefaultTableModel tableModel;
    private final JTable table; // Made table a field to easily access selected rows
    private final JTextField nameField;
    private final JTextField emailField;
    private final JTextField phoneField;

    public App() {
        tableModel = new DefaultTableModel(
                new String[]{"Name", "Email", "Phone"},
                0
        );
        table = new JTable(tableModel);

        nameField = new JTextField(20);
        emailField = new JTextField(20);
        phoneField = new JTextField(20);

        JFrame frame = new JFrame("Contact Form");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout(10, 10));

        frame.add(createFormPanel(), BorderLayout.NORTH);
        frame.add(createTablePanel(), BorderLayout.CENTER);
        frame.add(createActionPanel(), BorderLayout.SOUTH); // Added action buttons at the bottom

        // Listener to populate fields when a row is selected
        table.getSelectionModel().addListSelectionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                nameField.setText(tableModel.getValueAt(selectedRow, 0).toString());
                emailField.setText(tableModel.getValueAt(selectedRow, 1).toString());
                phoneField.setText(tableModel.getValueAt(selectedRow, 2).toString());
            }
        });

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Input Contact"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1;
        panel.add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;
        panel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1;
        panel.add(emailField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0;
        panel.add(new JLabel("Phone:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1;
        panel.add(phoneField, gbc);

        JButton addButton = new JButton("Add to List");
        addButton.addActionListener(e -> addContact());

        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.weightx = 0;
        panel.add(addButton, gbc);

        return panel;
    }

    private JScrollPane createTablePanel() {
        table.setRowHeight(24);
        table.getTableHeader().setReorderingAllowed(false);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // Restrict to one row at a time

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Contact List"));
        scrollPane.setPreferredSize(new Dimension(500, 200));
        return scrollPane;
    }

    // New panel containing Edit and Delete functionalities
    private JPanel createActionPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        JButton editButton = new JButton("Update Selected");
        JButton deleteButton = new JButton("Delete Selected");

        editButton.addActionListener(e -> editContact());
        deleteButton.addActionListener(e -> deleteContact());

        panel.add(editButton);
        panel.add(deleteButton);
        return panel;
    }

    private void addContact() {
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();

        if (validateInputs(name, email, phone)) {
            tableModel.addRow(new Object[]{name, email, phone});
            clearFields();
        }
    }

    // New method to handle editing/updating data
    private void editContact() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            showWarning("Please select a contact from the table to update.");
            return;
        }

        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();

        if (validateInputs(name, email, phone)) {
            tableModel.setValueAt(name, selectedRow, 0);
            tableModel.setValueAt(email, selectedRow, 1);
            tableModel.setValueAt(phone, selectedRow, 2);
            clearFields();
            table.clearSelection();
        }
    }

    // New method to handle deleting data
    private void deleteContact() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            showWarning("Please select a contact from the table to delete.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                null,
                "Are you sure you want to delete this contact?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            tableModel.removeRow(selectedRow);
            clearFields();
            table.clearSelection();
        }
    }

    private boolean validateInputs(String name, String email, String phone) {
        if (name.isEmpty() || email.isEmpty() || phone.isEmpty()) {
            showWarning("Please fill in Name, Email, and Phone.");
            return false;
        }
        return true;
    }

    private void showWarning(String message) {
        JOptionPane.showMessageDialog(
                null,
                message,
                "Validation",
                JOptionPane.WARNING_MESSAGE
        );
    }

    private void clearFields() {
        nameField.setText("");
        emailField.setText("");
        phoneField.setText("");
        nameField.requestFocus();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(App::new);
    }
}