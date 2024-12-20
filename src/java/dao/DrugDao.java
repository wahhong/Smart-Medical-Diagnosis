package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Drug;

public class DrugDao {

    private Connection conn = null;
    private String query = null;
    private ResultSet rs = null;

    public DrugDao(Connection conn) {
        this.conn = conn;
    }

    public boolean editDrug(int drugID, String drugName, String drugDesc, int drugQuantity, double drugPrice, String drugExpire, int drugStatus) {
        query = "UPDATE Drug SET drugName = ?, drugDesc = ?, drugQuantity = ?, drugPrice = ?, drugExpire = ?, drugStatus = ? WHERE drugID = ?";

        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, drugName);
            ps.setString(2, drugDesc);
            ps.setInt(3, drugQuantity);
            ps.setDouble(4, drugPrice);
            ps.setString(5, drugExpire);
            ps.setInt(6, drugStatus);
            ps.setInt(7, drugID);

            int rowsUpdated = ps.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            System.err.println("Error updating drug: " + e.getMessage());
            return false;
        }
    }

    public Drug getDrugById(int drugID) {
        query = "SELECT * FROM Drug WHERE drugID = ?";
        Drug drug = null;

        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, drugID);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    drug = new Drug();

                    drug.setDrugID(rs.getInt("drugID"));
                    drug.setDrugName(rs.getString("drugName"));
                    drug.setDrugDesc(rs.getString("drugDesc"));
                    drug.setDrugQuantity(rs.getInt("drugQuantity"));
                    drug.setDrugPrice(rs.getDouble("drugPrice"));
                    drug.setDrugDate(rs.getString("drugDate"));
                    drug.setDrugExpire(rs.getString("drugExpire"));
                    drug.setDrugStatus(rs.getInt("drugStatus"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving drug by ID: " + e.getMessage());
        }

        return drug;
    }

    public boolean updateAddDrugQty(int drugID, int qty) {
        String selectQuery = "SELECT drugQuantity FROM drug WHERE drugID = ?;";
        String updateQuery = "UPDATE drug SET drugQuantity = ? WHERE drugID = ?;";

        try {
            PreparedStatement selectPs = conn.prepareStatement(selectQuery);
            PreparedStatement updatePs = conn.prepareStatement(updateQuery);
            selectPs.setInt(1, drugID);
            ResultSet rs = selectPs.executeQuery();

            if (rs.next()) {
                int currentQty = rs.getInt("drugQuantity");

                if (currentQty >= qty) {
                    int newQty = currentQty + qty;

                    updatePs.setInt(1, newQty);
                    updatePs.setInt(2, drugID);
                    int rowsAffected = updatePs.executeUpdate();

                    return rowsAffected > 0;
                } else {
                    System.out.println("Not enough quantity available.");
                    return false;
                }
            } else {
                System.out.println("Drug not found.");
                return false;
            }
        } catch (SQLException ex) {
            Logger.getLogger(DrugDao.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean updateDrugQty(int drugID, int qty) {
        String selectQuery = "SELECT drugQuantity FROM drug WHERE drugID = ?;";
        String updateQuery = "UPDATE drug SET drugQuantity = ? WHERE drugID = ?;";

        try {
            PreparedStatement selectPs = conn.prepareStatement(selectQuery);
            PreparedStatement updatePs = conn.prepareStatement(updateQuery);
            selectPs.setInt(1, drugID);
            ResultSet rs = selectPs.executeQuery();

            if (rs.next()) {
                int currentQty = rs.getInt("drugQuantity");

                if (currentQty >= qty) {
                    int newQty = currentQty - qty;

                    updatePs.setInt(1, newQty);
                    updatePs.setInt(2, drugID);
                    int rowsAffected = updatePs.executeUpdate();

                    return rowsAffected > 0;
                } else {
                    System.out.println("Not enough quantity available.");
                    return false;
                }
            } else {
                System.out.println("Drug not found.");
                return false;
            }
        } catch (SQLException ex) {
            Logger.getLogger(DrugDao.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean isDrugStockSufficient(int drugID, int quantity) {
        query = "SELECT drugQuantity FROM Drug WHERE drugID = ?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, drugID);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int availableQuantity = rs.getInt("drugQuantity");
                    return availableQuantity >= quantity; // Check if stock is sufficient
                }
            }
        } catch (SQLException e) {
            System.err.println("Error checking drug stock: " + e.getMessage());
        }
        return false;
    }

    public boolean insertDrug(String drugName, String drugDesc, int drugQuantity, double drugPrice, String drugExpire, int drugStatus) {
        query = "INSERT INTO Drug (drugName, drugDesc, drugQuantity, drugPrice, drugExpire, drugStatus) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, drugName);
            ps.setString(2, drugDesc);
            ps.setInt(3, drugQuantity);
            ps.setDouble(4, drugPrice);
            ps.setString(5, drugExpire);
            ps.setInt(6, drugStatus);

            int rowsInserted = ps.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            System.err.println("Error inserting drug: " + e.getMessage());
            return false;
        }
    }

    public List<Drug> getAllDrugAvailable() {
        query = "SELECT * FROM Drug WHERE drugStatus = 1 AND drugExpire > CURDATE() + INTERVAL 14 DAY AND drugQuantity > 0 AND drugDelete = 0;";
        List<Drug> drugList = new ArrayList<>();

        try (PreparedStatement ps = conn.prepareStatement(query); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Drug drug = new Drug();

                drug.setDrugID(rs.getInt("drugID"));
                drug.setDrugName(rs.getString("drugName"));
                drug.setDrugDesc(rs.getString("drugDesc"));
                drug.setDrugQuantity(rs.getInt("drugQuantity"));
                drug.setDrugPrice(rs.getDouble("drugPrice"));
                drug.setDrugDate(rs.getString("drugDate"));
                drug.setDrugExpire(rs.getString("drugExpire"));
                drug.setDrugStatus(rs.getInt("drugStatus"));

                drugList.add(drug);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving drugs: " + e.getMessage());
        }
        return drugList;
    }

    public List<Drug> getAllDrug() {
        query = "SELECT * FROM Drug WHERE drugDelete = 0";
        List<Drug> drugList = new ArrayList<>();

        try (PreparedStatement ps = conn.prepareStatement(query); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Drug drug = new Drug();

                drug.setDrugID(rs.getInt("drugID"));
                drug.setDrugName(rs.getString("drugName"));
                drug.setDrugDesc(rs.getString("drugDesc"));
                drug.setDrugQuantity(rs.getInt("drugQuantity"));
                drug.setDrugPrice(rs.getDouble("drugPrice"));
                drug.setDrugDate(rs.getString("drugDate"));
                drug.setDrugExpire(rs.getString("drugExpire"));
                drug.setDrugStatus(rs.getInt("drugStatus"));

                drugList.add(drug);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving drugs: " + e.getMessage());
        }
        return drugList;
    }

    public boolean deleteDrug(int drugID) {
        String query = "UPDATE drug SET drugDelete = 1 WHERE drugID = ?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, drugID);
            int rowsAffected = ps.executeUpdate();

            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting drug: " + e.getMessage());
            return false;
        }
    }
}
