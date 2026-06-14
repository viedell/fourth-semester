
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class CafeManagementGUI extends JFrame {

    JTextField txtName, txtCategory, txtPrice, txtStock, txtSearch;
    JTable table;
    DefaultTableModel model;
    int selectedId = -1;

    public CafeManagementGUI() {
        setTitle("Cafe Management System");
        setSize(800, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        DBConnection.createTable();

        JPanel formPanel = new JPanel(new GridLayout(5,2,10,10));

        txtName = new JTextField();
        txtCategory = new JTextField();
        txtPrice = new JTextField();
        txtStock = new JTextField();

        formPanel.add(new JLabel("Menu Name"));
        formPanel.add(txtName);

        formPanel.add(new JLabel("Category"));
        formPanel.add(txtCategory);

        formPanel.add(new JLabel("Price"));
        formPanel.add(txtPrice);

        formPanel.add(new JLabel("Stock"));
        formPanel.add(txtStock);

        JButton btnAdd = new JButton("Add");
        JButton btnUpdate = new JButton("Update");
        JButton btnDelete = new JButton("Delete");

        formPanel.add(btnAdd);
        formPanel.add(btnUpdate);

        add(formPanel, BorderLayout.NORTH);

        model = new DefaultTableModel();
        model.setColumnIdentifiers(new String[]{
                "ID", "Name", "Category", "Price", "Stock"
        });

        table = new JTable(model);
        JScrollPane pane = new JScrollPane(table);
        add(pane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();

        txtSearch = new JTextField(15);
        JButton btnSearch = new JButton("Search");
        bottomPanel.add(new JLabel("Search:"));
        bottomPanel.add(txtSearch);
        bottomPanel.add(btnSearch);
        bottomPanel.add(btnDelete);

        add(bottomPanel, BorderLayout.SOUTH);

        loadTable();

        btnAdd.addActionListener(e -> addData());
        btnUpdate.addActionListener(e -> updateData());
        btnDelete.addActionListener(e -> deleteData());
        btnSearch.addActionListener(e -> searchData());

        table.getSelectionModel().addListSelectionListener(e -> {
            int row = table.getSelectedRow();

            if (row >= 0) {
                selectedId = Integer.parseInt(model.getValueAt(row,0).toString());
                txtName.setText(model.getValueAt(row,1).toString());
                txtCategory.setText(model.getValueAt(row,2).toString());
                txtPrice.setText(model.getValueAt(row,3).toString());
                txtStock.setText(model.getValueAt(row,4).toString());
            }
        });
    }

    void addData() {
        if (emptyFields()) return;

        String sql = "INSERT INTO menu_items(name, category, price, stock) VALUES(?,?,?,?)";

        try(Connection conn = DBConnection.connect();
            PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, txtName.getText());
            pst.setString(2, txtCategory.getText());
            pst.setDouble(3, Double.parseDouble(txtPrice.getText()));
            pst.setInt(4, Integer.parseInt(txtStock.getText()));

            pst.executeUpdate();
            JOptionPane.showMessageDialog(this, "Data Added!");
            clearFields();
            loadTable();

        } catch(Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    void updateData() {
        if(selectedId == -1){
            JOptionPane.showMessageDialog(this, "Select data first!");
            return;
        }

        String sql = "UPDATE menu_items SET name=?, category=?, price=?, stock=? WHERE id=?";

        try(Connection conn = DBConnection.connect();
            PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, txtName.getText());
            pst.setString(2, txtCategory.getText());
            pst.setDouble(3, Double.parseDouble(txtPrice.getText()));
            pst.setInt(4, Integer.parseInt(txtStock.getText()));
            pst.setInt(5, selectedId);

            pst.executeUpdate();
            JOptionPane.showMessageDialog(this, "Data Updated!");
            clearFields();
            loadTable();

        } catch(Exception e){
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    void deleteData() {
        if(selectedId == -1){
            JOptionPane.showMessageDialog(this, "Select data first!");
            return;
        }

        String sql = "DELETE FROM menu_items WHERE id=?";

        try(Connection conn = DBConnection.connect();
            PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setInt(1, selectedId);
            pst.executeUpdate();

            JOptionPane.showMessageDialog(this, "Data Deleted!");
            clearFields();
            loadTable();

        } catch(Exception e){
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    void searchData() {
        model.setRowCount(0);

        String sql = "SELECT * FROM menu_items WHERE name LIKE ?";

        try(Connection conn = DBConnection.connect();
            PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, "%" + txtSearch.getText() + "%");
            ResultSet rs = pst.executeQuery();

            while(rs.next()){
                model.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("category"),
                        rs.getDouble("price"),
                        rs.getInt("stock")
                });
            }

        } catch(Exception e){
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    void loadTable() {
        model.setRowCount(0);

        try(Connection conn = DBConnection.connect();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM menu_items")) {

            while(rs.next()){
                model.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("category"),
                        rs.getDouble("price"),
                        rs.getInt("stock")
                });
            }

        } catch(Exception e){
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    boolean emptyFields(){
        if(txtName.getText().isEmpty() ||
           txtCategory.getText().isEmpty() ||
           txtPrice.getText().isEmpty() ||
           txtStock.getText().isEmpty()) {

            JOptionPane.showMessageDialog(this, "Please fill all fields!");
            return true;
        }
        return false;
    }

    void clearFields(){
        txtName.setText("");
        txtCategory.setText("");
        txtPrice.setText("");
        txtStock.setText("");
        selectedId = -1;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CafeManagementGUI().setVisible(true));
    }
}
